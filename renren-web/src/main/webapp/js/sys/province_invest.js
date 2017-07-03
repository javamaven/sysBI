$(function() {
	initProvinceInvestEcharts();
	setInterval('queryProvinceInvestData()', 30*60*1000);
});

var ProvinceInvest_Obj = document.getElementById('province_invest_chart_div');
var ProvinceInvest_Chart ;
function initProvinceInvestEcharts() {
	//折线图
	ProvinceInvest_Chart = echarts.init(ProvinceInvest_Obj);
	// 使用刚指定的配置项和数据显示图表。
	queryProvinceInvestInitData();
}
function queryProvinceInvestData(){
	$.ajax({
		type: "POST",
		url: '../main/queryProvinceInvestInvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			var ProvinceInvestList = data.data_list;
			ProvinceInvest_Chart.setOption(getProvinceInvestOption(ProvinceInvestList));
		}
	});
}
function queryProvinceInvestInitData(){
	$.ajax({
		type: "POST",
		url: '../main/queryProvinceInvestInvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			var data_list = data.data_list;
//			for (var i = 0; i < data.time_list.length; i++) {
//				var t = data.time_list[i];
//				time_list.push(t.substring(11, t.length));
//			}
			ProvinceInvest_Chart.setOption(getProvinceInvestOption(data_list, data.province_list));
		}
	});
}

function getProvinceInvestOption(data_list, province_list){
var option = {
	    title: {
//	        text: 'ProvinceInvest 个人累计投资额(万元)',
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
//	        data: ['ProvinceInvest','top9','top8','top7','top6','top5','top4','top3','top2','top1']
	        data: province_list
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
                            return colorList[12]
                        },
                        label: {  
                            show: true,//是否展示  
                            offset: [0,-2],
                            position: 'right',
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
