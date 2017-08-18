$(function () {

});

function initTableWidth(){
	var width = $(window).width();
//	console.info(width)
	
	$("#echart_div_td").css("width", width/2);
}

var echartDivObj = document.getElementById('echart_div');
var chart ;
var echartDivObj2 = document.getElementById('echart_div2');
var chart2 ;
var echartDivObj3 = document.getElementById('echart_div3');
var chart3 ;
var echartDivObj4 = document.getElementById('echart_div4');
var chart4 ;
var echartDivObj5 = document.getElementById('echart_div5');
var chart5 ;
var echartDivObj6 = document.getElementById('echart_div6');
var chart6 ;
var echartDivObj7 = document.getElementById('echart_div7');
var chart7 ;
var echartDivObj8 = document.getElementById('echart_div8');
var chart8 ;
var echartDivObj9 = document.getElementById('echart_div9');
var chart9 ;
var echartDivObj10 = document.getElementById('echart_div10');
var chart10 ;
var echartDivObj11 = document.getElementById('echart_div11');
var chart11 ;
var echartDivObj12 = document.getElementById('echart_div12');
var chart12 ;
function initEcharts() {
	//折线图
	chart = echarts.init(echartDivObj);
	chart2 = echarts.init(echartDivObj2);
	chart3 = echarts.init(echartDivObj3);
	chart4 = echarts.init(echartDivObj4);
	chart5 = echarts.init(echartDivObj5);
	chart6 = echarts.init(echartDivObj6);
	chart7 = echarts.init(echartDivObj7);
	chart8 = echarts.init(echartDivObj8);
	chart9 = echarts.init(echartDivObj9);
	chart10 = echarts.init(echartDivObj10);
	chart11 = echarts.init(echartDivObj11);
	chart12 = echarts.init(echartDivObj12);
	
	
	chart2.setOption(getOption2());
//	chart5.setOption(getOption5());
	chart6.setOption(getOption6());
//	chart9.setOption(getOption9());
//	chart10.setOption(getOption10());
//	chart11.setOption(getOption11());
//	chart12.setOption(getOption12());
}



function getOption2(){
	var 
	option2 = {
			   title: {
			       text: "项目期限与利率走势",
			       x: "center"
			   },
			   tooltip: {
			       trigger: "item",
			       formatter: "{a} <br/>{b} : {c}"
			   },
			   legend: {
			       x: 'left',
			   },
			   xAxis: [
			       {
			           type: "category",
			           name: "x",
			           splitLine: {show: false},
			           data: ["1月", "3月", "6月", "9月", "12月"]
			       }
			   ],
			   yAxis: [
			       {
			           type: "log",
			           name: "y"
			       }
			   ],
			    toolbox: {
			       show: true,
			       feature: {
			           mark: {
			               show: true
			           },
			         
			           saveAsImage: {
			               show: true
			           }
			       }
			   },
			   calculable: true,
			   series:
				   [
			       {
			           type: "line",
			           color: ['#31859C'],
			           itemStyle: {
			                emphasis: {
			                    shadowBlur: 10,
			                    shadowOffsetX: 0,
			                    shadowColor: 'rgba(0, 0, 0, 0.5)'
			                }
			            },
			            itemStyle: {
			            	normal:{
		                        label: { 
		                            show: true,//是否展示  
		                            formatter: '{c}%' 
		                        }  
			            	}
					   },
			           data: [6.8, 7.8, 8.8, 10.0, 10.5]

			       }
			   ]
			};
			                
	
	return option2;
}
 
//function initTimeCond(){
//    $("#invest_end_time").datetimepicker({
//        format: 'yyyy-mm-dd',
//        minView:'month',
//        language: 'zh-CN',
//        autoclose:true
//    }).on("click",function(){
//    });
//}


function queryEchartData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/list?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	chart.setOption(getOption(msg.page.list));
		    },
		    
	 });
	 
}



