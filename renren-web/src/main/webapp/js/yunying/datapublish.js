$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initTimeCond1();
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
			executePost('../yunying/pilu/exportExcel', {'params' : JSON.stringify(params)});
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
			{ label: '年份', name: 'YEAR', index: '$YEAR', width: 90,align:'right' },
			{ label: '月份', name: 'MONTH', index: '$MONTH', width: 90 ,align:'right'}, 			
			{ label: '平台累计交易额 ', name: 'SUM', index: '$SUM', width: 90 ,align:'right'}	
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 200,
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
			{ label: '年份', name: 'DMONTH', index: '$HONGBAO', width: 100,align:'right' },
			{ label: '月份', name: 'MONTH', index: '$YUNYING', width: 80 ,align:'right'}, 	
			{ label: '按天付息', name: 'TIAN', index: '$CUNGUAN', width: 100,align:'right' }, 
			{ label: '按月付息', name: 'YUE', index: '$CUNGUAN', width: 100,align:'right' }, 			
			{ label: '到期还本还息', name: 'DAOQI', index: '$TIME', width: 100,align:'right' }, 			
			{ label: '等额本息', name: 'BENXI', index: '$YUNYINGFEIYONG', width: 100 ,align:'right'}
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 200,
//        rownumbers: true, 
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
					url: '../yunying/pilu/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/pilu/ddylist',
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
