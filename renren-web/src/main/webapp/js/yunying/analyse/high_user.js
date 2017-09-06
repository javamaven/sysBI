$(function () {
	initTimeCond();
	initCurrInvestEcharts();
	initExportFunction();
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
		executePost('../analyse/exportHighUser', {'params' : JSON.stringify(params)});  
	});

}

var chartDivObj = document.getElementById('dapan_div');
var chartDivObj2 = document.getElementById('dapan_div2');
var chart ;
var chart2 ;
function initCurrInvestEcharts() {
	//折线图
	chart = echarts.init(chartDivObj);
	chart2 = echarts.init(chartDivObj2);
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

function queryDapanAnalyse(){
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
		    url: "../analyse/queryHighUserAnalyse",
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	buildTable(msg.data_list);
		    	
		    	chart.setOption(getCurrInvestOption(msg.data_list[0]));
		    	chart2.setOption(getCurrInvestOption(msg.data_list[1]));
		    }
	 });

}
//var zrColor = require('zrender/tool/color');
var colorList = [
  '#ff7f50','#87cefa','#da70d6','#32cd32','#6495ed',
  '#ff69b4','#ba55d3','#cd5c5c','#ffa500','#40e0d0'
];
var itemStyle = {
//    normal: {
//        color: function(params) {
//          if (params.dataIndex < 0) {
//            // for legend
//            return colorList[0];
//          }
//          else {
//            // for bar
//        	  return colorList[0];
//          }
//        }
//    }
};
function getCurrInvestOption(data){
	console.info(data)
	var option = {
		    title : {
		        text: '高净值用户分布',
		        subtext: data.日期,
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
//		    legend: {
//		        orient: 'vertical',
//		        left: 'left',
//		        data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
//		    },
		    series : [
		        {
		            name: '高净值用户',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:[
		                {value: data.人数_10_20 , name:'[10,20)人数'},
		                {value: data.人数_20_50 , name:'[20,50)人数'},
		                {value: data.人数_50_100 , name:'[50,100)人数'},
		                {value: data.人数_100_200 , name:'[100,200)人数'},
		                {value: data.人数_200 , name:'>=200人数'}
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

	 return option;
}

function buildTable(data_list){
	var html = '';
	
	for (var i = 0; i < data_list.length; i++) {
		var row = data_list[i];
        html += '<tr>' +   
			    '	 <td>'+row.日期+'</td>' +    
			    
			    '    <td>'+row.人数+'</td>' +    
			    '    <td>'+formatNumber(row.总投资金额_万元,2)+'</td>' +   
			    '    <td>'+ row.人均笔均红包使用_ALL + '</td>' +  
        
			    '    <td>'+row.人数_10_20+'</td>' +    
			    '    <td>'+formatNumber(row.总投资金额_10_20_万元,2)+'</td>' +   
			    '    <td>'+ row.人均笔均红包使用_10_20 + '</td>' +    
			    
			    '    <td>'+row.人数_20_50+'</td>' +    
			    '    <td>'+formatNumber(row.总投资金额_20_50_万元,2)+'</td>' +   
			    '    <td>'+ row.人均笔均红包使用_20_50 + '</td>' +  
			    
			    '    <td>'+row.人数_50_100+'</td>' +    
			    '    <td>'+formatNumber(row.总投资金额_50_100_万元,2)+'</td>' +   
			    '    <td>'+ row.人均笔均红包使用_50_100 + '</td>' +  		
			    
			    '    <td>'+row.人数_100_200+'</td>' +    
			    '    <td>'+formatNumber(row.总投资金额_100_200_万元,2)+'</td>' +   
			    '    <td>'+ row.人均笔均红包使用_100_200 + '</td>' +  
			    
			    '    <td>'+row.人数_200+'</td>' +    
			    '    <td>'+formatNumber(row.总投资金额_200_万元,2)+'</td>' +   
			    '    <td>'+ row.人均笔均红包使用_200 + '</td>' +  			    
			    '</tr>'; 
	}
	
	$("#dapan_table").html( getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]));
}
function getLastWeekHuanbi(lastweek, curr){
	var html = '';
    html += '<tr>' +   
    '	 <td>与上期环比</td>' +    
    '    <td>'+ formatNumber((curr.人数-lastweek.人数)*100/lastweek.人数,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总投资金额_万元-lastweek.总投资金额_万元)*100/lastweek.总投资金额_万元,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.人均笔均红包使用_ALL-lastweek.人均笔均红包使用_ALL)*100/lastweek.人均笔均红包使用_ALL,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.人数_10_20-lastweek.人数_10_20)*100/lastweek.人数_10_20,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.总投资金额_10_20_万元-lastweek.总投资金额_10_20_万元)*100/lastweek.总投资金额_10_20_万元,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.人均笔均红包使用_10_20-lastweek.人均笔均红包使用_10_20)*100/lastweek.人均笔均红包使用_10_20,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_20_50-lastweek.人数_20_50)*100/lastweek.人数_20_50,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.总投资金额_20_50_万元-lastweek.总投资金额_20_50_万元)*100/lastweek.总投资金额_20_50_万元,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.人均笔均红包使用_50_100-lastweek.人均笔均红包使用_50_100)*100/lastweek.人均笔均红包使用_50_100,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_50_100-lastweek.人数_50_100)*100/lastweek.人数_50_100,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.总投资金额_50_100_万元-lastweek.总投资金额_50_100_万元)*100/lastweek.总投资金额_50_100_万元,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.人均笔均红包使用_10_20-lastweek.人均笔均红包使用_10_20)*100/lastweek.人均笔均红包使用_10_20,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_100_200-lastweek.人数_100_200)*100/lastweek.人数_100_200,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.总投资金额_100_200_万元-lastweek.总投资金额_100_200_万元)*100/lastweek.总投资金额_100_200_万元,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.人均笔均红包使用_100_200-lastweek.人均笔均红包使用_100_200)*100/lastweek.人均笔均红包使用_100_200,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_200-lastweek.人数_200)*100/lastweek.人数_200,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.总投资金额_200_万元-lastweek.总投资金额_200_万元)*100/lastweek.总投资金额_200_万元,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.人均笔均红包使用_200-lastweek.人均笔均红包使用_200)*100/lastweek.人均笔均红包使用_200,2) +'%</td>' +  
    
    '</tr>'; 
    return html;
}

