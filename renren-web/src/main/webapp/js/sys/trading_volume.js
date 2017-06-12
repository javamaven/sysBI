$(function() {
	initInvestQushiEcharts();
	
	setInterval('queryQushiData()', 30*60*1000);
});

var qushiObj = document.getElementById('qushi_chart_div');
var qushiChart ;
var data_list = [];
var time_list = [];
function initInvestQushiEcharts() {
	//折线图
	qushiChart = echarts.init(qushiObj);
	// 使用刚指定的配置项和数据显示图表。
	queryInitData();
}
function queryQushiData(){
	$.ajax({
		type: "POST",
		url: '../main/queryLast30MinuteInvestAmount',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			console.info(data)
			data_list.push(data.amount);
			time_list.push(data.time.substring(11, data.time.length));
			qushiChart.setOption(getQushiOption());
		}
	});
}
function queryInitData(){
	$.ajax({
		type: "POST",
		url: '../main/queryInvestAmountList',
		data: {},
		contentType: "application/json;charset=utf-8",
		success : function(data) {
			console.log(data)
			data_list = data.data_list;
			for (var i = 0; i < data.time_list.length; i++) {
				var t = data.time_list[i];
				time_list.push(t.substring(11, t.length));
			}
			qushiChart.setOption(getQushiOption());
		}
	});
}

function getQushiOption(){
return {
		title:{
//			text: '投资额'
		},
//		legend:{
//			show:true,
//			data: [{
//			    name: '投资额(元)',
//			    // 强制设置图形为圆。
////			    icon: 'circle',
//			    // 设置文本为红色
//			    textStyle: {
//			        color: '#3398DB'
//			    }
//			}]
//		},
		color: ['#3398DB'],
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'line'        // 默认为直线，可选为：'line' | 'shadow'
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
//	            data : ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun','Fri', 'Sat', 'Sun' ],
	            data : time_list,
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
	            name:'投资额(元)',
	            type:'bar',
	            barWidth: '60%',
//	            data:[10, 52, 200, 334, 390, 330, 220, 200, 334, 390]
	            data: data_list
	        }
	    ]
	};

}