function getOption(rows){
//	console.info('++++++++getOption++++++++')
//	console.info(rows)
	var date_list = [];
	var data_list = [];
	var currDate = getCurrDate();
	var day = currDate.substring(8, 10);
	var month = currDate.substring(5, 7);
	month = parseInt(month);
	day = parseInt(day);
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		if(day <= 20){//不要改月数据
			if(month + '月' == row.MONTH){
				continue;
			}
			date_list.push(row.YEAR+row.MONTH);
			data_list.push(row.SUM);
		}else {
			date_list.push(row.YEAR+row.MONTH);
			data_list.push(row.SUM);
		}
		
	}
	var option = {
			 title : {
			        text: '累计交易金额(亿元)',
			        x:'center'
			    },
	
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
	            name:'平台累计交易额（亿元）',
	            color: ['#31859C'],
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
                                fontWeight:'bold',  
                                fontSize : '14',  
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


function queryEchartBiaoData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/biaolilv?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart3.setOption(getOption3(msg.page.list));
		    },
		    
	 });
	 
}




function queryEchartProjectData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/ddylist?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart4.setOption(getOption4(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartBiaoQiXianData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/biaoqixian?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart5.setOption(getOption5(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartJieKuanData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/jiekuan?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart7.setOption(getOption7(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartTouZiData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/touzi?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart8.setOption(getOption8(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartNianLinData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/nianli?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart9.setOption(getOption9(msg.page.list));
		    },
		    
	 });
	 
}

function queryEchartXingBieData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/xingbie?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart10.setOption(getOption10(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartTouZiNianLingData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/touzinianling?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
//		    	console.info(msg.page.list)
		    	chart11.setOption(getOption11(msg.page.list));
		    },
		    
	 });
	 
}
function queryEchartTouZiXingBieData(){
	var paramsUrl = '';
	paramsUrl += 'page=1&limit=200';
	 $.ajax({
		    type: "GET",
		    url: "../yunying/pilu/touzixingbie?" + paramsUrl,
//		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {	
		    	console.info(msg.page.list)
		    	chart12.setOption(getOption12(msg.page.list));
		    },
		    
	 });
	 
}

function getOption4(rows) {

	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	var data_list5 = [];
	var data_list6 = [];
	var yue_total = 0;
	var tian_total=0;
	var benxi_total=0;
	var daoqi_total=0;
	var currDate = getCurrDate();
	var day = currDate.substring(8, 10);
	var month = currDate.substring(5, 7);
	month = parseInt(month);
	day = parseInt(day);
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		if(day <= 20){//不要改月数据
			if(month + '月' == row.MONTH){
				continue;
			}
		data_list.push(row.BENXI);	
		data_list2.push(row.DAOQI);
		data_list3.push(row.DMONTH+row.MONTH);
		data_list4.push(row.MONTH);
		data_list5.push(row.TIAN);
		data_list6.push(row.YUE);
		yue_total += row.YUE;
		tian_total+=row.TIAN;
		benxi_total+=row.BENXI;
		daoqi_total+=row.DAOQI;
	}else{
		data_list.push(row.BENXI);	
		data_list2.push(row.DAOQI);
		data_list3.push(row.DMONTH+row.MONTH);
		data_list4.push(row.MONTH);
		data_list5.push(row.TIAN);
		data_list6.push(row.YUE);
		yue_total += row.YUE;
		tian_total+=row.TIAN;
		benxi_total+=row.BENXI;
		daoqi_total+=row.DAOQI;
		}
	}
	option4 = {
			title : {
		        text: '按还款方式（万元）',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		    	 padding:35,
		        data:['按天付息：'+tian_total, '按月付息：' + yue_total,'到期还本还息：'+daoqi_total,'等额本息：'+benxi_total]
		    },
		    toolbox: {
		        show : true,
		        feature : {
//		            mark : {show: true},
//		            dataView : {show: true, readOnly: false},
//		            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
//		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    xAxis : [
		 
		          {
		            type : 'category',
		            data : data_list3
		        }
		    ],
		    yAxis : [
		       {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'按天付息：'+tian_total,
		            color: ['#31859C'],
			        formatter: "{a} <br/>{b} : {c}",
		            type:'bar',
		            stack: '总量',
//		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:data_list5
		        },
		        {
		            name:'按月付息：' + yue_total,
		            color: ['#B7DEE8'],
		            type:'bar',
		            stack: '总量',
//		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:data_list6
		        },
		        {
		            name:'到期还本还息：'+daoqi_total,
		            color: ['#B9CDE5'],
		            type:'bar',
		            stack: '总量',
//		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:data_list2
		        },
		        {
		            name:'等额本息：'+benxi_total,
		            color: ['#215968'],
		            type:'bar',
		            stack: '总量',
//		            itemStyle : { normal: {label : {show: true, position: 'insideRight'}}},
		            data:data_list
		        }
		       
		    ]
		};
		                    
		  return option4;                  
}


function getOption3(rows) {
//	console.info('++++++++getOption333++++++++')
//	console.info(rows)
	
	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	var data_list5 = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.I1);	
		data_list2.push(row.I2);
		data_list3.push(row.I3);
		data_list4.push(row.I4);
		data_list5.push(row.I5);
	}
	
	option3 = {
		    title : {
		        text: '标的收益率',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
//		            mark : {show: true},
//		            dataView : {show: true, readOnly: false},
//		            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
//		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['5%及以下','5%-7%','7%-9%','9%-11%','11%以上']
		    },
		    series : [
		        {
		            name: '标利率',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0','#61A0A8'] ,
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },		 
		            data:[
		            	
		                {value:data_list, name:'5%及以下'},
		                {value:data_list2, name:'5%-7%'},
		                {value:data_list3, name:'7%-9%'},
		                {value:data_list4, name:'9%-11%'},
		                {value:data_list5, name:'11%以上'}
		            ]
		           
		        
		        }
		    ]
		};
	return option3;
}




