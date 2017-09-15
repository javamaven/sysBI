$(function () {
	initDayTable();
	initMonthTable();
	initTotalTable();
	initSelectEvent();
	initExportFunction();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportdalilymarketing/exportExcel', {'params' : JSON.stringify(params)});
	});

}


function initSelectEvent(){
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'day'){
			$("#day_div").show();
			$("#month_div").hide();
			$("#total_div").hide();
		}else if(select == 'month'){
			$("#day_div").hide();
			$("#month_div").show();
			$("#total_div").hide();
		}else if(select == 'total'){
			$("#day_div").hide();
			$("#month_div").hide();
			$("#total_div").show();
		}
	});
	//月报列表，月报汇总切换
	$("#report_type_select").change(function(){
		var select = $(this).children('option:selected').val();
		monthSelect(select);
	});
	
}
function initDayTable(){
    $("#jqGrid_day").jqGrid({
        url: '../dmreportdalilymarketing/dayList',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 80, key: true },
			{ label: '注册人数', name: 'regNum', index: '$REG_NUM', width: 80 }, 			
			{ label: '实名人数', name: 'authNum', index: '$AUTH_NUM', width: 80 }, 			
			{ label: '首投人数', name: 'firstInvNum', index: '$FIRST_INV_NUM', width: 80 }, 			
			{ label: '充值人数', name: 'reNum', index: '$RE_NUM', width: 80 }, 			
			{ label: '投资人数', name: 'invNum', index: '$INV_NUM', width: 80 }, 			
			{ label: '首投金额', name: 'invFirstMoney', index: '$INV_FIRST_MONEY', width: 100 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}    }, 			
			{ label: '年化首投金额', name: 'yInvFirstMoney', index: '$Y_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '投资金额', name: 'invMoney', index: '$INV_MONEY', width: 80, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '年化投资金额', name: 'yInvMoney', index: '$Y_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '充值金额', name: 'reAmount', index: '$RE_AMOUNT', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '提现金额', name: 'wiAmount', index: '$WI_AMOUNT', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '充提差', name: 'pureRecharge', index: '$PURE_RECHARGE', width: 80, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '当日注册当日充值', name: 'dayReAmount', index: '$DAY_RE_AMOUNT', width: 140 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} }, 			
			{ label: '当日注册当日投资', name: 'dayInvMoney', index: '$DAY_INV_MONEY', width: 140, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道注册人数', name: 'cRegNum', index: '$C_REG_NUM', width: 100 }, 			
			{ label: '渠道实名人数', name: 'cAuthNum', index: '$C_AUTH_NUM', width: 100 }, 			
			{ label: '渠道首投人数', name: 'cFirstInvNum', index: '$C_FIRST_INV_NUM', width: 100 }, 			
			{ label: '渠道首投金额', name: 'cInvFirstMoney', index: '$C_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '年化渠道首投金额', name: 'yCInvFirstMoney', index: '$Y_C_INV_FIRST_MONEY', width: 130, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道投资金额', name: 'cInvMoney', index: '$C_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道年化投资金额', name: 'yCInvMoney', index: '$Y_C_INV_MONEY', width: 140 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} }, 			
			{ label: '当月首投用户当日投资', name: 'mInvMoney', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  },
			{ label: '渠道费用', name: 'cCost', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  },
			{ label: '渠道充值', name: 'cRecharge', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }
        ],
        height:  $(window).height()-130,
        rowNum: 20,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
		viewrecords: true,
		rowList : [10,30,50],
        pager: "#jqGridPager_day",
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

