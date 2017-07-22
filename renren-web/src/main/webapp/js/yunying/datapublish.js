$(function () {
	initEcharts();
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initTimeCond1();
	initCountTableGrid();
});

var echartDivObj = document.getElementById('echart_div');
var chart ;
function initEcharts() {
	//折线图
	chart = echarts.init(echartDivObj);
}

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
			executePost('../yunying/pilu/exportExcel2', {'params' : JSON.stringify(params)});
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
			{ label: '平台累计交易额（亿元） ', name: 'SUM', index: '$SUM', width: 90 ,align:'right'}	
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
        },
        loadComplete: function(){
//			var rows = $("#jqGrid_count").jqGrid("getRowData");
			queryEchartData();
        }
    });
}

function queryEchartData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/list?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	console.info(msg.page.list)
		    	chart.setOption(getOption(msg.page.list));
		    }
	 });
}

function getOption(rows){
	console.info('++++++++getOption++++++++')
	console.info(rows)
	var date_list = [];
	var data_list = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		date_list.push(row.YEAR+row.MONTH);
		data_list.push(row.SUM);
	}
	var option = {
	    color: ['#3398DB'],
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : date_list,
	            axisTick: {
	                alignWithLabel: true
	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : [
	        {
	            name:'平台累计交易额（亿元）',
	            type:'bar',
	            barWidth: '60%',
	            data: data_list
	        }
	    ]
	};
	return option;

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
//					url: '../yunying/pilu/list',
//		            postData: getParams()
//	            }).trigger("reloadGrid");
//			}else if(select == 'vip_count'){
//				$("#jqGrid_count").jqGrid("clearGridData");
//				$("#jqGrid_count").jqGrid('setGridParam',{ 
//					datatype:'json', 
//					url: '../yunying/pilu/ddylist',
//		            postData: getParams()
//	            }).trigger("reloadGrid");
//			}
//		}
//	}
//});

function reload(){
//	vm.showList = true;
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



function getParams(){
	var params = {
        	'invest_end_time': $("#invest_end_time").val(),
        	'invest_month_time': $("#invest_month_time").val()
	};
	return params;
}
