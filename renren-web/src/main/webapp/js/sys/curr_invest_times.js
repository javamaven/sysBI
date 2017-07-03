$(function() {
	initCurrInvestTimesEcharts();
	setInterval('queryCurrInvestTimesData()', 5*60*1000);
});

var CurrInvestTimes_Obj = document.getElementById('curr_invest_times_chart_div');
var CurrInvestTimes_Chart ;
function initCurrInvestTimesEcharts() {
	//折线图
	CurrInvestTimes_Chart = echarts.init(CurrInvestTimes_Obj);
	// 使用刚指定的配置项和数据显示图表。
	queryCurrInvestTimesInitData();
}
function queryCurrInvestTimesData(){
	$.ajax({
		type: "POST",
		url: '../main/queryCurrInvestTimes',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			CurrInvestTimes_Chart.setOption(getCurrInvestTimesOption(data));
		}
	});
}
function queryCurrInvestTimesInitData(){
	$.ajax({
		type: "POST",
		url: '../main/queryCurrInvestTimes',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			CurrInvestTimes_Chart.setOption(getCurrInvestTimesOption(data));
		}
	});
}
var labelTop_times = {
	    normal : {
	        label : {
	            show : true,
	            position : 'center',
	            formatter : '{b}',
	            textStyle: {
	            	color: 'gray'
//	                baseline : 'bottom'
	            }
	        },
	        labelLine : {
	            show : false
	        }
	    }
	};
	var labelFromatter_times = {
	    normal : {
	        label : {
	            formatter : function (params){
	            	return params.value;
//	            	if(params.value > 100000000){
//	            		return   formatNumber( params.value/100000000, 2) + "亿元"
//	            	}else{
//	            		return   formatNumber( params.value/10000, 2) + "万元"
//	            	}
	            },
	            textStyle: {  
                    fontWeight:'bold',  
                    fontSize : '14', 
                    fontFamily : '微软雅黑',  
                }  
	        }
	    },
	}
	var labelBottom = {
	    normal : {
	        color: '#ccc',
	        label : {
	            show : true,
	            position : 'center'
	        },
	        labelLine : {
	            show : false
	        }
	    },
	    emphasis: {
	        color: 'rgba(0,0,0,0)'
	    }
	};
var radius = [50, 70];
function getCurrInvestTimesOption(data){
	var option = {
		    title : {
//		        text: 'The App World'
		     
		    },
		    series : [
		        {
		            type : 'pie',
		            center : ['10%', '30%'],
		            radius : radius,
		            x: '0%', // for funnel
		            itemStyle : labelFromatter_times,
		            data : [
		            	{name:'other', value: data.month, itemStyle : labelBottom},
		            	{name:'当月交易笔数', value: 2000000000-data.month,itemStyle : labelTop_times}
		            ]
		        }
		        ,
		        {
		            type : 'pie',
		            center : ['30%', '30%'],
		            radius : radius,
		            x:'20%', // for funnel
		            itemStyle : labelFromatter_times,
		            data : [
		                {name:'other', value:data.day, itemStyle : labelBottom},
		                {name:'当天交易笔数', value:45000000 - data.day,itemStyle : labelTop_times}
		            ]
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
