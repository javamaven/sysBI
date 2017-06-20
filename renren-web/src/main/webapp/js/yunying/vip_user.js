$(function () {
	initDetailTableGrid();
	initCountTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
		}
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


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../yunying/dmreportvipuser/exportExcel', {'params' : JSON.stringify(params)});
		}else if(select == 'vip_count'){
			executePost('../yunying/dmreportvipsituation/exportExcel', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [			
			{ label: '时间', name: 'statPeriod', index: '$STAT_PERIOD', width: 90,align:'right' },
			{ label: '用户ID', name: 'oldUserId', index: '$OLD_USER_ID', width: 90 ,align:'right'}, 			
			{ label: '存管ID', name: 'cgUserId', index: '$CG_USER_ID', width: 90 ,align:'right'}, 			
			{ label: '名单用户名', name: 'oldUsername', index: '$OLD_USERNAME', width: 90,align:'right' }, 			
			{ label: '用户名', name: 'username', index: '$USERNAME', width: 90,align:'right' }, 			
			{ label: '姓名', name: 'realname', index: '$REALNAME', width: 90,align:'right' }, 			
			{ label: '名单电话号码', name: 'oldPhone', index: '$OLD_PHONE', width: 100,align:'right' }, 			
			{ label: '电话号码', name: 'phone', index: '$PHONE', width: 100,align:'right' }, 			
			{ label: '名单总待收', name: 'await', index: '$AWAIT', width: 90 ,align:'right'}, 	
			{ label: '当前总待收', name: 'totalReceipt', index: '$TOTAL_RECEIPT', width: 80 ,align:'right'}	,
			{ label: '等级划分', name: 'lv', index: '$LV', width: 80 ,align:'right'}, 			
			{ label: '所属人', name: 'owner', index: '$OWNER', width: 80 ,align:'right'}, 			
			{ label: '账户余额', name: 'balance', index: '$BALANCE', width: 90,align:'right' }, 			
			{ label: '注册时间', name: 'regTime', index: '$REG_TIME', width: 140,align:'right' }, 			
			{ label: '最近登录时间', name: 'loginTime', index: '$LOGIN_TIME', width: 140,align:'right' }, 			
			{ label: '最近一次回款日期', name: 'lastRecoverTime', index: '$LAST_RECOVER_TIME', width: 140 ,align:'right'}, 			
			{ label: '最近一次回款金额', name: 'lastRecoverMoney', index: '$LAST_RECOVER_MONEY', width: 140 ,align:'right'}, 			
			{ label: '最近一次充值日期', name: 'lastRechargeTime', index: '$LAST_RECHARGE_TIME', width: 140,align:'right' }, 			
			{ label: '最近一次充值金额', name: 'lastRechargeMoney', index: '$LAST_RECHARGE_MONEY', width: 140,align:'right' }, 			
			{ label: '累计充值金额', name: 'rechargeMoneyC', index: '$RECHARGE_MONEY_C', width: 130,align:'right' }, 			
			{ label: '账户红包', name: 'voucherMoney', index: '$VOUCHER_MONEY', width: 80,align:'right' }, 			
			{ label: '最近一次提现日期', name: 'lastCashTime', index: '$LAST_CASH_TIME', width: 140 ,align:'right'}, 			
			{ label: '最近一次提现金额', name: 'lastCashMoney', index: '$LAST_CASH_MONEY', width: 100,align:'right' }, 			
			{ label: '累计提现金额', name: 'cashMoneyC', index: '$CASH_MONEY_C', width: 100,align:'right' }, 			
			{ label: '投资次数', name: 'invCou', index: '$INV_COU', width: 80 ,align:'right'}, 			
			{ label: '平均投资期限', name: 'avgPeriod', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '当月投资金额', name: 'monthTender', index: '$MONTH_TENDER', width: 100,align:'right' }, 			
			{ label: '当月年化金额', name: 'monthTenderY', index: '$MONTH_TENDER_Y', width: 100 ,align:'right'}, 			
			{ label: '当月投资次数', name: 'monthTenderCou', index: '$MONTH_TENDER_COU', width: 100 ,align:'right'}, 			
			{ label: '当天投资金额', name: 'dayTender', index: '$DAY_TENDER', width: 100 ,align:'right'}, 			
			{ label: '当天年化金额', name: 'dayTenderY', index: '$DAY_TENDER_Y', width: 100 ,align:'right'}, 			
			{ label: '当天投资次数', name: 'dayTenderCou', index: '$DAY_TENDER_COU', width: 100 ,align:'right'} 			
	
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
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
        }
    });
}

function initCountTableGrid(){
    $("#jqGrid_count").jqGrid({
        datatype: "json",
        colModel: [			
			{ label: '时间', name: 'statPeriod', index: '$STAT_PERIOD', width: 90,align:'right' },
			{ label: '所属人', name: 'owner', index: '$OWNER', width: 80 ,align:'right'}, 			
			{ label: '当天新增投资人数', name: 'dayUserCou', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '当天新增投资次数', name: 'dayTenderCou', index: '$MONTH_TENDER', width: 100,align:'right' }, 			
			{ label: '当天新增年化投资金额', name: 'dayTenderY', index: '$MONTH_TENDER_Y', width: 100 ,align:'right'}, 			
			{ label: '当天新增投资金额', name: 'dayTender', index: '$MONTH_TENDER_COU', width: 100 ,align:'right'} 			
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
        pager: "#jqGridPager_count",
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
        }
    });
    $("#vip_count_div").hide();
}


var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDdzRemain: {}
	},
	methods: {
		reload: function (event) {
			vm.showList = true;
			var select = $("#list_select").children('option:selected').val();
			if(select == 'vip_detail'){
				$("#jqGrid").jqGrid("clearGridData");
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/dmreportvipuser/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/dmreportvipsituation/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}
