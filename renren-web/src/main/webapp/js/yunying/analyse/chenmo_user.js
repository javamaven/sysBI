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
		executePost('../analyse/exportChenmoUser', {'params' : JSON.stringify(params)});  
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
		    url: "../analyse/queryChenmoUserAnalyse",
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
		        text: '沉默用户分布',
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
		            name: '沉默用户',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:[
		                {value: data.人数_30 , name:'=30人数'},
		                {value: data.人数_30_90 , name:'(30,90)人数'},
		                {value: data.人数_90 , name:'=90人数'},
		                {value: data.人数_90_180 , name:'(90,180)人数'},
		                {value: data.人数_180 , name:'=180人数'},
		                {value: data.人数_大于180 , name:'>180人数'}
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
			    
			    '    <td>'+ row.人数 +'</td>' +    
			    '    <td>'+ row.高净值用户数_全量 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_全量,2) + '</td>' +  
        
			    '    <td>'+ row.人数_30 +'</td>' +    
			    '    <td>'+ row.高净值用户数_30 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_30,2) + '</td>' +  
			    
			    '    <td>'+ row.人数_30_90 +'</td>' +    
			    '    <td>'+ row.高净值用户数_30_90 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_30_90,2) + '</td>' +  
			    
			    '    <td>'+ row.人数_90 +'</td>' +    
			    '    <td>'+ row.高净值用户数_90 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_90,2) + '</td>' +  
			    
			    '    <td>'+ row.人数_90_180 +'</td>' +    
			    '    <td>'+ row.高净值用户数_90_180 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_90_180,2) + '</td>' +  
			    
			    '    <td>'+ row.人数_180 +'</td>' +    
			    '    <td>'+ row.高净值用户数_180 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_180,2) + '</td>' +  
			    
			    '    <td>'+ row.人数_大于180 +'</td>' +    
			    '    <td>'+ row.高净值用户数_大于180 +'</td>' +   
			    '    <td>'+ formatNumber(row.平均投资金额_大于180,2) + '</td>' +  
			    '</tr>'; 
	}
	
	$("#dapan_table").html( getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]));
}
function getLastWeekHuanbi(lastweek, curr){
	var html = '';
    html += '<tr>' +   
    '	 <td>与上周环比</td>' +    
    '    <td>'+ formatNumber((curr.人数-lastweek.人数)*100/lastweek.人数,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.高净值用户数_全量-lastweek.高净值用户数_全量)*100/lastweek.高净值用户数_全量,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均投资金额_全量-lastweek.平均投资金额_全量)*100/lastweek.平均投资金额_全量,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.人数_30-lastweek.人数_30)*100/lastweek.人数_30,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.高净值用户数_30-lastweek.高净值用户数_30)*100/lastweek.高净值用户数_30,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均投资金额_30-lastweek.平均投资金额_30)*100/lastweek.平均投资金额_30,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_30_90-lastweek.人数_30_90)*100/lastweek.人数_30_90,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.高净值用户数_30_90-lastweek.高净值用户数_30_90)*100/lastweek.高净值用户数_30_90,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均投资金额_30_90-lastweek.平均投资金额_30_90)*100/lastweek.平均投资金额_30_90,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_90-lastweek.人数_90)*100/lastweek.人数_90,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.高净值用户数_90-lastweek.高净值用户数_90)*100/lastweek.高净值用户数_90,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均投资金额_90-lastweek.平均投资金额_90)*100/lastweek.平均投资金额_90,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_90_180-lastweek.人数_90_180)*100/lastweek.人数_90_180,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.高净值用户数_90_180-lastweek.高净值用户数_90_180)*100/lastweek.高净值用户数_90_180,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均投资金额_90_180-lastweek.平均投资金额_90_180)*100/lastweek.平均投资金额_90_180,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.人数_180-lastweek.人数_180)*100/lastweek.人数_180,2) +'%</td>' ;
    
    if(lastweek.高净值用户数_180 == 0){
    	html += '    <td>0%</td>';  
    }else{
    	html += '    <td>'+ formatNumber((curr.高净值用户数_180-lastweek.高净值用户数_180)*100/lastweek.高净值用户数_180,2) +'%</td>'; 
    }
    if(lastweek.平均投资金额_180 == 0){
    	html += '    <td>0%</td>';  
    }else{
    	html += '    <td>'+ formatNumber((curr.平均投资金额_180-lastweek.平均投资金额_180)*100/lastweek.平均投资金额_180,2) +'%</td>';
    }
    html += 
    '    <td>'+ formatNumber((curr.人数_大于180-lastweek.人数_大于180)*100/lastweek.人数_大于180,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.高净值用户数_大于180-lastweek.高净值用户数_大于180)*100/lastweek.高净值用户数_大于180,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均投资金额_大于180-lastweek.平均投资金额_大于180)*100/lastweek.平均投资金额_大于180,2) +'%</td>' +  
    
    '</tr>'; 
    return html;
}

function getTableHead(){
	var html = '';
	html += ' <tr> ' +
	' <th rowspan="2">日期</th> ' +
	' <th colspan="3">全量</th> ' +
	' <th colspan="3">=30</th> ' +
	' <th colspan="3">(30,90)</th> ' +
	' <th colspan="3">=90</th> ' +
	' <th colspan="3">(90,180)</th> ' +
	' <th colspan="3">=180</th> ' +
	' <th colspan="3">>180</th> ' +
	' </tr> ' +
	' <tr>   ' +
	' <th>人数</th> ' +  
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th> ' +
	' <th>=30人数</th> ' +  
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th>   ' +
	' <th>(30,90)人数</th>   ' +
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th> ' +
	' <th>=90人数</th> ' +  
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th> ' +
	' <th>(90,180)人数</th> ' +  
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th> ' +
	' <th>=180人数</th> ' +  
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th> ' +
	' <th>>180人数</th> ' +  
	' <th>高净值用户数</th>  ' +
	' <th>平均投资金额</th> ' +
		' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}