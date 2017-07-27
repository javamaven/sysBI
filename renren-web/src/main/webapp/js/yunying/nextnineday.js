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
    $("#end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}
function initTimeCond1(){
    $("#begin_time").datetimepicker({
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
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../yunying/nine/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/nine/exportExcel2', {'params' : JSON.stringify(params)});
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
			{ label: '理财计划解锁金额', name: 'UNLOCK_MONEY', index: '$UNLOCK_MONEY', width: 90 ,align:'right'}	
//			{ label: '累计解锁未退出金额', name: 'LJ_UNLOCK_MONEY', index: '$LJ_UNLOCK_MONEY', width: 90,align:'right' }

				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 300,
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
			var rows = $("#jqGrid").jqGrid("getRowData");
			var option = getOption(rows);
			console.info(option)
			chart.setOption(option);
        }
    });
}

//function queryEchartData(){
//	var paramsUrl = '';
//	var begin_time = $("#begin_time").val();
//	var end_time = $("#end_time").val();
//	paramsUrl += 'page=1&limit=300&begin_time=' + begin_time + '&end_time=' + end_time;
//	 $.ajax({
//		    type: "GET",
//		    url: "../yunying/nine/list?" + paramsUrl,
////		    data: JSON.stringify(getParams()),
//		    contentType: "application/json;charset=utf-8",
//		    success : function(msg) {
//		    	console.info(msg)
//		    	chart.setOption(getOption(msg.page.list));
//		    }
//	 });
//}

function getOption(rows){
	console.info('++++++++getOption++++++++')
	console.info(rows)
	var date_list = [];
	var data_list = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		date_list.push(row.TIME);
		data_list.push(row.UNLOCK_MONEY);
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
	            name:'理财计划解锁金额（万元）',
	            type:'bar',
	            barWidth: '60%',
	            data: data_list
	        }
	    ]
	};
	return option;

}




function reload(){
//	vm.showList = true;
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
			url: '../yunying/nine/ddylist',
            postData: getParams()
        }).trigger("reloadGrid");
		
	}
}

function getParams(){
	var params = {
        	'begin_time': $("#begin_time").val(),
        	'end_time': $("#end_time").val()
	};
	return params;
}