function getOption5(rows) {
//	console.info(rows)
	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	var data_list5 = [];
	var data_list6 = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.P1);	
		data_list2.push(row.P2);
		data_list3.push(row.P3);
		data_list4.push(row.P4);
		data_list5.push(row.P5);
		data_list6.push(row.P6);
	}
	
	option5 = {
		    title : {
		        text: '标的期限占比',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    }, 
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },    
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['<=30天','1-3月','3-6月','6-9月','9-12月','12月以上']
		    },
		    series : [
		        {
		            name: '标的期限',
		            type: 'pie',
		            radius : '55%',
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0','#61A0A8','#91C7AE'] ,
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'<=30天'},
		                {value:data_list2, name:'1-3月'},
		                {value:data_list3, name:'3-6月'},
		                {value:data_list4, name:'6-9月'},
		                {value:data_list5, name:'9-12月'},
		                {value:data_list6, name:'12月以上'}
		            ]
		            
		        }
		    ]
		};

	return option5;
}

function getOption6() {
	
	option6 = {
		    title : {
		        text: '产品组合',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    series : [
		        {
		            name: '产品名称',
		            type: 'pie',
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0'] ,
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:[
		                {value:500, name:'小额贷款'},
		                {value:500, name:'汽车金融'},
		                {value:500, name:'信用贷'},
		                {value:500, name:'消费金融'}
		            ],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            }
		        }
		    ]
		};

	return option6;
}

function getOption7(rows) {
	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.B1);	
		data_list2.push(row.B2);
		data_list3.push(row.B3);
		data_list4.push(row.B4);
	}
	
	option7 = {
		    title : {
		        text: '按借款金额',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['3万及以内','3-10万','10-100万','100万以上']
		    },
		    series : [
		        {
		            name: '借款金额',
		            type: 'pie',
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0'] ,
		            radius : '55%',
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'3万及以内'},
		                {value:data_list2, name:'3-10万'},
		                {value:data_list3, name:'10-100万'},
		                {value:data_list4, name:'100万以上'}
		            ]
		        }
		    ]
		};

	return option7;
}

function getOption8(rows) {
	
	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	var data_list5 = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.LV1);	
		data_list2.push(row.LV2);
		data_list3.push(row.LV3);
		data_list4.push(row.LV4);
		data_list5.push(row.LV5);
	}
	
	option8 = {
		    title : {
		        text: '按投资人数',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['一万以内','1-4.9万','5-9.9万','10-99万','100万以上']
		    },
		    series : [
		        {
		            name: '投资金额人数占比',
		            type: 'pie',
		            radius : '55%',
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0','#61A0A8'] ,
		            center: ['50%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'一万以内'},
		                {value:data_list2, name:'1-4.9万'},
		                {value:data_list3, name:'5-9.9万'},
		                {value:data_list4, name:'10-99万'},
		                {value:data_list5, name:'100万以上'}
		            ]
		        }
		    ]
		};

	return option8;
}

