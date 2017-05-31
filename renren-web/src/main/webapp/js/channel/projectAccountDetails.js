$(function () {
    $("#jqGrid").jqGrid({
        url: '../dmreportfinrepaymentdetail/list',
        datatype: "json",
        colModel: [			
			{ label: 'statPeriod', name: 'statPeriod', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '项目合同编号', name: 'sourcecaseno', index: '$SOURCECASENO', width: 80 }, 			
			{ label: '借款人', name: 'customername', index: '$CUSTOMERNAME', width: 80 }, 			
			{ label: '借款金额', name: 'payformoney', index: '$PAYFORMONEY', width: 80 }, 			
			{ label: '放款金额', name: 'payformoneyout', index: '$PAYFORMONEYOUT', width: 80 }, 			
			{ label: '计划还款日', name: 'planrepaydate', index: '$PLANREPAYDATE', width: 80 }, 			
			{ label: '实际还款日期', name: 'realredate', index: '$REALREDATE', width: 80 }, 			
			{ label: '应还本金', name: 'remain', index: '$REMAIN', width: 80 }, 			
			{ label: '应还利息', name: 'reinterest', index: '$REINTEREST', width: 80 }, 			
			{ label: '已还本金', name: 'rebackmain', index: '$REBACKMAIN', width: 80 }, 			
			{ label: '已还利息', name: 'rebackinterest', index: '$REBACKINTEREST', width: 80 }, 			
			{ label: '已还罚息', name: 'reamercedmoney', index: '$REAMERCEDMONEY', width: 80 }, 			
			{ label: '已还违约金', name: 'reamercedmoney3', index: '$REAMERCEDMONEY3', width: 80 }, 			
			{ label: '项目结清日', name: 'realgetmoneydate', index: '$REALGETMONEYDATE', width: 80 }, 			
			{ label: '期次', name: 'reindex', index: '$REINDEX', width: 80 }, 			
			{ label: '总期数', name: 'totpmts', index: '$TOTPMTS', width: 80 }, 			
			{ label: '手续费收入', name: 'rebackservice', index: '$REBACKSERVICE', width: 80 }			
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportFinRepaymentdetail: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportFinRepaymentdetail = {};
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
			var url = vm.dmReportFinRepaymentdetail.statPeriod == null ? "../dmreportfinrepaymentdetail/save" : "../dmreportfinrepaymentdetail/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportFinRepaymentdetail),
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
				    url: "../dmreportfinrepaymentdetail/delete",
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
			$.get("../dmreportfinrepaymentdetail/info/"+statPeriod, function(r){
                vm.dmReportFinRepaymentdetail = r.dmReportFinRepaymentdetail;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});