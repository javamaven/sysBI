$(function () {
	initDetailTableGrid();
	initExportFunction();
	initEvent();
});




function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
			executePost('../yunying/daishouqujian/exportExcel', {'params' : JSON.stringify(params)});
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '待收本金区间', name: 'G_AWAIT', index: '$TYPE', width: 90,align:'right' },
			{ label: '人数', name: 'NUM', index: '$NUM', width: 90 ,align:'right'}, 			
			{ label: '人数占比', name: 'NUM_RATE', index: '$SUM', width: 90 ,align:'right',
				formatter: function(value, options, row){
						return formatNumber(value*100,2) + '%';
					}	
			}, 
			
			{ label: '待收本金', name: 'AWAIT', index: '$BORROW_USER', width: 90 ,align:'right'}, 			
			{ label: '待收占比', name: 'AWAIT_RATE', index: '$BORROW_CAPITAL', width: 90 ,align:'right',
				formatter: function(value, options, row){
					return formatNumber(value*100,2) + '%';
					}
				}, 
			
			{ label: '人均待收', name: 'AVG_AWAIT', index: '$NUMM', width: 90,align:'right' },
			{ label: '加权期限', name: 'AVG_PERIOD', index: '$SUMM', width: 90 ,align:'right'},		
			{ label: '加权利率', name: 'AVG_APR', index: '$AVGG', width: 90 ,align:'right',	
				
				formatter: function(value, options, row){
			
				return formatNumber(value*100,2) + '%';
				   }
			},
			{ label: '当日利息', name: 'INTEREST', index: '$NUMS', width: 90,align:'right' }
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
				$("#jqGrid").jqGrid("clearGridData");
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/daishouqujian/list',
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
