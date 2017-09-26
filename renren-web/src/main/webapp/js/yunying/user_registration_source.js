$(function () {
	initDetailTableGrid();
	initExportFunction();
	initEvent();
	initTimeCond();
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
function initTimeCond(){
    $("#period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#period").val(addDate(getCurrDate(), -1));
}



function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
			executePost('../yunying/source/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '注册来源', name: 'HUIZONG', frozen : true,index: 'HUIZONG', width: 100,align:'left'},
			{ label: '注册人数', name: 'ALL_REG', index: 'ALL_REG', width: 100 ,align:'right'}, 			
			{ label: '当月注册', name: 'M_REG', index: 'M_REG', width: 100 ,align:'right'}, 
			
			{ label: '当日注册', name: 'D_REG', index: 'D_REG', width: 100 ,align:'right'}, 			
			{ label: '首投人数', name: 'ALL_FIRST', index: 'ALL_FIRST', width: 100 ,align:'right'}, 
			
			{ label: '当月首投人数', name: 'M_FIRST', index: 'M_FIRST', width: 100,align:'right'}, 			
			{ label: '当日首投人数', name: 'D_FIRST', index: 'D_FIRST', width: 100 ,align:'right'} ,
			
			
			{ label: '当月首投金额', name: 'M_FIRST_INV', index: 'M_FIRST_INV', width: 120,align:'right' },
			{ label: '当日首投金额', name: 'D_FIRST_INV', index: 'D_FIRST_INV', width: 120 ,align:'right'}, 			
			{ label: '当月充值', name: 'WEIZHI4', index: 'WEIZHI4', width: 120 ,align:'right'}, 
			
			{ label: '当月净充值', name: 'WEIZHI3', index: 'WEIZHI3', width: 120 ,align:'right'}, 
			
			{ label: '当日充值', name: 'WEIZHI2', index: 'WEIZHI2', width: 120,align:'right'}, 			
			
			
			{ label: '当日净充值', name: 'WEIZHI1', index: 'WEIZHI1', width: 120 ,align:'right'}, 			
			{ label: '当月投资', name: 'M_INV', index: 'M_INV', width: 120 ,align:'right'}, 
			
			{ label: '当日投资', name: 'D_INV', index: 'D_INV', width: 120,align:'right'}, 			
			{ label: '待收本金', name: 'AWIAT', index: 'AWIAT', width: 120 ,align:'right'} 
		
	
        ],
		viewrecords: true,
        height: $(window).height()-150,
        rowNum: 1000,
        rownumbers: true, 
        autowidth:true,
//        sortable:true,
//        sortname:'sortorder',
//        sortorder:'asc',
        shrinkToFit: false,
        showSummaryOnHide: true,
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
            order: "order",
            sort:"sortorder"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        }
    });
    jQuery("#jqGrid").jqGrid('setFrozenColumns');  //冻结列

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
					url: '../yunying/source/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		
	}
});

function getParams(){
	var params = {
			'period': $("#period").val()
	};
	return params;
}
