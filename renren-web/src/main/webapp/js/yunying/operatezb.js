$(function () {
	initEcharts();
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initTimeCond1();

	initCountTableGrid();
	initDaiShouTableGrid()
});

function reload(){
	
	var select = $("#list_select").children('option:selected').val();
	if(select == 'vip_detail'){
//		queryEchartData();
		$("#jqGrid").jqGrid("clearGridData");
		$("#jqGrid").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/zb2/list',
            postData: getParams()
        }).trigger("reloadGrid");
	}else if(select == 'vip_count'){
//		queryEchartData2();
		$("#jqGrid_count").jqGrid("clearGridData");
		$("#jqGrid_count").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/zb2/ddylist',
            postData: getParams()
        }).trigger("reloadGrid");
	}else if(select == 'daishou'){
		$("#jqGrid_daishou").jqGrid("clearGridData");
		$("#jqGrid_daishou").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/zb2/ddylist2',
            postData: getParams()
        }).trigger("reloadGrid");
		
	}else if(select == 'zichan'){
		$("#jqGrid_zichan").jqGrid("clearGridData");
		$("#jqGrid_zichan").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/zbp2p/zichanlist',
            postData: getParams()
        }).trigger("reloadGrid");
		
	}
}


var echartDivObj = document.getElementById('echart_div');
var chart ;
var echartDivObj2 = document.getElementById('echart_div2');
var chart2 ;
var echartDivObj3 = document.getElementById('echart_div3');
var chart3 ;
function initEcharts() {
	//折线图
	chart = echarts.init(echartDivObj);
	chart2 = echarts.init(echartDivObj2);
	chart3 = echarts.init(echartDivObj3);
}


//function queryEchartData(){
//	var paramsUrl = '';
//	var begin_time = $("#begin_time").val();
//	var end_time = $("#end_time").val();
//	paramsUrl += 'page=1&limit=200&begin_time=' + begin_time + '&end_time=' + end_time;
//	 $.ajax({
//		    type: "GET",
//		    url: "../yunying/zb2/list?" + paramsUrl,
////		    data: JSON.stringify(getParams()),
//		    contentType: "application/json;charset=utf-8",
//		    success : function(msg) {
//		    	loaded();
//		    	chart.setOption(getOption(msg.page.list));
//		    },
//		    
//	 });
//	 
//}
function queryEchartData2(){
	var paramsUrl = '';
	var begin_time = $("#begin_time").val();
	var end_time = $("#end_time").val();
	paramsUrl += 'page=1&limit=200&begin_time=' + begin_time + '&end_time=' + end_time;
	 $.ajax({
		    type: "GET",
		    url: "../yunying/zb2/ddylist?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	chart2.setOption(getOption2(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartData3(){
	var paramsUrl = '';
	var begin_time = $("#begin_time").val();
	var end_time = $("#end_time").val();
	paramsUrl += 'page=1&limit=200&begin_time=' + begin_time + '&end_time=' + end_time;
	 $.ajax({
		    type: "GET",
		    url: "../yunying/zb2/ddylist2?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	chart3.setOption(getOption3(msg.page.list));
		    },
		    
	 });
	 
}


function getOption(rows){
//	console.info('++++++++getOption++++++++')
//	console.info(rows)
	var date_list = [];
	var data_list = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		date_list.push(row.TIME);
		data_list.push(row.MONEY);
	}
	var option = {
			 title : {
			        text: '理财计划解金额(万元)',
			        x:'center',
//			        	  fontWeight: 'normal'
			    },
	            textStyle: {  
	                fontWeight: 'normal',              //标题颜色  
	                color: '#000000' 
//	                fontSize : '20'
	            },  
	
	    color: ['#34B5C8'],
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
	    toolbox: {
	        show : true,
	        feature : {
	            saveAsImage : {show: true}
	        }
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : date_list,
//	            axisLabel: {   
////	            	  show: true,
//                      textStyle: {
//                          color: '#fff'
//                      }
//	            }

//	            axisTick: {
//	                alignWithLabel: true
//	            }
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	          
	        }
	    ],
	    series : [
	        {
	            name:'理财计划解金额',
//	            color: ['#34B5C8'],
	            type:'bar',
	            itemStyle: {
	            	normal:{
//	            		color:function(params) {
//                            return '#a6faff';
//                        },
                        label: {  
                        	
                
                            show: true,//是否展示  
                            offset: [0,-2],
                            position: 'top',
                            textStyle: {  
//                                fontWeight:'bold',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑'
                            }  
                        }  
	            	}
	            },
	            barWidth: '60%',
	            data: data_list
	        }
	    ]
	};
	return option;

}

