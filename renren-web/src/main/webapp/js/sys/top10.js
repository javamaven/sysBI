$(function() {
	initTop10Echarts();
	setInterval('queryTop10Data()', 30*60*1000);
});

var top10_Obj = document.getElementById('top10_chart_div');
var top10_Chart ;
function initTop10Echarts() {
	//折线图
	top10_Chart = echarts.init(top10_Obj);
	// 使用刚指定的配置项和数据显示图表。
	queryTop10InitData();
}
function queryTop10Data(){
	$.ajax({
		type: "POST",
		url: '../main/queryTop10InvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			var top10List = data.data_list;
			top10_Chart.setOption(getTop10Option(top10List));
		}
	});
}
function queryTop10InitData(){
	$.ajax({
		type: "POST",
		url: '../main/queryTop10InvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			var data_list = data.data_list;
//			for (var i = 0; i < data.time_list.length; i++) {
//				var t = data.time_list[i];
//				time_list.push(t.substring(11, t.length));
//			}
			top10_Chart.setOption(getTop10Option(data_list));
		}
	});
}

function getTop10Option(data_list){
var option = {
	    title: {
//	        text: 'TOP10 个人累计投资额(万元)',
//	        
//        	textStyle: {
//                color: '#fff'
//            }
//	        	,
//	        subtext: '数据来自网络'
	    },
	    color: ['#3398DB'],
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
//	            type: 'shadow'
	        }
	    },
//	    legend: {
//	        data: ['2011年']
//	    },
	    grid: {
//	        left: '3%',
//	        right: '4%',
	        bottom: '0%'
	        	
//	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        splitLine:{
	        	show:false
	        },
        	show:false,
            axisLabel : {
                formatter: '{value}',
                textStyle: {
                    color: '#fff'
                }
            }
//	        ,
//	        boundaryGap: [0, 0.05]
	    },
	    yAxis: {
	        type: 'category',
	        splitLine:{
	        	show:false
	        },
	        show:true,
            axisLabel : {
                formatter: '{value}',
                textStyle: {
                    color: '#fff'
                }
            },
//	        data: ['top10','top9','top8','top7','top6','top5','top4','top3','top2','top1']
	        data: []
	    },
	    series: [
	        {
	            name: '累计投资额',
	            type: 'bar',
	            itemStyle: {
	            	normal:{
	            		color:function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
                               '#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
                               '#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
                            ];
                            return colorList[params.dataIndex]
                        },
                        label: {  
                            show: true,//是否展示  
                            offset: [0,1],
//                            position: 'right',
                            // position: 默认自适应，水平布局为'top'，垂直布局为'right'，可选为
                            //           'inside'|'left'|'right'|'top'|'bottom'
                            // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
                            textStyle: {  
                                fontWeight:'bold',  
                                fontSize : '14',  
                                fontFamily : '微软雅黑'
                            }  
                        }  
	            	}
	            },
	            barWidth:22,
	            data: data_list
//	            data: [18203, 23489, 29034, 104970, 131744, 630230]
	        }
	    ]
	};
	return option;
}

var dataStyle = { 
	    normal: {
	        label : {
	            show: true,
	            position: 'insideLeft',
	            formatter: '{c}%'
	        }
	    }
	};
