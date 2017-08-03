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
		executePost('../analyse/exportNewUser', {'params' : JSON.stringify(params)});  
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
		    url: "../analyse/queryNewUserAnalyse",
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
		        text: '新用户分布',
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
		            name: '新用户',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:[
		                {value: data.用户数_1 , name:'投资次数=1'},
		                {value: data.用户数_2 , name:'投资次数=2'},
		                {value: data.用户数_3 , name:'投资次数=3'},
		                {value: data.用户数_4_5 , name:'投资次数[4,5]'},
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
			    
			    '    <td>'+row.用户数+'</td>' +    
			    '    <td>'+ row.高净值用户数_全量 +'</td>' +   
			    '    <td>'+ formatNumber(row.总投资金额_全量,2) + '</td>' +  
			    '    <td>'+ formatNumber(row.总待收_全量,2)+ '</td>' +  
			    '    <td>'+ row.平均红包金额_全量 + '</td>' +  
			    
			    '    <td>'+row.用户数_1+'</td>' +    
			    '    <td>'+ row.高净值用户数_1 +'</td>' +   
			    '    <td>'+ formatNumber(row.总投资金额_1,2) + '</td>' +  
			    '    <td>'+ formatNumber(row.总待收_1,2)+ '</td>' +  
			    '    <td>'+ row.平均红包金额_1 + '</td>' +  
			    
			    '    <td>'+row.用户数_2+'</td>' +    
			    '    <td>'+ row.高净值用户数_2 +'</td>' +   
			    '    <td>'+ formatNumber(row.总投资金额_2,2) + '</td>' +  
			    '    <td>'+ formatNumber(row.总待收_2,2)+ '</td>' +  
			    '    <td>'+ row.平均红包金额_2 + '</td>' +  
			    
			    '    <td>'+row.用户数_3+'</td>' +    
			    '    <td>'+ row.高净值用户数_3 +'</td>' +   
			    '    <td>'+ formatNumber(row.总投资金额_3,2) + '</td>' +  
			    '    <td>'+ formatNumber(row.总待收_3,2)+ '</td>' +  
			    '    <td>'+ row.平均红包金额_3 + '</td>' +  
			    
			    '    <td>'+row.用户数_4_5+'</td>' +    
			    '    <td>'+ row.高净值用户数_4_5 +'</td>' +   
			    '    <td>'+ formatNumber(row.总投资金额_4_5,2) + '</td>' +  
			    '    <td>'+ formatNumber(row.总待收_4_5,2)+ '</td>' +  
			    '    <td>'+ row.平均红包金额_4_5 + '</td>' +  
        
			    '</tr>'; 
	}
	
	$("#dapan_table").html( getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]));
}
function getLastWeekHuanbi(lastweek, curr){
	var html = '';
    html += '<tr>' +   
    '	 <td>与上周环比</td>' +    
    '    <td>'+ formatNumber((curr.用户数-lastweek.用户数)*100/lastweek.用户数,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.高净值用户数_全量-lastweek.高净值用户数_全量)*100/lastweek.高净值用户数_全量,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总投资金额_全量-lastweek.总投资金额_全量)*100/lastweek.总投资金额_全量,2) +'%</td>' +  
    '    <td>'+ formatNumber((curr.总待收_全量-lastweek.总待收_全量)*100/lastweek.总待收_全量,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均红包金额_全量-lastweek.平均红包金额_全量)*100/lastweek.平均红包金额_全量,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.用户数_1-lastweek.用户数_1)*100/lastweek.用户数_1,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.高净值用户数_1-lastweek.高净值用户数_1)*100/lastweek.高净值用户数_1,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总投资金额_1-lastweek.总投资金额_1)*100/lastweek.总投资金额_1,2) +'%</td>' +  
    '    <td>'+ formatNumber((curr.总待收_1-lastweek.总待收_1)*100/lastweek.总待收_1,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均红包金额_1-lastweek.平均红包金额_1)*100/lastweek.平均红包金额_1,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.用户数_2-lastweek.用户数_2)*100/lastweek.用户数_2,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.高净值用户数_2-lastweek.高净值用户数_2)*100/lastweek.高净值用户数_2,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总投资金额_2-lastweek.总投资金额_2)*100/lastweek.总投资金额_2,2) +'%</td>' +  
    '    <td>'+ formatNumber((curr.总待收_2-lastweek.总待收_2)*100/lastweek.总待收_2,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均红包金额_2-lastweek.平均红包金额_2)*100/lastweek.平均红包金额_2,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.用户数_3-lastweek.用户数_3)*100/lastweek.用户数_3,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.高净值用户数_3-lastweek.高净值用户数_3)*100/lastweek.高净值用户数_3,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总投资金额_3-lastweek.总投资金额_3)*100/lastweek.总投资金额_3,2) +'%</td>' +  
    '    <td>'+ formatNumber((curr.总待收_3-lastweek.总待收_3)*100/lastweek.总待收_3,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均红包金额_3-lastweek.平均红包金额_3)*100/lastweek.平均红包金额_3,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.用户数_4_5-lastweek.用户数_4_5)*100/lastweek.用户数_4_5,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.高净值用户数_4_5-lastweek.高净值用户数_4_5)*100/lastweek.高净值用户数_4_5,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.总投资金额_4_5-lastweek.总投资金额_4_5)*100/lastweek.总投资金额_4_5,2) +'%</td>' +  
    '    <td>'+ formatNumber((curr.总待收_4_5-lastweek.总待收_4_5)*100/lastweek.总待收_4_5,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平均红包金额_4_5-lastweek.平均红包金额_4_5)*100/lastweek.平均红包金额_4_5,2) +'%</td>' +  
   
    
    '</tr>'; 
    return html;
}

function getTableHead(){
	var html = '';
	html += ' <tr> ' +
	' <th rowspan="2">日期</th> ' +
	' <th colspan="5">全量（≤5）</th> ' +
	' <th colspan="5">投资次数：1</th> ' +
	' <th colspan="5">投资次数：2</th> ' +
	' <th colspan="5">投资次数：3</th> ' +
	' <th colspan="5">投资次数：[4,5]</th> ' +
	' </tr> ' +
	' <tr>   ' +
	
	' <th>用户数</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>总投资金额</th> ' +
	' <th>总待收</th> ' +  
	' <th>平均红包金额</th>  ' +
	
	' <th>用户数</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>总投资金额</th> ' +
	' <th>总待收</th> ' +  
	' <th>平均红包金额</th>  ' +
	
	' <th>用户数</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>总投资金额</th> ' +
	' <th>总待收</th> ' +  
	' <th>平均红包金额</th>  ' +
	
	' <th>用户数</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>总投资金额</th> ' +
	' <th>总待收</th> ' +  
	' <th>平均红包金额</th>  ' +
	
	' <th>用户数</th> ' +  
	' <th>高净值用户</th>  ' +
	' <th>总投资金额</th> ' +
	' <th>总待收</th> ' +  
	' <th>平均红包金额</th>  ' +

		' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}