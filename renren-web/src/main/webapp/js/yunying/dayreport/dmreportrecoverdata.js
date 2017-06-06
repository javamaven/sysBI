$(function () {
  initTableGrid();
  initTimeCond();
  initExportFunction();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportrecoverdata/exportExcel', {'params' : JSON.stringify(params)});  
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
        url: '../dmreportrecoverdata/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 90, key: true,align:'right' },
			{ label: '用户ID', name: 'userId', index: '$USER_ID', width: 90,align:'right' }, 			
			{ label: '存管用户ID', name: 'cgUserId', index: '$CG_USER_ID', width: 90,align:'right' }, 			
			{ label: '用户名称', name: 'username', index: '$USERNAME', width: 90,align:'right' }, 			
			{ label: '手机号', name: 'phone', index: '$PHONE', width: 100 ,align:'right'}, 			
			{ label: '项目ID', name: 'projectId', index: '$PROJECT_ID', width: 100,align:'right' }, 			
			{ label: '回款金额', name: 'money', index: '$MONEY', width: 80 ,align:'right'}, 			
			{ label: '项目期限', name: 'borrowPeriod', index: '$BORROW_PERIOD', width: 80 ,align:'right'}, 			
			{ label: '注册日期', name: 'regTime', index: '$REG_TIME', width: 150,align:'right' }, 			
			{ label: '首投时间', name: 'xmInvOneTime', index: '$XM_INV_ONE_TIME', width: 150,align:'right' }, 			
			{ label: '注册后首投间隔(分)', name: 'xmStJg', index: '$XM_ST_JG', width: 110,align:'right' }, 			
			{ label: '最近一次投资时间', name: 'xmInvLastTime', index: '$XM_INV_LAST_TIME', width: 150 ,align:'right'}, 			
			{ label: '首投到最后一次投资时间间隔(分)', name: 'xmTzJg', index: '$XM_TZ_JG', width: 150 ,align:'right'}, 			
			{ label: '投资总金额', name: 'xmInvMoney', index: '$XM_INV_MONEY', width: 80 ,align:'right'}, 			
			{ label: '投资次数', name: 'xmInvCou', index: '$XM_INV_COU', width: 80 ,align:'right'}, 			
			{ label: '投资次数中使用奖励次数', name: 'useInvPackCou', index: '$USE_INV_PACK_COU', width: 80,align:'right' }, 			
			{ label: '债转次数', name: 'zzFqCou', index: '$ZZ_FQ_COU', width: 80,align:'right' }, 			
			{ label: '投资期限偏好', name: 'periodJq', index: '$PERIOD_JQ', width: 80 ,align:'right'}, 			
			{ label: '累计充值金额', name: 'czMoney', index: '$CZ_MONEY', width: 100 ,align:'right'}, 			
			{ label: '累计提现金额', name: 'txCgMoney', index: '$TX_CG_MONEY', width: 100 ,align:'right'}, 			
			{ label: '本次推送记录数', name: 'dateCou', index: '$DATE_COU', width: 100 ,align:'right'}			
        ],
		viewrecords: true,
        height: 390,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportRecoverData: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportRecoverData = {};
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
			var url = vm.dmReportRecoverData.statPeriod == null ? "../dmreportrecoverdata/save" : "../dmreportrecoverdata/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportRecoverData),
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
				    url: "../dmreportrecoverdata/delete",
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
			$.get("../dmreportrecoverdata/info/"+statPeriod, function(r){
                vm.dmReportRecoverData = r.dmReportRecoverData;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
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