function initMonthTable(){
    $("#jqGrid_month").jqGrid({
//        url: '../dmreportdalilymarketing/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 80, key: true },
			{ label: '注册人数', name: 'regNum', index: '$REG_NUM', width: 80 }, 			
			{ label: '实名人数', name: 'authNum', index: '$AUTH_NUM', width: 80 }, 			
			{ label: '首投人数', name: 'firstInvNum', index: '$FIRST_INV_NUM', width: 80 }, 			
//			{ label: '充值人数', name: 'reNum', index: '$RE_NUM', width: 80 }, 			
//			{ label: '投资人数', name: 'invNum', index: '$INV_NUM', width: 80 }, 			
			{ label: '首投金额', name: 'invFirstMoney', index: '$INV_FIRST_MONEY', width: 100 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}    }, 			
			{ label: '年化首投金额', name: 'yInvFirstMoney', index: '$Y_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '投资金额', name: 'invMoney', index: '$INV_MONEY', width: 80, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '年化投资金额', name: 'yInvMoney', index: '$Y_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '充值金额', name: 'reAmount', index: '$RE_AMOUNT', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '提现金额', name: 'wiAmount', index: '$WI_AMOUNT', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '充提差', name: 'pureRecharge', index: '$PURE_RECHARGE', width: 80, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '当日注册当日充值', name: 'dayReAmount', index: '$DAY_RE_AMOUNT', width: 140 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} }, 			
			{ label: '当日注册当日投资', name: 'dayInvMoney', index: '$DAY_INV_MONEY', width: 140, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道注册人数', name: 'cRegNum', index: '$C_REG_NUM', width: 100 }, 			
			{ label: '渠道实名人数', name: 'cAuthNum', index: '$C_AUTH_NUM', width: 100 }, 			
			{ label: '渠道首投人数', name: 'cFirstInvNum', index: '$C_FIRST_INV_NUM', width: 100 }, 			
			{ label: '渠道首投金额', name: 'cInvFirstMoney', index: '$C_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '年化渠道首投金额', name: 'yCInvFirstMoney', index: '$Y_C_INV_FIRST_MONEY', width: 130, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道投资金额', name: 'cInvMoney', index: '$C_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道年化投资金额', name: 'yCInvMoney', index: '$Y_C_INV_MONEY', width: 140 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} }, 			
			{ label: '当月首投用户当月投资', name: 'mInvMoney', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  },		
			{ label: '渠道费用', name: 'cCost', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  },
			{ label: '渠道充值', name: 'cRecharge', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }
   
			],
        height:  $(window).height()-130,
        rowNum: 20,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
		viewrecords: true,
		rowList : [10,30,50],
        pager: "#jqGridPager_month",
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
    $("#month_div").hide();
}
function initTotalTable(){
    $("#jqGrid_total").jqGrid({
//        url: '../dmreportdalilymarketing/totalList',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 80, key: true },
			{ label: '注册人数', name: 'regNum', index: '$REG_NUM', width: 80 }, 			
			{ label: '实名人数', name: 'authNum', index: '$AUTH_NUM', width: 80 }, 			
			{ label: '首投人数', name: 'firstInvNum', index: '$FIRST_INV_NUM', width: 80 }, 			
//			{ label: '充值人数', name: 'reNum', index: '$RE_NUM', width: 80 }, 			
//			{ label: '投资人数', name: 'invNum', index: '$INV_NUM', width: 80 }, 			
			{ label: '首投金额', name: 'invFirstMoney', index: '$INV_FIRST_MONEY', width: 100 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}    }, 			
			{ label: '年化首投金额', name: 'yInvFirstMoney', index: '$Y_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '投资金额', name: 'invMoney', index: '$INV_MONEY', width: 80, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '年化投资金额', name: 'yInvMoney', index: '$Y_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '充值金额', name: 'reAmount', index: '$RE_AMOUNT', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '提现金额', name: 'wiAmount', index: '$WI_AMOUNT', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '充提差', name: 'pureRecharge', index: '$PURE_RECHARGE', width: 80, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '当日注册当日充值', name: 'dayReAmount', index: '$DAY_RE_AMOUNT', width: 140 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} }, 			
			{ label: '当日注册当日投资', name: 'dayInvMoney', index: '$DAY_INV_MONEY', width: 140, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道注册人数', name: 'cRegNum', index: '$C_REG_NUM', width: 100 }, 			
			{ label: '渠道实名人数', name: 'cAuthNum', index: '$C_AUTH_NUM', width: 100 }, 			
			{ label: '渠道首投人数', name: 'cFirstInvNum', index: '$C_FIRST_INV_NUM', width: 100 }, 			
			{ label: '渠道首投金额', name: 'cInvFirstMoney', index: '$C_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '年化渠道首投金额', name: 'yCInvFirstMoney', index: '$Y_C_INV_FIRST_MONEY', width: 130, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道投资金额', name: 'cInvMoney', index: '$C_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '渠道年化投资金额', name: 'yCInvMoney', index: '$Y_C_INV_MONEY', width: 140 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} },
//			{ label: '当月首投用户当月投资', name: 'mInvMoney', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }	
			{ label: '渠道费用', name: 'cCost', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  },
			{ label: '渠道充值', name: 'cRecharge', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }
   
        ],
        height:  $(window).height()-130,
        rowNum: 20,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
		viewrecords: true,
		rowList : [10,30,50],
        pager: "#jqGridPager_total",
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
    $("#total_div").hide();
}
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDalilyMarketing: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportDalilyMarketing = {};
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
			var url = vm.dmReportDalilyMarketing.statPeriod == null ? "../dmreportdalilymarketing/save" : "../dmreportdalilymarketing/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportDalilyMarketing),
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
				    url: "../dmreportdalilymarketing/delete",
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
			$.get("../dmreportdalilymarketing/info/"+statPeriod, function(r){
                vm.dmReportDalilyMarketing = r.dmReportDalilyMarketing;
            });
		},
		reload: function (event) {
			vm.showList = true;
			
			var type = $("#list_select").children('option:selected').val();
			if(type == 'day'){
				$("#jqGrid_day").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../dmreportdalilymarketing/dayList',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(type == 'month'){
				$("#jqGrid_month").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../dmreportdalilymarketing/monthList',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(type == 'total'){
				$("#jqGrid_total").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../dmreportdalilymarketing/totalList',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
			
		}
	}
});

function getParams(){
	var params = {};
	var type = $("#list_select").children('option:selected').val();
	params.type = type;
	return params;
}