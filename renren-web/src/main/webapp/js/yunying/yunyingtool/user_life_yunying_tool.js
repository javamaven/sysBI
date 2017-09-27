$(function () {
	initTimeCond();
	initCurrInvestEcharts();
});
var CurrInvest_Obj = document.getElementById('echart_div');
var CurrInvest_Chart ;
function initCurrInvestEcharts() {
	//折线图
	CurrInvest_Chart = echarts.init(CurrInvest_Obj);
}

function exportDetail(timeLimit, isLiucun, isYingli){
	var params = getParams();
	params.timeLimit = timeLimit;
	params.isLiucun = isLiucun;
	params.isYingli = isYingli;
	console.info(params)
	executePost('../yunyingtool/exportDetail', {'params' : JSON.stringify(params)});  
}

function query(){
	var params = getParams();
	if(!params.first_invest_month_start){
		alert('请选择开始月份');
		return;
	}
	if(!params.first_invest_month_end){
		alert('请选择结束月份');
		return;
	}
	loading();
	$.ajax({
	    type: "POST",
	    url: "../yunyingtool/list",
	    data: JSON.stringify(params),
	    contentType: "application/json;charset=utf-8",
	    success : function(data) {
	    	loaded();
	    	var retData = data.data;
	    	buildTable(retData);
	    	var options = getCurrInvestOption(retData);
	    	CurrInvest_Chart.setOption(options);
	    }
	});
}


function getCurrInvestOption(data){
	var user1 = data['经营1-30天^留存^盈利']==null? 0:(data['经营1-30天^留存^盈利']['人数']);
	var user2 = data['经营1-30天^留存^亏损']==null? 0:(data['经营1-30天^留存^亏损']['人数']);
	var user3 = data['经营1-30天^流失^盈利']==null? 0:(data['经营1-30天^流失^盈利']['人数']);
	var user4 = data['经营1-30天^流失^亏损']==null? 0:(data['经营1-30天^流失^亏损']['人数']);
	
	var user5 = data['经营31-90天^留存^盈利']==null? 0:(data['经营31-90天^留存^盈利']['人数']);
	var user6 = data['经营31-90天^留存^亏损']==null? 0:(data['经营31-90天^留存^亏损']['人数']);
	var user7 = data['经营31-90天^流失^盈利']==null? 0:(data['经营31-90天^流失^盈利']['人数']);
	var user8 = data['经营31-90天^流失^亏损']==null? 0:(data['经营31-90天^流失^亏损']['人数']);
	
	var user9 = data['经营91-180天^留存^盈利']==null? 0:(data['经营91-180天^留存^盈利']['人数']);
	var user10 = data['经营91-180天^留存^亏损']==null? 0:(data['经营91-180天^留存^亏损']['人数']);
	var user11 = data['经营91-180天^流失^盈利']==null? 0:(data['经营91-180天^流失^盈利']['人数']);
	var user12 = data['经营91-180天^流失^亏损']==null? 0:(data['经营91-180天^流失^亏损']['人数']);
	
	var user13 = data['经营181天及以上^留存^盈利']==null? 0:(data['经营181天及以上^留存^盈利']['人数']);
	var user14 = data['经营181天及以上^留存^亏损']==null? 0:(data['经营181天及以上^留存^亏损']['人数']);
	var user15 = data['经营181天及以上^流失^盈利']==null? 0:(data['经营181天及以上^流失^盈利']['人数']);
	var user16 = data['经营181天及以上^流失^亏损']==null? 0:(data['经营181天及以上^流失^亏损']['人数']);
	
	var option = {
	    title: {
	        text: '用户生命周期分析',
//	        subtext: '数据来自国家统计局',
//	        sublink: 'http://data.stats.gov.cn/search/keywordlist2?keyword=%E5%9F%8E%E9%95%87%E5%B1%85%E6%B0%91%E6%B6%88%E8%B4%B9'
	    },
	    tooltip: {
	        trigger: 'axis',
	        backgroundColor: 'rgba(255,255,255,0.7)',
	        axisPointer: {
	            type: 'shadow'
	        },
	        formatter: function(params) {
	            var color = colorList[params[0].dataIndex];
	            var res = '<div style="color:' + color + '">';
	            res += '<strong>' + params[0].name + '（人）</strong>'
	            for (var i = 0, l = params.length; i < l; i++) {
	                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value 
	            }
	            res += '</div>';
	            return res;
	        }
	    },
	    legend: {
	        x: 'right',
	        data:['留存-盈利' , '留存-亏损', '流失-盈利', '流失-亏损']
	    },
	    calculable: true,
	    grid: {
	        y: 80,
	        y2: 40,
	        x2: 40
	    },
	    xAxis: [
	        {
	            type: 'category',
	            data: ['经营1-30天', '经营31-90天', '经营91-180天', '经营181天及以上']
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: '留存-盈利' ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [user1, user5, user9, user13]
	        },
	        {
	        	 name:  '留存-亏损' ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [user2, user6, user10, user14]
	        },
	        {
	        	 name:  '流失-盈利' ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [user3, user7, user11, user15]
	        },
	        {
	        	name:  '流失-亏损' ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [user4, user8, user12, user16]
	        }
	    ]
	};
	 return option;
}
var colorList = [
	  '#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed','#cd5c5c','#ffa500'
	];
