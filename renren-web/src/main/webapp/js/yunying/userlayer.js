$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initCountTableGrid();
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
    $("#time").datetimepicker({
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
			executePost('../yunying/userlayer/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/userlayer/exportExcel2', {'params' : JSON.stringify(params)});
		}
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'STAT_PERIOD', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '用户ID', name: 'USER_ID', index: '$USER_ID', width: 80 }, 			
			{ label: '用户标签', name: 'LABEL', index: '$LABEL', width: 80 }, 			
			{ label: '周期内投资金额', name: 'SUM_AMT', index: '$SUM_AMT', width: 80 }, 			
			{ label: '周期内投资笔数', name: 'CNT_ORDER', index: '$CNT_ORDER', width: 80 }, 			
			{ label: '项目平均周期', name: 'BORROW_PERIOD', index: '$BORROW_PERIOD', width: 80 }, 			
			{ label: '已沉默时长', name: 'HVNT_PUR', index: '$HVNT_PUR', width: 80 }, 			
			{ label: '距首投时长', name: 'FIRST_DT', index: '$FIRST_DT', width: 80 }, 			
			{ label: '周期内红包使用金额', name: 'COUPON', index: '$COUPON', width: 80 }, 			
			{ label: '当前待收金额', name: 'MONEY_WAIT', index: '$MONEY_WAIT', width: 80 }, 			
			{ label: '当前账户余额', name: 'BALANCE', index: '$BALANCE', width: 80 }, 			
			{ label: '总资产峰值(包含余额)', name: 'MAX_ASSET', index: '$MAX_ASSET', width: 80 }, 			
			{ label: '峰值日期', name: 'TIME_MAX_ASSET', index: '$TIME_MAX_ASSET', width: 80 }, 			
			{ label: '40天内提现金额', name: 'OUT_AMT_40', index: '$OUT_AMT', width: 80 }, 			
			{ label: '90天内提现金额', name: 'OUT_AMT_90', index: '$OUT_CNT_ORDER', width: 80 }, 			
			{ label: '当前持有红包金额', name: 'HOLD_CPON_AMT', index: '$HOLD_CPON_AMT', width: 80 }, 			
			{ label: '当前有效红包个数', name: 'CPON_NUM', index: '$CPON_NUM', width: 80 }			
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
//        shrinkToFit: false,
//        autoScroll: false,
//        multiselect: false,
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
			{ label: '日期', name: 'STAT_PERIOD', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '用户ID', name: 'USER_ID', index: '$USER_ID', width: 80 }, 			
			{ label: '用户标签', name: 'LABEL', index: '$LABEL', width: 80 }, 			
			{ label: '周期内投资金额', name: 'SUM_AMT', index: '$SUM_AMT', width: 80 }, 			
			{ label: '周期内投资笔数', name: 'CNT_ORDER', index: '$CNT_ORDER', width: 80 }, 			
			{ label: '项目平均周期', name: 'BORROW_PERIOD', index: '$BORROW_PERIOD', width: 80 }, 			
			{ label: '已沉默时长', name: 'HVNT_PUR', index: '$HVNT_PUR', width: 80 }, 			
			{ label: '距首投时长', name: 'FIRST_DT', index: '$FIRST_DT', width: 80 }, 			
			{ label: '周期内红包使用金额', name: 'COUPON', index: '$COUPON', width: 80 }, 			
			{ label: '当前待收金额', name: 'MONEY_WAIT', index: '$MONEY_WAIT', width: 80 }, 			
			{ label: '当前账户余额', name: 'BALANCE', index: '$BALANCE', width: 80 }, 			
			{ label: '总资产峰值(包含余额)', name: 'MAX_ASSET', index: '$MAX_ASSET', width: 80 }, 			
			{ label: '峰值日期', name: 'TIME_MAX_ASSET', index: '$TIME_MAX_ASSET', width: 80 }, 			
			{ label: '累计提现金额', name: 'OUT_AMT', index: '$OUT_AMT', width: 80 }, 			
			{ label: '累计提现次数', name: 'OUT_CNT_ORDER', index: '$OUT_CNT_ORDER', width: 80 }, 			
			{ label: '当前持有红包金额', name: 'HOLD_CPON_AMT', index: '$HOLD_CPON_AMT', width: 80 }, 			
			{ label: '当前有效红包个数', name: 'CPON_NUM', index: '$CPON_NUM', width: 80 }			
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
					url: '../yunying/userlayer/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/userlayer/ddylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'time': $("#time").val(),
        	'userid': $("#userid").val()
	};
	return params;
}
