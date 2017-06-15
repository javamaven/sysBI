$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportawaitdaily/exportExcel', {'params' : JSON.stringify(params)});  
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

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../dmreportawaitdaily/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 80, key: true,align:'right' },
			{ label: '代收本金', name: 'awaitCapital', index: '$AWAIT_CAPITAL', width: 80,align:'right' }, 			
			{ label: '代收利息', name: 'awaitInterest', index: '$AWAIT_INTEREST', width: 80 ,align:'right'}, 			
			{ label: '已收本金', name: 'yesCapital', index: '$YES_CAPITAL', width: 80 ,align:'right'}, 			
			{ label: '已收利息', name: 'yesInterest', index: '$YES_INTEREST', width: 80 ,align:'right'}, 			
			{ label: '新增代收本金', name: 'addAwaitCapital', index: '$ADD_AWAIT_CAPITAL', width: 80,align:'right' }, 			
			{ label: '新增代收利息', name: 'addAwaitInteres', index: '$ADD_AWAIT_INTERES', width: 80 ,align:'right'}, 			
			{ label: '还款本金', name: 'recoverCapital', index: '$RECOVER_CAPITAL', width: 80 ,align:'right'}, 			
			{ label: '还款利息', name: 'recoverInterest', index: '$RECOVER_INTEREST', width: 80 ,align:'right'}, 			
			{ label: '数据来源', name: 'fromSys', index: '$FROM_SYS', width: 80,align:'right' }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
        multiselect: false,
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportAwaitDaily: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportAwaitDaily = {};
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
			var url = vm.dmReportAwaitDaily.statPeriod == null ? "../dmreportawaitdaily/save" : "../dmreportawaitdaily/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportAwaitDaily),
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
				    url: "../dmreportawaitdaily/delete",
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
			$.get("../dmreportawaitdaily/info/"+statPeriod, function(r){
                vm.dmReportAwaitDaily = r.dmReportAwaitDaily;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../dmreportawaitdaily/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}