$(function () {
	initTimeCond();
	initExportFunction();
	initCurrInvestEcharts();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var day_last =  $("#stat_period_last").val();
		if(!day_last){
			alert('请先选择上期日期');
			return;
		}
		var day_curr =  $("#stat_period_curr").val();
		if(!day_curr){
			alert('请先选择本期日期');
			return;
		}
		var params = getParams();
		executePost('../analyse/exportDapanAnalyse', {'params' : JSON.stringify(params)});  
	});

}

var CurrInvest_Obj = document.getElementById('dapan_div');
var CurrInvest_Chart ;
function initCurrInvestEcharts() {
	//折线图
	CurrInvest_Chart = echarts.init(CurrInvest_Obj);
}

function initTimeCond(){
    $("#stat_period_last").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#stat_period_curr").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}

var table_html = '';
function queryDapanAnalyse(){
	table_html = '';
	var day_last =  $("#stat_period_last").val();
	if(!day_last){
		alert('请先选择上期日期');
		return;
	}
	var day_curr =  $("#stat_period_curr").val();
	if(!day_curr){
		alert('请先选择本期日期');
		return;
	}
	
	loading();
	$.ajax({
		    type: "POST",
//		    async: false,
		    url: "../analyse/queryDapanAnalyse",
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	console.info(msg)
		    	if(msg.data_list == null || msg.data_list && msg.data_list.length < 2){
		    		alert('没有数据');
		    	}else{
		    		var html = buildTable(msg.data_list);
		    		if(table_html == ''){
		    			table_html += html;
		    		}
		    		CurrInvest_Chart.setOption(getCurrInvestOption(msg.data_list));
		    		$.ajax({
		    		    type: "POST",
//		    		    async: false,
		    		    url: "../analyse/queryDapanDaishouAnalyse",
		    		    data: JSON.stringify(getParams()),
		    		    contentType: "application/json;charset=utf-8",
		    		    success : function(msg) {
		    		    	loaded();
		    		    	if(msg.data_list == null || msg.data_list && msg.data_list.length < 2){
		    		    		alert('没有数据');
		    		    	}else{
		    		    		buildDaishouTable(msg.data_list);
//		    		    		CurrInvest_Chart.setOption(getCurrInvestOption(msg.data_list));
		    		    	}
		    		    }
		    		});
		    	}
		    }
	 });

}
var colorList = [
	  '#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
	  '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0'
	];
var itemStyle = {
	    normal: {
	        color: function(params) {
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
	        text: '总体用户分布',
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
	        data:[data[0].日期 , data[1].日期]
	    },
//	    toolbox: {
//	        show: true,
//	        orient: 'vertical',
//	        y: 'center',
//	        feature: {
//	            mark: {show: true},
//	            dataView: {show: true, readOnly: false},
//	            restore: {show: true},
//	            saveAsImage: {show: true}
//	        }
//	    },
	    calculable: true,
	    grid: {
	        y: 80,
	        y2: 40,
	        x2: 40
	    },
	    xAxis: [
	        {
	            type: 'category',
	            data: ['总投资用户', '高净值用户', '沉默全量用户', '全量新用户', '成熟全量用户', '自然全量用户']
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
	            data: [row1.总投资用户,row1.总高净值用户,row1.沉默全量用户, row1.全量新用户 , row1.成熟全量用户 , row1.自然全量用户]
	        },
	        {
	        	 name:  data[1].日期 ,
	            type: 'bar',
	            itemStyle: itemStyle,
	            data: [row2.总投资用户,row2.总高净值用户,row2.沉默全量用户, row2.全量新用户 , row2.成熟全量用户 , row2.自然全量用户]
	        }
	    ]
	};
	 return option;
}
/**
 * 大盘分析-待收表格
 * @param data_list
 * @returns
 */
