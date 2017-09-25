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
        	{ label: '注册来源', name: 'HUIZONG', frozen : true,index: '$TYPE', width: 100,align:'left'},
			{ label: '注册人数', name: 'ALL_REG', index: '$NUM', width: 100 ,align:'right'}, 			
			{ label: '当月注册', name: 'M_REG', index: '$SUM', width: 100 ,align:'right'}, 
			
			{ label: '当日注册', name: 'D_REG', index: '$BORROW_USER', width: 100 ,align:'right'}, 			
			{ label: '首投人数', name: 'ALL_FIRST', index: '$BORROW_CAPITAL', width: 100 ,align:'right'}, 
			
			{ label: '当月首投人数', name: 'M_FIRST', index: '$BORROW_USER', width: 100,align:'right'}, 			
			{ label: '当日首投人数', name: 'D_FIRST', index: '$BORROW_CAPITAL', width: 100 ,align:'right'} ,
			
			
			{ label: '当月首投金额', name: 'M_FIRST_INV', index: '$TYPE', width: 120,align:'right' },
			{ label: '当日首投金额', name: 'D_FIRST_INV', index: '$NUM', width: 120 ,align:'right'}, 			
			{ label: '当月充值', name: 'WEIZHI4', index: '$SUM', width: 120 ,align:'right'}, 
			
			{ label: '当月提现', name: 'M_WI', index: '$BORROW_USER', width: 120 ,align:'right'}, 			
			{ label: '当月净充值', name: 'WEIZHI3', index: '$BORROW_CAPITAL', width: 120 ,align:'right'}, 
			
			{ label: '当日充值', name: 'WEIZHI2', index: '$BORROW_USER', width: 120,align:'right'}, 			
			{ label: '当日提现', name: 'D_WI', index: '$BORROW_CAPITAL', width: 120 ,align:'right'} ,
			
			
			{ label: '当日净充值', name: 'WEIZHI1', index: '$BORROW_USER', width: 120 ,align:'right'}, 			
			{ label: '当月投资', name: 'M_INV', index: '$BORROW_CAPITAL', width: 120 ,align:'right'}, 
			
			{ label: '当日投资', name: 'D_INV', index: '$BORROW_USER', width: 120,align:'right'}, 			
			{ label: '待收本金', name: 'AWIAT', index: '$BORROW_CAPITAL', width: 120 ,align:'right'} 
		
	
        ],
		viewrecords: true,
        height: $(window).height()-150,
        rowNum: 1000,
        rownumbers: true, 
        autowidth:true,
        sortable:true,
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
    jQuery("#jqGrid").jqGrid('setGridParam',{page:$('#XtoPage').val()});

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
