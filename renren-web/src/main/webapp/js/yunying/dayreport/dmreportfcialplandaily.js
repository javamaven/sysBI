$(function () {
  initTableGrid();
  initTimeCond();
  initExportFunction();
  initEvent();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportfcialplandaily/exportExcel', {'params' : JSON.stringify(params)});  
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
//	        url: '../dmreportfcialplandaily/list',
	        datatype: "json",
	        colModel: [			
				{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 90, key: true ,align:'right'},
				{ label: '销售总额', name: 'tenderAmount', index: '$TENDER_AMOUNT', width: 80,align:'right' }, 			
				{ label: '15天期限', name: 'fifteenDay', index: '$FIFTEEN_DAY', width: 80 ,align:'right'}, 			
				{ label: '1个月期限', name: 'oneMonths', index: '$ONE_MONTHS', width: 80 ,align:'right'}, 			
				{ label: '2个月期限', name: 'twoMonths', index: '$TWO_MONTHS', width: 80 ,align:'right'}, 			
				{ label: '3个月期限', name: 'threeMonths', index: '$THREE_MONTHS', width: 80,align:'right' }, 			
				{ label: '6个月期限', name: 'sixMonths', index: '$SIX_MONTHS', width: 80,align:'right' }, 			
				{ label: '9个月期限', name: 'nineMonths', index: '$NINE_MONTHS', width: 80,align:'right' }, 			
				{ label: '12个月期限', name: 'twelveMonths', index: '$TWELVE_MONTHS', width: 90 ,align:'right'}, 			
				{ label: '其他期限', name: 'otherMonths', index: '$OTHER_MONTHS', width: 80 ,align:'right'}, 			
				{ label: '明日锁定期结束（到期）计划金额', name: 'lockEndMoney', index: '$LOCK_END_MONEY', width: 130,align:'right' }, 			
				{ label: '截止今日结束锁定期计划累计金额', name: 'lockEndMoneyHistory', index: '$LOCK_END_MONEY_HISTORY', width: 130,align:'right' }, 			
				{ label: '昨日结束锁定期人数', name: 'lockEndP', index: '$LOCK_END_P', width: 90 ,align:'right'}, 			
				{ label: '截止今日结束锁定期累计总人数', name: 'lockEndPHistory', index: '$LOCK_END_P_HISTORY', width: 130,align:'right' }, 			
				{ label: '理财计划申请退出金额', name: 'quitMoney', index: '$QUIT_MONEY', width: 90,align:'right' }, 			
				{ label: '理财计划申请退出人数', name: 'quitP', index: '$QUIT_P', width: 90 ,align:'right'}, 			
				{ label: '理财计划成功退出金额', name: 'quitSMoney', index: '$QUIT_S_MONEY', width: 90 ,align:'right'}, 			
				{ label: '理财计划成功退出人数', name: 'quitSP', index: '$QUIT_S_P', width: 90,align:'right' }			
	        ],
	        viewrecords: true,
	        height: $(window).height()-150,
	        rowNum: 20,
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
//	        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
	        }
	    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportFcialPlanDaily: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportFcialPlanDaily = {};
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
			var url = vm.dmReportFcialPlanDaily.statPeriod == null ? "../dmreportfcialplandaily/save" : "../dmreportfcialplandaily/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportFcialPlanDaily),
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
				    url: "../dmreportfcialplandaily/delete",
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
			$.get("../dmreportfcialplandaily/info/"+statPeriod, function(r){
                vm.dmReportFcialPlanDaily = r.dmReportFcialPlanDaily;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../dmreportfcialplandaily/list',
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