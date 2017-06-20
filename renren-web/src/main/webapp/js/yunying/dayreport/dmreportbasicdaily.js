$(function () {
  initTableGrid();
  initTimeCond();
  initExportFunction();
  initEvent();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportbasicdaily/exportExcel', {'params' : JSON.stringify(params)});  
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
//        url: '../dmreportbasicdaily/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 90, key: true,align:'right' },
			{ label: '投资用户数', name: 'invCou', index: '$INV_COU', width: 90 ,align:'right'}, 			
			{ label: '投资使用红包金额', name: 'usePackMoney', index: '$USE_PACK_MONEY', width: 80 ,align:'right'}, 			
			{ label: '人均红包金额', name: 'avgUsePackMoney', index: '$AVG_USE_PACK_MONEY', width: 70 ,align:'right'}, 			
			{ label: '用户投资本金', name: 'accountMoney', index: '$ACCOUNT_MONEY', width: 100 ,align:'right'}, 			
			{ label: '人均投资本金', name: 'avgMoney', index: '$AVG_MONEY', width: 80 ,align:'right'}, 			
			{ label: '用户投资年化金额', name: 'yearMoney', index: '$YEAR_MONEY', width: 100 ,align:'right'}, 			
			{ label: '人均年化金额', name: 'avgYearMoney', index: '$AVG_YEAR_MONEY', width: 80,align:'right' }, 			
			{ label: '邀请人返利', name: 'spreadsMoney', index: '$SPREADS_MONEY', width: 80 ,align:'right'}, 			
			{ label: '加息成本', name: 'discountCost', index: '$DISCOUNT_COST', width: 80,align:'right' }, 			
			{ label: '人均加息成本', name: 'avgDiscountCost', index: '$AVG_DISCOUNT_COST', width: 80 ,align:'right'}, 			
			{ label: '新增待收（预估）', name: 'forecastAwait', index: '$FORECAST_AWAIT', width: 100,align:'right' }, 			
			{ label: '新增代收（实际）', name: 'fullAwait', index: '$FULL_AWAIT', width: 100 ,align:'right'}, 			
			{ label: '新增待收（放款）', name: 'loanAwait', index: '$LOAN_AWAIT', width: 100 ,align:'right'}, 			
			{ label: '回款金额', name: 'recoverMoney', index: '$RECOVER_MONEY', width: 100,align:'right' }, 			
			{ label: '本月年化投资金额', name: 'monthNh', index: '$MONTH_NH', width: 80,align:'right' }, 			
			{ label: '新增且到12-31后还款的待收（考虑还款方式）（万元）', name: 'newEndYearAwait', index: '$NEW_END_YEAR_AWAIT', width: 190 ,align:'right'}, 			
			{ label: '到12-31后还款的待收金额', name: 'endYearAwait', index: '$END_YEAR_AWAIT', width: 100,align:'right' }, 			
			{ label: '总待收', name: 'allAwait', index: '$ALL_AWAIT', width: 80,align:'right' }			
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportBasicDaily: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportBasicDaily = {};
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
			var url = vm.dmReportBasicDaily.statPeriod == null ? "../dmreportbasicdaily/save" : "../dmreportbasicdaily/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportBasicDaily),
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
				    url: "../dmreportbasicdaily/delete",
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
			$.get("../dmreportbasicdaily/info/"+statPeriod, function(r){
                vm.dmReportBasicDaily = r.dmReportBasicDaily;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../dmreportbasicdaily/list',
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