function getOption9(rows) {
//	console.info(rows)
	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	var data_list5 = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.Y1);	
		data_list2.push(row.Y2);
		data_list3.push(row.Y3);
		data_list4.push(row.Y4);
		data_list5.push(row.Y5);
	}
	
	option9 = {
		    title : {
		        text: '借款人年龄分布',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['59前','60后','70后','80后','90后']
		    },
		    series : [
		        {
		            name: '年龄',
		            type: 'pie',
		            radius : ['40%', '60%'],
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0','#61A0A8'] ,
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'59前'},
		                {value:data_list2, name:'60后'},
		                {value:data_list3, name:'70后'},
		                {value:data_list4, name:'80后'},
		                {value:data_list5, name:'90后'}
		            ]
		        }
		    ]
		};



	return option9;
}
function getOption10(rows) {
	
	var data_list = [];
	var data_list2 = [];

	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.M);	
		data_list2.push(row.G);

	}
	
	option10 = {
		    title : {
		        text: '借款人性别分布',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['男','女']
		    },
		    series : [
		        {
		            name: '访问来源',
		            type: 'pie',
		            color:['#39869B', '#46A1B9'] ,
		            radius : ['40%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'男'},
		                {value:data_list2, name:'女'}
		            ]
		        }
		    ]
		};



	return option10;
}
function getOption11(rows) {
	
	var data_list = [];
	var data_list2 = [];
	var data_list3 = [];
	var data_list4 = [];
	var data_list5 = [];
	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.Y1);	
		data_list2.push(row.Y2);
		data_list3.push(row.Y3);
		data_list4.push(row.Y4);
		data_list5.push(row.Y5);
	}
	
	option11 = {
		    title : {
		        text: '投资者年龄分布',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['59前','60后','70后','80后','90后']
		    },
		    series : [
		        {
		            name: '年龄',
		            type: 'pie',
		            color:['#39869B', '#46A1B9','#7CBBCF','#B5D4E0','#61A0A8'] ,
		            radius : ['40%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%' 
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'59前'},
		                {value:data_list2, name:'60后'},
		                {value:data_list3, name:'70后'},
		                {value:data_list4, name:'80后'},
		                {value:data_list5, name:'90后'}
		            ]
		        }
		    ]
		};



	return option11;
}
function getOption12(rows) {
	console.info(rows)
	var data_list = [];
	var data_list2 = [];

	for (var i = 0; i < rows.length; i++) {
		var row = rows[i];
		data_list.push(row.BOY);	
		data_list2.push(row.GRIL);

	}
	
	option12 = {
		    title : {
		        text: '投资者性别分布',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		    },   
		    legend: {
		        orient: 'vertical',
		        left: 'left',
		        data: ['男','女']
		    },
		    series : [
		        {
		            name: '访问来源',
		            type: 'pie',
		            color:['#39869B', '#46A1B9'] ,
		            radius : ['40%', '60%'],
		            itemStyle: {
		                emphasis: {
		                    shadowBlur: 10,
		                    shadowOffsetX: 0,
		                    shadowColor: 'rgba(0, 0, 0, 0.5)'
		                }
		            },
		            itemStyle: {
		            	normal:{
	                        label: { 
	                            show: true,//是否展示  
	                            formatter: '{d}%'
	                        }  
		            	}
				   },
		            data:[
		                {value:data_list, name:'男'},
		                {value:data_list2, name:'女'}
		            ]
		        }
		    ]
		};



	return option12;
}





function reload(){
	initEcharts();
	initEvent();
	queryEchartData();
	queryEchartBiaoData();
	initTableWidth();//自适应table宽度
	queryEchartProjectData();
	queryEchartBiaoQiXianData();
	queryEchartJieKuanData();
	queryEchartTouZiData();
	queryEchartXingBieData();
	queryEchartNianLinData();
	queryEchartTouZiNianLingData();
	queryEchartTouZiXingBieData();
}



function getParams(){
	var params = {
        	'invest_end_time': $("#invest_end_time").val(),
        	'invest_month_time': $("#invest_month_time").val()
	};
	return params;
}
