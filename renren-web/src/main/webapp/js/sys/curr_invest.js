$(function() {
//	initCurrInvestEcharts();
	queryCurrInvestData();
	setInterval('queryCurrInvestData()', 5*60*1000);
});

//var CurrInvest_Obj = document.getElementById('curr_invest_chart_div');
//var CurrInvest_Chart ;
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
			console.info(data)
			if(data.day > 100000000){
				$("#day_invest").html(formatNumber( data.day/100000000, 2) + "亿元");
        	}else{
        		$("#day_invest").html(formatNumber( data.day/10000, 2) + "万元");
        	}
			if(data.month > 100000000){
				$("#month_invest").html(formatNumber( data.month/100000000, 2) + "亿元");
        	}else{
        		$("#month_invest").html(formatNumber( data.month/10000, 2) + "万元");
        	}
			
			var yesterday_total_invest_amount = data.yesterday_total_invest_amount;
			var last_month_total_invest_amount = data.last_month_total_invest_amount;
			//当天与昨天投资额百分比
			if(data.day > yesterday_total_invest_amount){
				$("#last_day_rate").css("width", "100%")
			}else{
				var rate = data.day*100/yesterday_total_invest_amount;
				rate = parseInt(rate);
				$("#last_day_rate").css("width", rate + "%")
			}
			//当月与上月投资额百分比
			if(data.month > last_month_total_invest_amount){
				$("#last_month_rate").css("width", "100%")
			}else{
				var rate = data.month*100/last_month_total_invest_amount;
				rate = parseInt(rate);
				$("#last_month_rate").css("width", rate + "%")
			}
			
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
