$(function () {
	initTableGrid();
  initTimeCond();
  initExportFunction();
  initEvent();
});


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../../dmreportdepnopen/exportExcel', {'params' : JSON.stringify(params)});  
	});

}
function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}


function initTableGrid () {
    $("#jqGrid").jqGrid({
//        url: '../../dmreportdepnopen/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 70, key: true },			
			{ label: '用户名', name: 'username', index: '$USERNAME', width: 80 }, 			
			{ label: '用户姓名', name: 'realname', index: '$REALNAME', width: 80 }, 			
			{ label: '手机号', name: 'phone', index: '$PHONE', width: 80 }, 			
			{ label: '存管ID', name: 'cgUserId', index: '$CG_USER_ID', width: 80 }, 			
			{ label: '普通版待收金额', name: 'norWait', index: '$NOR_WAIT', width: 100 }, 			
			{ label: '最后一次投资时间', name: 'tenderTime', index: '$TENDER_TIME', width: 120 }, 			
			{ label: '最后一次回款时间', name: 'recoverTime', index: '$RECOVER_TIME', width: 120 }, 			
			{ label: '投资次数', name: 'invCou', index: '$INV_COU', width: 70 }, 			
			{ label: '累计投资金额', name: 'tenderCapital', index: '$TENDER_CAPITAL', width: 80 }, 			
			{ label: '用户等级', name: 'lv', index: '$LV', width: 70 }, 			
			{ label: '普通版账户可用余额', name: 'norBalance', index: '$NOR_BALANCE', width: 120 }, 			
			{ label: '可用红包金额', name: 'reward', index: '$REWARD', width: 80 }, 			
			{ label: '投资次数中使用奖励比例', name: 'rewardInv', index: '$REWARD_INV', width: 140 }			
        ],
		viewrecords: true,
        height: $(window).height()-130,
        rowNum: 20,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
//        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDepNopen: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportDepNopen = {};
		},
		update: function (event) {
			var statPeriod = getSelectedRow();
			if(statPeriod == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(statPeriod)
		},
		saveOrUpdate: function (event) {
			var url = vm.dmReportDepNopen.statPeriod == null ? "../dmreportdepnopen/save" : "../dmreportdepnopen/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportDepNopen),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var statPeriods = getSelectedRows();
			if(statPeriods == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../dmreportdepnopen/delete",
				    data: JSON.stringify(statPeriods),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(statPeriod){
			$.get("../dmreportdepnopen/info/"+statPeriod, function(r){
                vm.dmReportDepNopen = r.dmReportDepNopen;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../../dmreportdepnopen/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val().replace(/-/g,"")
	};
	return params;
}