$(function() {
	initCurrInvestEcharts();
	setInterval('queryCurrInvestData()', 5*60*1000);
});

var CurrInvest_Obj = document.getElementById('curr_invest_chart_div');
var CurrInvest_Chart ;
function initCurrInvestEcharts() {
	//折线图
	CurrInvest_Chart = echarts.init(CurrInvest_Obj);
	// 使用刚指定的配置项和数据显示图表。
	queryCurrInvestInitData();
}
function queryCurrInvestData(){
	$.ajax({
		type: "POST",
		url: '../main/queryCurrInvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			CurrInvest_Chart.setOption(getCurrInvestOption(data));
		}
	});
}
function queryCurrInvestInitData(){
	$.ajax({
		type: "POST",
		url: '../main/queryCurrInvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			CurrInvest_Chart.setOption(getCurrInvestOption(data));
		}
	});
}
var labelTop = {
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
	var labelFromatter = {
	    normal : {
	        label : {
	            formatter : function (params){
	            	if(params.value > 100000000){
	            		return   formatNumber( params.value/100000000, 2) + "亿元"
	            	}else{
	            		return   formatNumber( params.value/10000, 2) + "万元"
	            	}
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
function getCurrInvestOption(data){
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
		            itemStyle : labelFromatter,
		            data : [
		            	{name:'other', value: data.month, itemStyle : labelBottom},
		            	{name:'当月投资总额', value: 2000000000-data.month,itemStyle : labelTop}
		            ]
		        }
		        ,
		        {
		            type : 'pie',
		            center : ['30%', '30%'],
		            radius : radius,
		            x:'20%', // for funnel
		            itemStyle : labelFromatter,
		            data : [
		                {name:'other', value:data.day, itemStyle : labelBottom},
		                {name:'当天投资总额', value:45000000 - data.day,itemStyle : labelTop}
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
