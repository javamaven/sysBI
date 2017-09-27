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
			executePost('../yunying/assets/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '资产期限', name: 'PERIOD', frozen : true,index: 'PERIOD', width: 80,align:'left'},
        	
        	{ label: '当日资产供应', name: 'D_PUB', index: 'D_PUB', width: 100 ,align:'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
        	
			{ label: '当月资产供应', name: 'M_PUB', index: 'M_PUB', width: 100 ,align:'right',
				formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
			
			{ label: '期限占比', name: 'SUPPLY_RATE', index: 'SUPPLY_RATE', width: 80 ,align:'right',
				formatter: function(value, options, row){
					 if(value == null || value == ''){
						return '';
					}else{
						return formatNumber(value*100,2) + '%';
					}
				} 
			},
			
			
			
		
			{ label: '供应理财计划资产', name: 'M_PUB_FIN', index: 'M_PUB_FIN', width: 130 ,align:'right',
				formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
			
			
			{ label: '期限占比', name: 'SUPPLY_FIN_RATE', index: 'SUPPLY_FIN_RATE', width: 80,align:'right',
				formatter: function(value, options, row){
					 if(value == null || value == ''){
						return '';
					}else{
						return formatNumber(value*100,2) + '%';
					}
				} 
			},
			
			{ label: '当日成交', name: 'D_INV', index: 'D_INV', width: 90 ,align:'right',
				formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
			
			
			{ label: '当月成交', name: 'M_INV', index: 'M_INV', width: 90 ,align:'right',
				formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
			
			
			
			
			{ label: '期限占比', name: 'INV_RATE', index: 'INV_RATE', width: 80,align:'right',
				formatter: function(value, options, row){
					 if(value == null || value == ''){
						return '';
					}else{
						return formatNumber(value*100,2) + '%';
					}
				} 
			},
			
		
			
			{ label: '当日散标成交', name: 'D_SAN', index: 'D_SAN', width: 100 ,align:'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
			
			{ label: '当月散标成交', name: 'M_SAN', index: 'M_SAN', width: 100 ,align:'right' ,
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    },	    
			{ label: '当日理财计划成交', name: 'D_FIN', index: 'D_FIN', width: 130 ,align:'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
			},
			{ label: '当月理财计划成交', name: 'M_FIN', index: 'M_FIN', width: 130,align:'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return '';
		    		}
		    	} 
		    }

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
//    jQuery("#jqGrid").jqGrid('setFrozenColumns');  //冻结列

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
					url: '../yunying/assets/list',
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
