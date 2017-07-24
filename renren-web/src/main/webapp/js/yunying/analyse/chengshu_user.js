$(function () {
	initTimeCond();
	initCurrInvestEcharts();
	initExportFunction();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var day =  $("#stat_period").val();
		if(!day){
			alert('请先选择查询日期');
			return;
		}
		var params = getParams();
		executePost('../analyse/exportChenshuUser', {'params' : JSON.stringify(params)});  
	});

}

var CurrInvest_Obj = document.getElementById('dapan_div');
var CurrInvest_Obj2 = document.getElementById('dapan_div2');
var CurrInvest_Chart ;
var CurrInvest_Chart2 ;
function initCurrInvestEcharts() {
	//折线图
	CurrInvest_Chart = echarts.init(CurrInvest_Obj);
	CurrInvest_Chart2 = echarts.init(CurrInvest_Obj2);
}

function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}

function queryDapanAnalyse(){
	var day =  $("#stat_period").val();
	if(!day){
		alert('请先选择查询日期');
		return;
	}
	loading();
	 $.ajax({
		    type: "POST",
		    url: "../analyse/queryChengshuUserAnalyse",
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	console.info(msg)
		    	loaded();
		    	if(msg.data_list == null || msg.data_list && msg.data_list.length < 2){
		    		alert('没有数据');
		    	}else{
		    		buildTable(msg.data_list);
		    		CurrInvest_Chart.setOption(getCurrInvestOption(msg.data_list));
		    		CurrInvest_Chart2.setOption(getAvgInvestOption(msg.data_list));
		    	}
		    }
	 });

}
var itemStyle = {
    normal: {
        color: function(params) {
        	console.info(params)
          if (params.seriesIndex == 0) {
            return '#5b9bd5';
          }
          else {
        	  return '#ed7d31';
          }
        }
    }
};
function getCurrInvestOption(data){
	var row1 = data[0];
	var row2 = data[1];
	var option = {
	    title: {
	        text: '待收',
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
	            // for text color
	            var color = colorList[params[0].dataIndex];
	            var res = '<div style="color:' + color + '">';
	            res += '<strong>待收为' + params[0].name + '人数</strong>'
	            for (var i = 0, l = params.length; i < l; i++) {
	                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value 
	            }
	            res += '</div>';
	            return res;
	        }
	    },
	    legend: {
	        x: 'center',
//	        data:['2010', '2011']
	        data:[data[0].日期 , data[1].日期]
	    },
	    toolbox: {
	        show: true,
	        orient: 'vertical',
	        y: 'center',
	        feature: {
	            mark: {show: true},
	            dataView: {show: true, readOnly: false},
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
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
	            axisLabel: {  
	            	interval:0,  
	            	rotate:20  
	            },
	            data: ['0', '(0,5000)', '[5000,1万)', '[1万,2万)', '[2万,5万)', '[5万,10万)', '≥10万']
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: data[0].日期 ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [row1.待收_0,row1.待收_0_5000,row1.待收_5000_10000, row1.待收_10000_20000 , row1.待收_20000_50000 , row1.待收_50000_100000, row1.待收_大于等于100000]
	        },
	        {
	        	 name:  data[1].日期 ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [row2.待收_0,row2.待收_0_5000,row2.待收_5000_10000, row2.待收_10000_20000 , row2.待收_20000_50000 , row2.待收_50000_100000, row2.待收_大于等于100000]
	        }
	    ]
	};
	 return option;
}
var colorList = [
	  '#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
	  '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0'
	];