function buildDaishouTable(data_list){
	var html = '';
	for (var i = 0; i < data_list.length; i++) {
		var row = data_list[i];
        html += '<tr>' +   
			    '	 <td>'+row.日期+'</td>' +    
			    '    <td>'+formatNumber( row.总投资用户待收资金,2 ) +'</td>' +    
			    '    <td>'+formatNumber( row.总高净值用户待收资金,2 ) +'</td>' +  
			    '    <td>'+ formatNumber( row.总高净值用户待收资金_占比*100,2 ) +'%</td>' +  
			    
			    '    <td>'+ formatNumber( row.沉默用户待收资金 ,2 )+'</td>' +    
			    '    <td>'+ formatNumber( row.沉默用户_高净值待收资金 ,2 )+'</td>' +   
			    '    <td>'+ formatNumber( row.沉默用户待收资金_占比*100,2 )+'%</td>' +   
			    
			    '    <td>'+ formatNumber( row.新用户待收资金,2 ) +'</td>' +    
			    '    <td>'+ formatNumber( row.新用户_高净值待收资金,2 ) +'</td>' +   
			    '    <td>'+ formatNumber( row.新用户待收资金_占比*100,2 )+'%</td>' +  
			    
			    '    <td>'+ formatNumber( row.成熟用户待收资金,2 )+'</td>' +    
			    '    <td>'+ formatNumber( row.成熟用户_高净值待收资金,2 )+'</td>' +   
			    '    <td>'+ formatNumber( row.成熟用户待收资金_占比*100,2)+'%</td>' +  
			    
			    '    <td>'+ formatNumber( row.自然用户待收资金,2 )+'</td>' +    
			    '    <td>'+ formatNumber( row.自然用户_高净值待收资金,2 )+'</td>' +   
			    '    <td>'+ formatNumber( row.自然用户待收资金_占比*100, 2) +'%</td>' +  
			    '</tr>'; 
	}
	
	$("#dapan_table").html(table_html + getDaishouTableHead() + html + getDaishouLastWeekHuanbi(data_list[0], data_list[1]));
//	$("#dapan_daishou_table").html( getDaishouTableHead() + html + getDaishouLastWeekHuanbi(data_list[0], data_list[1]));
}
function buildTable(data_list){
	var html = '';
	
	for (var i = 0; i < data_list.length; i++) {
		var row = data_list[i];
        html += '<tr>' +   
			    '	 <td>'+row.日期+'</td>' +    
			    '    <td>'+row.总投资用户+'</td>' +    
			    '    <td>'+row.总高净值用户+'</td>' +   
			    '    <td>'+ formatNumber( row.占比*100,2 ) +'%</td>' +  
			    '    <td>'+row.沉默全量用户+'</td>' +    
			    '    <td>'+row.沉默_高净值用户+'</td>' +   
			    '    <td>'+ formatNumber( row.沉默_占比*100,2 )+'%</td>' +    
			    '    <td>'+row.全量新用户+'</td>' +    
			    '    <td>'+row.新用户_高净值用户+'</td>' +   
			    '    <td>'+ formatNumber( row.新用户_占比*100,2 )+'%</td>' +  
			    '    <td>'+row.成熟全量用户+'</td>' +    
			    '    <td>'+row.成熟_高净值用户+'</td>' +   
			    '    <td>'+formatNumber(row.成熟_占比*100,2)+'%</td>' +  
			    '    <td>'+row.自然全量用户+'</td>' +    
			    '    <td>'+row.自然_高净值用户+'</td>' +   
			    '    <td>'+ formatNumber(row.自然_占比*100, 2) +'%</td>' +  
			    '</tr>'; 
	}
	
	return  getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]);