function getOption2(rows){
//	console.info('++++++++getOption++++++++')
//	console.info(rows)
	var date_list = [];
	var data_list = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		date_list.push(row.TIME);
		data_list.push(row.MONEY);
	}
	var option2 = {
			 title : {
			        text: '每天申请退出的金额(万元)',
			        x:'center'
			    },
			      textStyle: {  
		                fontWeight: 'normal',              //标题颜色  
		                color: '#000000' 
//		                fontSize : '20'
		            },  
	
	    color: ['#34B5C8'],
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
	    toolbox: {
	        show : true,
	        feature : {
	            saveAsImage : {show: true}
	        }
	    },
	    xAxis : [
	        {
	            type : 'category',
//	            axisLabel: {   (X坐标打斜)
//	            	interval:0,  
//	            	rotate:20  
//	            },
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
	            name:'每天申请退出的金额',
	            color: ['#34B5C8'],
	            type:'bar',
	            itemStyle: {
	            	normal:{
//	            		color:function(params) {
//                            return '#a6faff';
//                        },
                        label: {  
                            show: true,//是否展示  
                            offset: [0,-2],
                            position: 'top',
                            textStyle: {  
//                                fontWeight:'bold',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑'
                            }  
                        }  
	            	}
	            },
	            barWidth: '60%',
	            data: data_list
	        }
	    ]
	};
	return option2;

}

function getOption3(rows){
//	console.info('++++++++getOption++++++++')
//	console.info(rows)
	var date_list = [];
	var data_list = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		date_list.push(row.TIME);
		data_list.push(row.MONEY);
	}
	var option3 = {
			 title : {
			        text: '每天理财计划底层资产还款金额(万元)',
			        x:'center'
			    },
			      textStyle: {  
		                fontWeight: 'normal',              //标题颜色  
		                color: '#000000' 
//		                fontSize : '20'
		            },  
	
	    color: ['#34B5C8'],
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
	    toolbox: {
	        show : true,
	        feature : {
	            saveAsImage : {show: true}
	        }
	    },
	    xAxis : [
	        {
	            type : 'category',
//	            axisLabel: {   (X坐标打斜)
//	            	interval:0,  
//	            	rotate:20  
//	            },
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
	            name:'每天理财计划底层资产还款金额',
	            color: ['#34B5C8'],
	            type:'bar',
	            itemStyle: {
	            	normal:{
//	            		color:function(params) {
//                            return '#a6faff';
//                        },
                        label: {  
                            show: true,//是否展示  
                            offset: [0,-2],
                            position: 'top',
                            textStyle: {  
//                                fontWeight:'bold',  
                                fontSize : '12',  
                                fontFamily : '微软雅黑'
                            }  
                        }  
	            	}
	            },
	            barWidth: '60%',
	            data: data_list
	        }
	    ]
	};
	return option3;

}

function initDaiShouTableGrid(){
    $("#jqGrid_daishou").jqGrid({
        datatype: "json",
        colModel: [			
        	{ label: '日期', name: 'TIME', index: '$STAT_PERIOD', width: 100,align:'right' },
			{ label: '还款金额', name: 'MONEY', index: '$MONTH_TENDER_COU', width: 100 ,align:'right'}
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
//        rownumbers: true, 
        autowidth:true,
        pager: "#jqGridPager_daishou",
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
        },loadComplete: function(){
        	
			queryEchartData3();
        }
    });
    $("#vip_daishou_div").hide();
}




function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
			$("#vip_daishou_div").hide();
			$("#zichan_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			$("#vip_daishou_div").hide();
			$("#zichan_div").hide();
		}else if(select == 'daishou'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#vip_daishou_div").show();
			$("#zichan_div").hide();
		}else if(select == 'zichan'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#vip_daishou_div").hide();
			$("#zichan_div").show();
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
			executePost('../yunying/zb2/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/zb2/exportExcel', {'params' : JSON.stringify(params)});
		}else if(select == 'daishou'){
			executePost('../yunying/zb2/exportExcel', {'params' : JSON.stringify(params)});
		}
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '日期', name: 'TIME', index: '$TYPE', width: 90,align:'right' },
			{ label: '解锁金额', name: 'MONEY', index: '$NUM', width: 90 ,align:'right'} 			
		
					
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
        },  loadComplete: function(){
			var rows = $("#jqGrid").jqGrid("getRowData");
			var option = getOption(rows);
			console.info(option)
			chart.setOption(option);
        }
    });
}

function initCountTableGrid(){
    $("#jqGrid_count").jqGrid({
        datatype: "json",
        colModel: [			
        	{ label: '日期', name: 'TIME', index: '$TYPE', width: 90,align:'right' },
			{ label: '退出金额', name: 'MONEY', index: '$NUM', width: 90 ,align:'right'} 		
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
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
        },
        	loadComplete: function(){
        	
			queryEchartData2();
        }
    });
    $("#vip_count_div").hide();
}



function getParams(){
	var params = {
        	'begin_time': $("#begin_time").val(),
        	'end_time': $("#end_time").val()
	};
	return params;
}
