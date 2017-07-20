$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initTimeCond1();
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
    $("#invest_end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}
function initTimeCond1(){
    $("#invest_month_time").datetimepicker({
        format: 'yyyy-mm',
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
			executePost('../yunying/yxp2p/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/p2p/exportExcel2', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '回款日期', name: 'TIME', index: '$TIME', width: 90,align:'right' },
			{ label: '普通版回款', name: 'PTB_REPAY_ACCOUNT_WAIT', index: '$PTB_REPAY_ACCOUNT_WAIT', width: 90 ,align:'right'}, 			
			{ label: '存管版回款', name: 'CGB_REPAY_ACCOUNT_WAIT', index: '$CGB_REPAY_ACCOUNT_WAIT', width: 90 ,align:'right'}, 
			{ label: '总回款', name: 'REPAY_ACCOUNT_WAIT', index: '$REPAY_ACCOUNT_WAIT', width: 90 ,align:'right'}, 			
			{ label: '理财计划解锁金额', name: 'UNLOCK_MONEY', index: '$UNLOCK_MONEY', width: 90 ,align:'right'}, 	
			{ label: '累计解锁未退出金额', name: 'LJ_UNLOCK_MONEY', index: '$LJ_UNLOCK_MONEY', width: 90,align:'right' }

				
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
					url: '../yunying/nine/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/p2p/ddylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'invest_end_time': $("#invest_end_time").val(),
        	'invest_month_time': $("#invest_month_time").val()
	};
	return params;
}