//	$("#dapan_table").html( getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]));
}
function getDaishouLastWeekHuanbi(lastweek, curr){
	var html = '';
    html += '<tr>' +   
    '	 <td>与上期环比</td>' +    
    '    <td>'+ formatNumber((curr.总投资用户待收资金-lastweek.总投资用户待收资金)*100/lastweek.总投资用户待收资金,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总高净值用户待收资金-lastweek.总高净值用户待收资金)*100/lastweek.总高净值用户待收资金,2) +'%</td>' +   
    '    <td></td>' +  
    '    <td>'+ formatNumber((curr.沉默用户待收资金-lastweek.沉默用户待收资金)*100/lastweek.沉默用户待收资金,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.沉默用户_高净值待收资金-lastweek.沉默用户_高净值待收资金)*100/lastweek.沉默用户_高净值待收资金,2) +'%</td>' +   
    '    <td></td>' +    
    '    <td>'+ formatNumber((curr.新用户待收资金-lastweek.新用户待收资金)*100/lastweek.新用户待收资金,2) +'%</td>' + 
    '    <td>'+ formatNumber((curr.新用户_高净值待收资金-lastweek.新用户_高净值待收资金)*100/lastweek.新用户_高净值待收资金,2) +'%</td>' + 
    '    <td></td>' +  
    '    <td>'+ formatNumber((curr.成熟用户待收资金-lastweek.成熟用户待收资金)*100/lastweek.成熟用户待收资金,2) +'%</td>' + 
    '    <td>'+ formatNumber((curr.成熟用户_高净值待收资金-lastweek.成熟用户_高净值待收资金)*100/lastweek.成熟用户_高净值待收资金,2) +'%</td>' + 
    '    <td></td>' +  
    '    <td>'+ formatNumber((curr.自然用户待收资金-lastweek.自然用户待收资金)*100/lastweek.自然用户待收资金,2) +'%</td>' + 
    '    <td>'+ formatNumber((curr.自然用户_高净值待收资金-lastweek.自然用户_高净值待收资金)*100/lastweek.自然用户_高净值待收资金,2) +'%</td>' + 
    '    <td></td>' +  
    '</tr>'; 
    return html;
}
function getLastWeekHuanbi(lastweek, curr){
	var html = '';
    html += '<tr>' +   
    '	 <td>与上期环比</td>' +    
    '    <td>'+ formatNumber((curr.总投资用户-lastweek.总投资用户)*100/lastweek.总投资用户,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总高净值用户-lastweek.总高净值用户)*100/lastweek.总高净值用户,2) +'%</td>' +   
    '    <td></td>' +  
    '    <td>'+ formatNumber((curr.沉默全量用户-lastweek.沉默全量用户)*100/lastweek.沉默全量用户,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.沉默_高净值用户-lastweek.沉默_高净值用户)*100/lastweek.沉默_高净值用户,2) +'%</td>' +   
    '    <td></td>' +    
    '    <td>'+ formatNumber((curr.全量新用户-lastweek.全量新用户)*100/lastweek.全量新用户,2) +'%</td>' + 
    '    <td>'+ formatNumber((curr.新用户_高净值用户-lastweek.新用户_高净值用户)*100/lastweek.新用户_高净值用户,2) +'%</td>' + 
    '    <td></td>' +  
    '    <td>'+ formatNumber((curr.成熟全量用户-lastweek.成熟全量用户)*100/lastweek.成熟全量用户,2) +'%</td>' + 
    '    <td>'+ formatNumber((curr.成熟_高净值用户-lastweek.成熟_高净值用户)*100/lastweek.成熟_高净值用户,2) +'%</td>' + 
    '    <td></td>' +  
    '    <td>'+ formatNumber((curr.自然全量用户-lastweek.自然全量用户)*100/lastweek.自然全量用户,2) +'%</td>' + 
    '    <td>'+ formatNumber((curr.自然_高净值用户-lastweek.自然_高净值用户)*100/lastweek.自然_高净值用户,2) +'%</td>' + 
    '    <td></td>' +  
    '</tr>'; 
    return html;
}
function getNbsp(num){
	var html = '';
	for (var i = 0; i < num; i++) {
//		html += '&nbsp;';
	}
	return html;
}
function getDaishouTableHead(){
	var html = '';
	html += ' <tr> ' +
	' <th rowspan="2"  >'+getNbsp(6)+'日&nbsp;期'+getNbsp(6)+'</th> ' +
	' <th rowspan="2"  >'+getNbsp(6)+'总投资用户待收资金</th>'+getNbsp(6)+' ' +
	' <th colspan="2"  >'+getNbsp(6)+'总高净值用户待收资金</th>'+getNbsp(6)+' ' +
	' <th colspan="3" >沉默用户待收资金</th> ' +
	' <th colspan="3"  >新用户待收资金</th> ' +
	' <th colspan="3"  >成熟用户待收资金</th> ' +
	' <th colspan="3"  >自然用户待收资金</th> ' +
	' </tr> ' +
	' <tr>   ' +
	' <th>高净值用户</th> ' +  
	' <th>占比</th>  ' +
	' <th>沉默全量用户</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>占比</th>   ' +
	' <th>全量新用户</th>   ' +
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
	' <th>成熟全量用户</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
	' <th>自然全量用户</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
		' </tr> ';
	return html;
}
function getTableHead(){
	var html = '';
	html += ' <tr> ' +
	' <th rowspan="2">日期</th> ' +
	' <th rowspan="2">总投资用户</th> ' +
	' <th colspan="2">高净值用户</th> ' +
	' <th colspan="3">沉默用户</th> ' +
	' <th colspan="3">新用户</th> ' +
	' <th colspan="3">成熟用户</th> ' +
	' <th colspan="3">自然用户</th> ' +
	' </tr> ' +
	' <tr>   ' +
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
	' <th>沉默全量用户</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>占比</th>   ' +
	' <th>全量新用户</th>   ' +
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
	' <th>成熟全量用户</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
	' <th>自然全量用户</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>占比</th> ' +
	' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriodLast': $("#stat_period_last").val(),
        	'statPeriodCurr': $("#stat_period_curr").val()
	};
	return params;
}