var itemStyle = {
	    normal: {
	        color: function(params) {
	          if (params.seriesIndex == 0) {
	            return '#5b9bd5';
	          }else if(params.seriesIndex == 1){
	        	  return '#ed7d31';
	          }else if(params.seriesIndex == 2){
	        	  return '#da70d6';
	          }else {
	        	  return '#cd5c5c';
	          }
	        }
	    }
};

function buildTable(retData){
	var html = '';
	var times = [];
	times.push('经营1-30天');
	times.push('经营31-90天');
	times.push('经营91-180天');
	times.push('经营181天及以上');
//	times.push('汇总');//汇总的没有计算，直接将4值相加
	
	html += '<tr>';
	html += '<td>人数</td>'; 
	
	var total_licun_yingli_user_num = 0;
	var total_licun_kuisun_user_num = 0;
	var total_liushi_yingli_user_num = 0;
	var total_liushi_kuisun_user_num = 0;
	
	var total_licun_yingli_money = 0;
	var total_licun_kuisun_money = 0;
	var total_liushi_yingli_money = 0;
	var total_liushi_kuisun_money = 0;
	
	var total_licun_yingli_wait = 0;
	var total_licun_kuisun_wait = 0;
	var total_liushi_yingli_wait = 0;
	var total_liushi_kuisun_wait = 0;
	var money_line_html = '';
	var wait_line_html = '';
	var styleHtml = ' style="font-size: 12px;cursor: pointer;font-weight: normal;text-decoration:underline;color: black" ';
	for (var i = 0; i < times.length; i++) {
		var time = times[i];
		
		if(retData[time + '^留存^盈利']){
			total_licun_yingli_user_num += retData[time + '^留存^盈利']['人数'];
			total_licun_yingli_money += retData[time + '^留存^盈利']['盈亏金额（万元）'];
			total_licun_yingli_wait += retData[time + '^留存^盈利']['总待收（万元）'];
		}
		if(retData[time + '^留存^亏损']){
			total_licun_kuisun_user_num += retData[time + '^留存^亏损']['人数'];
			total_licun_kuisun_money += retData[time + '^留存^亏损']['盈亏金额（万元）'];
			total_licun_kuisun_wait += retData[time + '^留存^亏损']['总待收（万元）'];
		}
		if(retData[time + '^流失^盈利']){
			total_liushi_yingli_user_num += retData[time + '^流失^盈利']['人数'];
			total_liushi_yingli_money += retData[time + '^流失^盈利']['盈亏金额（万元）'];
			total_liushi_yingli_wait += retData[time + '^流失^盈利']['总待收（万元）'];
		}
		if(retData[time + '^流失^亏损']){
			total_liushi_kuisun_user_num += retData[time + '^流失^亏损']['人数'];
			total_liushi_kuisun_money += retData[time + '^流失^亏损']['盈亏金额（万元）'];
			total_liushi_kuisun_wait += retData[time + '^流失^亏损']['总待收（万元）'];
		}
		
		html += '<td><a ' + styleHtml + ' onclick="exportDetail(\''+time+'\',\'留存\',\'盈利\')" >' + (retData[time + '^留存^盈利'] == null? 0 : (retData[time + '^留存^盈利']['人数'])) + '</a></td>';
		html += '<td><a ' + styleHtml + ' onclick="exportDetail(\''+time+'\',\'留存\',\'亏损\')">' + (retData[time + '^留存^亏损'] == null? 0 : (retData[time + '^留存^亏损']['人数'])) + '</a></td>';
		html += '<td><a ' + styleHtml + ' onclick="exportDetail(\''+time+'\',\'流失\',\'盈利\')">' + (retData[time + '^流失^盈利'] == null? 0 : (retData[time + '^流失^盈利']['人数'])) + '</a></td>';
		html += '<td><a ' + styleHtml + ' onclick="exportDetail(\''+time+'\',\'流失\',\'亏损\')">' + (retData[time + '^流失^亏损'] == null? 0 : (retData[time + '^流失^亏损']['人数'])) + '</a></td>';
		
		money_line_html += '<td>' + (retData[time + '^留存^盈利'] == null ? 0 : (retData[time + '^留存^盈利']['盈亏金额（万元）'])) + '</td>';
		money_line_html += '<td>' + (retData[time + '^留存^亏损'] == null ? 0 : (retData[time + '^留存^亏损']['盈亏金额（万元）'])) + '</td>';
		money_line_html += '<td>' + (retData[time + '^流失^盈利'] == null ? 0 : (retData[time + '^流失^盈利']['盈亏金额（万元）'])) + '</td>';
		money_line_html += '<td>' + (retData[time + '^流失^亏损'] == null ? 0 : (retData[time + '^流失^亏损']['盈亏金额（万元）'])) + '</td>';
		
		wait_line_html += '<td>' + (retData[time + '^留存^盈利'] == null ? 0 : (retData[time + '^留存^盈利']['总待收（万元）'])) + '</td>';
		wait_line_html += '<td>' + (retData[time + '^留存^亏损'] == null ? 0 : (retData[time + '^留存^亏损']['总待收（万元）'])) + '</td>';
		wait_line_html += '<td>' + (retData[time + '^流失^盈利'] == null ? 0 : (retData[time + '^流失^盈利']['总待收（万元）'])) + '</td>';
		wait_line_html += '<td>' + (retData[time + '^流失^亏损'] == null ? 0 : (retData[time + '^流失^亏损']['总待收（万元）'])) + '</td>';
	}
	//汇总
	html += '<td><a ' + styleHtml + ' onclick="exportDetail(\'汇总\',\'留存\',\'盈利\')">' + total_licun_yingli_user_num + '</a></td>';
	html += '<td><a ' + styleHtml + ' onclick="exportDetail(\'汇总\',\'留存\',\'亏损\')">' + total_licun_kuisun_user_num + '</a></td>';
	html += '<td><a ' + styleHtml + ' onclick="exportDetail(\'汇总\',\'流失\',\'盈利\')">' + total_liushi_yingli_user_num + '</a></td>';
	html += '<td><a ' + styleHtml + ' onclick="exportDetail(\'汇总\',\'流失\',\'亏损\')">' + total_liushi_kuisun_user_num + '</a></td>';
	html += '</tr>';

	html += '<tr>';
	html += '<td>盈亏金额（万元）</td>';
	html += money_line_html;
	//汇总
	html += '<td>' + formatNumber(total_licun_yingli_money,2) + '</td>';
	html += '<td>' + formatNumber(total_licun_kuisun_money,2) + '</td>';
	html += '<td>' + formatNumber(total_liushi_yingli_money,2) + '</td>';
	html += '<td>' + formatNumber(total_liushi_kuisun_money,2) + '</td>';
	html += '</tr>';
	
	html += '<tr>';
	html += '<td>总待收（万元）</td>'; 
	html += wait_line_html;
	//汇总
	html += '<td>' + formatNumber(total_licun_yingli_wait,2) + '</td>';
	html += '<td>' + formatNumber(total_licun_kuisun_wait,2) + '</td>';
	html += '<td>' + formatNumber(total_liushi_yingli_wait,2) + '</td>';
	html += '<td>' + formatNumber(total_liushi_kuisun_wait,2) + '</td>';
	html += '</tr>';
	
	$("#table_div").html( getTableHead() + html );
}
function getTableHead(){
	var html = '';
	html += ' <tr> ' +
	' <th></th>' +
	' <th colspan="4" >经营1-30天</th> ' +
	' <th colspan="4" >经营31-90天</th> ' +
	' <th colspan="4" >经营91-180天</th> ' +
	' <th colspan="4" >经营181天及以上</th> ' +
	' <th colspan="4" >汇总</th> ' +
	' </tr> ' +
	' <tr>   ' +
	' <th>留存情况</th> ' +  
	' <th colspan="2" >留存</th>  ' +
	' <th colspan="2" >流失</th>  ' +
	' <th colspan="2" >留存</th>  ' +
	' <th colspan="2" >流失</th>  ' +
	' <th colspan="2" >留存</th>  ' +
	' <th colspan="2" >流失</th>  ' +
	' <th colspan="2" >留存</th>  ' +
	' <th colspan="2" >流失</th>  ' +
	' <th colspan="2" >留存</th>  ' +
	' <th colspan="2" >流失</th>  ' +
	' </tr> ' +
	' <tr>   ' +
	' <th>盈亏</th> ' +  
	
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +  
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +  
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +  
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +  
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +  
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +  
	' <th>盈利</th> ' +  
	' <th>亏损</th> ' +
	
	' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'first_invest_month_start': $("#first_invest_month_start").val(),
        	'first_invest_month_end': $("#first_invest_month_end").val(),
        	'user_type': $("#user_type_select").val()
	};
	return params;
}

function initTimeCond(){
  $("#first_invest_month_start").datetimepicker({
      format: 'yyyy-mm',
      weekStart: 1,  
      autoclose: true,  
      startView: 3,  
      minView: 3,  
      forceParse: false,  
      language: 'zh-CN'  
  }).on("click",function(){
  }); 
  $("#first_invest_month_end").datetimepicker({
      format: 'yyyy-mm',
      weekStart: 1,  
      autoclose: true,  
      startView: 3,  
      minView: 3,  
      forceParse: false,  
      language: 'zh-CN'  
  }).on("click",function(){
  });
}