function getAvgInvestOption(data){
	var row1 = data[0];
	var row2 = data[1];
	var option = {
	    title: {
	        text: '平均投资额',
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
	            // for text color
	            var color = colorList[params[0].dataIndex];
	            var res = '<div style="color:' + color + '">';
	            res += '<strong>平均投资额为' + params[0].name + '人数</strong>'
	            for (var i = 0, l = params.length; i < l; i++) {
	                res += '<br/>' + params[i].seriesName + ' : ' + params[i].value 
	            }
	            res += '</div>';
	            return res;
	        }
	    },
	    legend: {
	        x: 'center',
//	        data:['2010', '2011']
	        data:[data[0].日期 , data[1].日期]
	    },
	    toolbox: {
	        show: true,
	        orient: 'vertical',
	        y: 'center',
	        feature: {
	            mark: {show: true},
	            dataView: {show: true, readOnly: false},
	            restore: {show: true},
	            saveAsImage: {show: true}
	        }
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
	            axisLabel: {  
	            	interval:0,  
	            	rotate:20  
	            },
	            data: ['(0,1千)', '[1千,3千)', '[3千,5千)', '[5千,1万)', '[1万,2万)', '[2万,5万)', '[5万,10万)', '≥10万']
	        }
	    ],
	    yAxis: [
	        {
	            type: 'value'
	        }
	    ],
	    series: [
	        {
	            name: data[0].日期 ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [row1.平均投资金额_0_1000,row1.平均投资金额_1000_3000,row1.平均投资金额_3000_5000, row1.平均投资金额_5000_10000 , row1.平均投资金额_10000_20000 , row1.平均投资金额_20000_50000, row1.平均投资金额_50000_100000, row1.平均投资金额_大于等于100000]
	        },
	        {
	        	 name:  data[1].日期 ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [row2.平均投资金额_0_1000,row2.平均投资金额_1000_3000,row2.平均投资金额_3000_5000, row2.平均投资金额_5000_10000 , row2.平均投资金额_10000_20000 , row2.平均投资金额_20000_50000, row2.平均投资金额_50000_100000, row2.平均投资金额_大于等于100000]
	        }
	    ]
	};
	 return option;
}
function getDaishouTableRow1(row1, row2){
	var html = 
	'<tr>' +  
    '    <td>0</td>' +   
    '    <td>'+ row1.待收_0 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_0*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_0-row2.待收_0)*100/row2.待收_0,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>(0,5000)</td>' +   
    '    <td>'+ row1.待收_0_5000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_0_5000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_0_5000-row2.待收_0_5000)*100/row2.待收_0_5000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[5000,1万)</td>' +   
    '    <td>'+ row1.待收_5000_10000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_5000_10000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_5000_10000-row2.待收_5000_10000)*100/row2.待收_5000_10000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[1万,2万)</td>' +   
    '    <td>'+ row1.待收_10000_20000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_10000_20000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_10000_20000-row2.待收_10000_20000)*100/row2.待收_10000_20000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[2万,5万)</td>' +   
    '    <td>'+ row1.待收_20000_50000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_20000_50000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_20000_50000-row2.待收_20000_50000)*100/row2.待收_20000_50000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[5万,10万)</td>' +   
    '    <td>'+ row1.待收_50000_100000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_50000_100000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_50000_100000-row2.待收_50000_100000)*100/row2.待收_50000_100000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>≥10万</td>' +   
    '    <td>'+ row1.待收_大于等于100000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_大于等于100000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.待收_大于等于100000-row2.待收_大于等于100000)*100/row2.待收_大于等于100000,2) +'%</td>' + 
    '</tr>' ;
	return html;
}
function getDaishouTableRow2(row1){
	var nullTd = '    <td> - </td>';
	var html = 
	'<tr>' +  
    '    <td>0</td>' +   
    '    <td>'+ row1.待收_0 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_0*100,2 ) +'%</td>' +    
     nullTd + 
    '</tr>' + 
    '<tr>' +  
    '    <td>(0,5000)</td>' +   
    '    <td>'+ row1.待收_0_5000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_0_5000*100,2 ) +'%</td>' +    
    nullTd + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[5000,1万)</td>' +   
    '    <td>'+ row1.待收_5000_10000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_5000_10000*100,2 ) +'%</td>' +    
    nullTd + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[1万,2万)</td>' +   
    '    <td>'+ row1.待收_10000_20000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_10000_20000*100,2 ) +'%</td>' +    
    nullTd + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[2万,5万)</td>' +   
    '    <td>'+ row1.待收_20000_50000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_20000_50000*100,2 ) +'%</td>' +    
    nullTd + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[5万,10万)</td>' +   
    '    <td>'+ row1.待收_50000_100000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_50000_100000*100,2 ) +'%</td>' +    
    nullTd + 
    '</tr>' + 
    '<tr>' +  
    '    <td>≥10万</td>' +   
    '    <td>'+ row1.待收_大于等于100000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_待收_大于等于100000*100,2 ) +'%</td>' +    
    nullTd + 
    '</tr>' ;
	return html;
}
function getAvgInvestTableRow1(row1, row2){
	var html = 
	'<tr>' +  
    '    <td>[1千,3千)</td>' +   
    '    <td>'+ row1.平均投资金额_1000_3000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_1000_3000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_1000_3000-row2.平均投资金额_1000_3000)*100/row2.平均投资金额_1000_3000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[3千,5千)</td>' +   
    '    <td>'+ row1.平均投资金额_3000_5000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_3000_5000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_3000_5000-row2.平均投资金额_3000_5000)*100/row2.平均投资金额_3000_5000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[5千,1万)</td>' +   
    '    <td>'+ row1.平均投资金额_5000_10000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_5000_10000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_5000_10000-row2.平均投资金额_5000_10000)*100/row2.平均投资金额_5000_10000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[1万,2万)</td>' +   
    '    <td>'+ row1.平均投资金额_10000_20000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_10000_20000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_10000_20000-row2.平均投资金额_10000_20000)*100/row2.平均投资金额_10000_20000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[2万,5万)</td>' +   
    '    <td>'+ row1.平均投资金额_20000_50000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_20000_50000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_20000_50000-row2.平均投资金额_20000_50000)*100/row2.平均投资金额_20000_50000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>[5万,10万)</td>' +   
    '    <td>'+ row1.平均投资金额_50000_100000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_50000_100000*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_50000_100000-row2.平均投资金额_50000_100000)*100/row2.平均投资金额_50000_100000,2) +'%</td>' + 
    '</tr>' + 
    '<tr>' +  
    '    <td>≥10万</td>' +   
    '    <td>'+ row1.平均投资金额_大于等于100000 +'</td>' +  
    '    <td>'+ formatNumber( row1.占比_平均投资金额_大于等于10万*100,2 ) +'%</td>' +    
    '    <td>'+ formatNumber((row1.平均投资金额_大于等于100000-row2.平均投资金额_大于等于100000)*100/row2.平均投资金额_大于等于100000,2) +'%</td>' + 
    '</tr>' ;
	return html;
}
function getAvgInvestTableRow2(row1){
	var nullTd = '    <td> - </td>';
	var html = 
		'<tr>' +  
	    '    <td>[1千,3千)</td>' +   
	    '    <td>'+ row1.平均投资金额_1000_3000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_1000_3000*100,2 ) +'%</td>' +    
	    nullTd + 
	    '</tr>' + 
	    '<tr>' +  
	    '    <td>[3千,5千)</td>' +   
	    '    <td>'+ row1.平均投资金额_3000_5000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_3000_5000*100,2 ) +'%</td>' +    
	    nullTd +  
	    '</tr>' + 
	    '<tr>' +  
	    '    <td>[5千,1万)</td>' +   
	    '    <td>'+ row1.平均投资金额_5000_10000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_5000_10000*100,2 ) +'%</td>' +    
	    nullTd +  
	    '</tr>' + 
	    '<tr>' +  
	    '    <td>[1万,2万)</td>' +   
	    '    <td>'+ row1.平均投资金额_10000_20000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_10000_20000*100,2 ) +'%</td>' +    
	    nullTd +  
	    '</tr>' + 
	    '<tr>' +  
	    '    <td>[2万,5万)</td>' +   
	    '    <td>'+ row1.平均投资金额_20000_50000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_20000_50000*100,2 ) +'%</td>' +    
	    nullTd +  
	    '</tr>' + 
	    '<tr>' +  
	    '    <td>[5万,10万)</td>' +   
	    '    <td>'+ row1.平均投资金额_50000_100000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_50000_100000*100,2 ) +'%</td>' +    
	    nullTd +  
	    '</tr>' + 
	    '<tr>' +  
	    '    <td>≥10万</td>' +   
	    '    <td>'+ row1.平均投资金额_大于等于100000 +'</td>' +  
	    '    <td>'+ formatNumber( row1.占比_平均投资金额_大于等于10万*100,2 ) +'%</td>' +    
	    nullTd +  
	    '</tr>' ;
		return html;
}
function buildTable(data_list){
	var html = '';
	var row1 = data_list[1];
	var row2 = data_list[0];
	
    html += '<tr>' +   
		    '	 <td rowspan="16">待收</td>' +    
		    '    <td rowspan="8">'+row1.日期+'</td>' +    
		    '    <td>全量（>5）</td>' +   
		    '    <td>'+ row1.全量用户 +'</td>' +  
		    '    <td>'+ formatNumber( row1.占比_全量*100,2 ) +'%</td>' +    
		    '    <td>'+ formatNumber((row1.全量用户-row2.全量用户)*100/row2.全量用户,2) +'%</td>' +  
		    '</tr>' +  
		    
		    getDaishouTableRow1(row1, row2) +
		    
		    '<tr>' +  
		    '    <td rowspan="8">'+row2.日期+'</td>' +    
		    '    <td>全量（>5）</td>' +    
		    '    <td>'+ row2.全量用户 +'</td>' +  
		    '    <td>'+ formatNumber( row2.占比_全量*100,2 ) +'%</td>' +    
		    '    <td> - </td>' +  
		    '</tr>' + 
		    getDaishouTableRow2(row2);
    
    html += '<tr>' +   
		    '	 <td rowspan="16">平均投资金额</td>' +    
		    '    <td rowspan="8">'+row1.日期+'</td>' +    
		    '    <td>(0,1千)</td>' +   
		    '    <td>'+ row1.平均投资金额_0_1000 +'</td>' +  
		    '    <td>'+ formatNumber( row1.占比_平均投资金额_0_1000*100,2 ) +'%</td>' +    
		    '    <td>'+ formatNumber((row1.平均投资金额_0_1000-row2.平均投资金额_0_1000)*100/row2.平均投资金额_0_1000,2) +'%</td>' +  
		    '</tr>' +  
		    
		    getAvgInvestTableRow1(row1, row2) +
		    
		    '<tr>' +  
		    '    <td rowspan="8">'+row2.日期+'</td>' +    
		    '    <td>(0,1千)</td>' +    
		    '    <td>'+ row2.全量用户 +'</td>' +  
		    '    <td>'+ formatNumber( row2.占比_全量*100,2 ) +'%</td>' +    
		    '    <td> - </td>' +  
		    '</tr>' + 
		    getAvgInvestTableRow2(row2);
	
	$("#dapan_table").html( getTableHead() + html);
}


function getTableHead(){
	var html = '';
	html += ' <tr> ' +
		' <td>类别</td> ' +
		' <td>日期</td> ' +
		' <td>区间</td> ' +
		' <td>人数</td> ' +
		' <td>占比</td> ' +
		' <td>环比</td> ' +
		' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}