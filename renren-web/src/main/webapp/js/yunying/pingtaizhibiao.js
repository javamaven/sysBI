$(function () {
	initDetailTableGrid();
	initExportFunction();
	initEvent();
//	initSelectEvent();
	initTimeCond();
	initNianLinTableGrid();
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
    $("#statPeriod").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#statPeriod").val(addDate(getCurrDate(), -1));
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
			executePost('../yunying/pingtai/exportExcel', {'params' : JSON.stringify(params)});
		
		
	});

}
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '日期', name: 'STAT_PERIOD', index: 'STAT_PERIOD', width: 90,align:'right' },
			{ label: '指标名称', name: 'DATE_NAME', index: 'DATE_NAME', width: 90 ,align:'right'}, 			
		
			{ label: '指标值', name: 'DATA_NUM', index: '$NUMS', width: 90,align:'right' }
        ],
		viewrecords: true,
        height: $(window).height()-150,
        rowNum: 20,
//        rownumbers: true, 
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

function initNianLinTableGrid(){
    $("#jqGrid_count").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '年龄区间', name: 'G_OLDS', index: '$TYPE', width: 90,align:'right' },
			{ label: '人数', name: 'NUM', index: '$NUM', width: 90 ,align:'right'}, 			
			{ label: '人数占比', name: 'NUM_RATE', index: '$SUM', width: 90 ,align:'right',
				formatter: function(value, options, row){
					return formatNumber(value*100,2) + '%';
				}	
			}, 
			
			
			{ label: '待收本金', name: 'AWAIT', index: '$BORROW_USER', width: 130 ,align:'right'}, 			
			{ label: '待收占比', name: 'AWAIT_RATE', index: '$BORROW_CAPITAL', width: 90 ,align:'right', 
				formatter: function(value, options, row){
					return formatNumber(value*100,2) + '%';
				}	
			}, 
			{ label: '人均待收本金', name: 'AVG_AWAIT', index: '$NUMM', width: 130,align:'right' },
			{ label: '男性人数', name: 'MAN', index: '$SUMM', width: 90 ,align:'right'},		
			{ label: '女性人数', name: 'WOMAN', index: '$AVGG', width: 90 ,align:'right'},
			{ label: '性别未知人数', name: 'WEIZHI', index: '$NUMS', width: 130,align:'right'},
			{ label: '男性人均待收', name: 'AVG_MAN_AWAIT', index: '$NUMS', width: 130,align:'right'},
			{ label: '女性人均待收', name: 'AVG_WOMAN_AWAIT', index: '$NUMS', width: 130,align:'right'},
			{ label: '性别未知人均待收', name: 'AVG_WEIZHI_AWAIT', index: '$NUMS', width: 150,align:'right'}
        ],
		viewrecords: true,
        height: $(window).height()-150,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
//        shrinkToFit: false,
//        autoScroll: false,
//        multiselect: false,
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
//					url: '../yunying/pingtai/list',
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
					url: '../yunying/pingtai/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		
	}
});

function getParams(){
	var params = {
			'statPeriod': $("#statPeriod").val()
	};
	return params;
}
