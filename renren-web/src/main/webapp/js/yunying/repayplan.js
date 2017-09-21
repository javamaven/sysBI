$(function () {
	initDetailTableGrid();
	initExportFunction();
	initEvent();
//	initSelectEvent();
});


//function initSelectEvent(){
//	//日报，月报切换
//	$("#list_select").change(function(){
//		var select = $(this).children('option:selected').val();
//		if(select == 'vip_detail'){
//			$("#vip_detail_div").show();
//			$("#vip_count_div").hide();
//		}else if(select == 'nianlin'){
//			$("#vip_detail_div").hide();
//			$("#vip_count_div").show();
//		}
//	});
//}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
			executePost('../yunying/repayplan/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '日期', name: 'DATES', index: '$TYPE', width: 150,align:'left' },
			{ label: '散标总回款', name: 'ALL_SANBIAO', index: '$NUM', width: 150 ,align:'right'}, 			
			{ label: '存管散标回款', name: 'CG_SANBIAO', index: '$SUM', width: 150 ,align:'right'}, 
			
			{ label: '旧版散标回款', name: 'OLD_SANBIAO', index: '$BORROW_USER', width: 150 ,align:'right'}, 			
			{ label: '理财计划底层回款', name: 'FINANCIAL', index: '$BORROW_CAPITAL', width: 150 ,align:'right'}, 
			
			{ label: '解锁金额', name: 'UNLOCKS', index: '$BORROW_USER', width: 150,align:'right'}, 			
			{ label: '理财计划预测退出', name: 'FORECAST', index: '$BORROW_CAPITAL', width: 150 ,align:'right'} 
			
		
	
        ],
		viewrecords: true,
        height: $(window).height()-150,
        rowNum: 1000,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
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




//var vm = new Vue({
//	el:'#rrapp',
//	data:{
//		showList: true,
//		title: null,
//		dmReportDdzRemain: {}
//	},
//	methods: {
//		reload: function (event) {
//			vm.showList = true;
//			var select = $("#list_select").children('option:selected').val();
//			if(select == 'vip_detail'){
//				$("#jqGrid").jqGrid("clearGridData");
//				$("#jqGrid").jqGrid('setGridParam',{ 
//					datatype:'json', 
//					url: '../yunying/daishouqujian/list',
//		            postData: getParams()
//	            }).trigger("reloadGrid");
//			}else if(select == 'nianlin'){
//				$("#jqGrid_count").jqGrid("clearGridData");
//				$("#jqGrid_count").jqGrid('setGridParam',{ 
//					datatype:'json', 
//					url: '../yunying/daishouqujian/ddylist',
//		            postData: getParams()
//	            }).trigger("reloadGrid");
//			}
//		}
//	}
//});


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
				$("#jqGrid").jqGrid("clearGridData");
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/repayplan/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		
	}
});

function getParams(){
	var params = {
        	
	};
	return params;
}