function getTableHead(){
	var html = '';
	html += ' <tr> ' +
		' <th rowspan="2">日期</th> ' +
		' <th colspan="3">全量</th> ' +
		' <th colspan="3">[10W,20W)</th> ' +
		' <th colspan="3">[20W,50W)</th> ' +
		' <th colspan="3">[50W,100W)</th> ' +
		' <th colspan="3">[100W,200W)</th> ' +
		' <th colspan="3">≥200W</th> ' +
		' </tr> ' +
		' <tr>   ' +
		' <th>人数</th> ' +  
		' <th>总投资金额</th>  ' +
		' <th>人均笔均红包使用</th> ' +
		' <th>[10,20)人数</th> ' +  
		' <th>总投资金额</th>  ' +
		' <th>人均笔均红包使用</th>   ' +
		' <th>[20,50)人数</th>   ' +
		' <th>总投资金额</th>  ' +
		' <th>人均笔均红包使用</th> ' +
		' <th>[50,100)人数</th> ' +  
		' <th>总投资金额</th>  ' +
		' <th>人均笔均红包使用</th> ' +
		' <th>[100,200)人数</th> ' +  
		' <th>总投资金额</th>  ' +
		' <th>人均笔均红包使用</th> ' +
		' <th>≥200人数</th> ' +  
		' <th>总投资金额</th>  ' +
		' <th>人均笔均红包使用</th> ' +
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