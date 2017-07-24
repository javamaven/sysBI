$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
});

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
		var day =  $("#stat_period").val();
		if(!day){
			alert('请先选择查询日期');
			return;
		}
		executePost('../analyse/exportBusinessAlarm', {'params' : JSON.stringify(params)});  
	});

}

function queryBusinessAlarm(){
	var params = getParams();
	 $.ajax({
	        type: "POST",
	        url: "../analyse/queryBusinessAlarm",
	        async: true,
	        data: JSON.stringify(params),
	        contentType: "application/json;charset=utf-8",
	        success : function(retData) {
	        	var data = retData.data_list;
	        	var row1 = data[1];
	        	var row2 = data[0];
	        	$("#user_num1").html(row1.用户数1 + " &nbsp;&nbsp;&nbsp;<font style='font-weight: bold;'>环比：</font>" + formatNumber((row1.用户数1-row2.用户数1)*100/row2.用户数1,2) + "%");
	        	$("#user_num2").html(row1.用户数2 + " &nbsp;&nbsp;&nbsp;<font style='font-weight: bold;'>环比：</font>" + formatNumber((row1.用户数2-row2.用户数2)*100/row2.用户数2,2) + "%");
	        }
	     });
	 
}

function initTableGrid(){
	   $("#jqGrid").jqGrid({
//	        url: '../yunying/dmreportddzremain/list',
	        datatype: "json",
	        colModel: [			
				{ label: '用户ID', name: 'USER_ID', index: '$STAT_PERIOD', width: 70 ,align:'right'},
				{ label: '存管ID', name: 'CG_USER_ID', index: '$USERNAME', width: 80 ,align:'right'}, 			
				{ label: '用户名', name: 'USERNAME', index: '$REALNAME', width: 80,align:'right' },		
				{ label: '电话', name: 'PHONE', index: '$PHONE', width: 80,align:'right' }, 			
				{ label: '姓名', name: 'REALNAME', index: '$REG_TIME', width: 80 ,align:'right'}, 			
				{ label: '成功提现金额', name: 'availableAmount', index: '$AVAILABLE_AMOUNT_', width: 80,align:'right' }, 			
				{ label: '账户资产权益额', name: 'MONEY_ALL', index: '$COU', width: 80 ,align:'right'}, 			
				{ label: '账户余额', name: 'MONEY_BALANCE', index: '$XM_INV_MONEY', width: 100,align:'right' }, 			
				{ label: '当前待收金额', name: 'MONEY_WAIT', index: '$IS_INTERNAL', width: 80,align:'right' }, 			
				{ label: '注册日期', name: 'REGISTER_TIME', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},	
				
				{ label: '注册后首投间隔（分）', name: 'REG_TO_INVEST', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '平均投资时间间隔（分）', name: 'AVG_INV_TIME', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '平均投资金额', name: 'AVG_INV_MONEY', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				
				{ label: '投资笔数', name: 'INV_NUM', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '使用红包投资笔数', name: 'USE_VOUCHE_NUM', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '转让笔数', name: 'TRANSFER_NUM', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				
				{ label: '标的平均投资期限', name: 'AVG_PERIOD', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '累计充值金额', name: 'MONEY_RECHARGE', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '累计提现金额', name: 'MONEY_WITHDRAW', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				
				{ label: '提现金额/充值金额', name: '提现金额_充值金额', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '距今未复投时长', name: 'NO_INV', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '未来30天内待收金额', name: 'RECOVER_ACCOUNT_WAIT', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				
				{ label: '使用红包总额', name: 'USE_VOUCHE_MONEY', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '存管版代收金额', name: 'MONEY_WAIT_DEP', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '待收金额', name: 'isInternalTuijian', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'},
				{ label: '普通版本持有投资种类', name: 'NOR_PERIOD_TYPE', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'}
	        ],				

			viewrecords: true,
	        height: $(window).height()-180,
	        rowNum: 20,
	        rownumbers: true, 
	        rownumWidth: 25, 
	        autowidth:true,
	        shrinkToFit: false,
	        autoScroll: false,
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
		dmReportDdzRemain: {}
	},
	methods: {
		reload: function (event) {
			var day =  $("#stat_period").val();
			if(!day){
				alert('请先选择查询日期');
				return;
			}
			queryBusinessAlarm();
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../analyse/queryBusinessAlarmList',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val(),
        	'statType': $("#statType").val()
	};
	return params;
}