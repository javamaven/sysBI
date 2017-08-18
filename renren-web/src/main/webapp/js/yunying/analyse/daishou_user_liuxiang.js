$(function () {
	initTimeCond();
	initExportFunction();
	queryDaishouUserLiuxiang();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var day =  $("#stat_period").val();
		if(!day){
			alert('请先选择查询日期');
			return;
		}
		var params = getParams();
		executePost('../analyse/exportZiranUser', {'params' : JSON.stringify(params)});  
	});

}


function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#stat_period").val('2017-08-13');
    
}

function queryDaishouUserLiuxiang(){
	var day =  $("#stat_period").val();
	if(!day){
		alert('请先选择查询日期');
		return;
	}
	loading();
	 $.ajax({
		    type: "POST",
		    url: "../analyse/queryDaishouUserLiuxiang",
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	buildTable(msg.data_map);
		    	
		    }
	 });

}

function buildTable(data_map){
	var statPeriod = data_map.statPeriod;
	var weekAgo = data_map.weekAgo;
	var html = '';
	//第一行
	html += '<tr><td rowspan="6">高净值用户</td>'+
	'	 <td rowspan="6">'+data_map.高净值_留存用户+'</td>'+
	'	 <td rowspan="6">'+ formatNumber(data_map.高净值_留存_上周待收,2) +'</td>' + 
	'	 <td rowspan="6">'+ formatNumber(data_map.高净值_留存_本周待收,2) + '</td>' +  
    '    <td rowspan="6">'+ formatNumber(data_map.高净值_留存_净增待收,2) + '</td>' +  

    '    <td>总增量 流入</td>' +   
    '    <td>'+data_map.高净值_总_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_总_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_总_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_总_流入.流入净待收,2)+'</td>' +    
    
    '    <td>总减量 流出</td>' +   
    '    <td>'+data_map.高净值_总_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_总_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_总_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_总_流出.流出净待收,2)+'</td>' +    
    
    '    <td rowspan="6">'+ (data_map.高净值_总_流入.人数-data_map.高净值_总_流出.人数) +'</td>' +    
    '    <td rowspan="6">'+ formatNumber((data_map.高净值_总_流入.流入净待收 + data_map.高净值_总_流出.流出净待收),2) +'</td>' +    
    '</tr>'; 
	//第2行
	html += '<tr>' +   
    '    <td>成熟用户 流入</td>' +   
    '    <td>'+data_map.高净值_成熟_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_成熟_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_成熟_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_成熟_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 成熟用户 </td>' +   
    '    <td>'+data_map.高净值_成熟_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_成熟_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_成熟_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_成熟_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	
	//第3行
	html += '<tr>' +   
    '    <td>0待收 成熟用户  流入</td>' +   
    '    <td>'+data_map.高净值_0待收_成熟_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_成熟_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_成熟_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_成熟_流入.流入净待收,2)+'</td>' +    
    '    <td> 流出新用户  </td>' +   
    '    <td>'+data_map.高净值_新_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_新_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_新_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_新_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	//第4行
	html += '<tr>' +   
    '    <td>新用户 流入</td>' +   
    '    <td>'+data_map.高净值_新_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_新_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_新_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_新_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 0待收的成熟用户（提现完） </td>' +   
    '    <td>'+data_map.高净值_0待收_成熟_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_成熟_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_成熟_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_成熟_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	//第5行
	html += '<tr>' +   
    '    <td>0待收 新用户 流入 </td>' +   
    '    <td>'+data_map.高净值_0待收_新_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_新_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_新_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_新_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 0待收的新用户（提现完） </td>' +   
    '    <td>'+data_map.高净值_0待收_新_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_新_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_新_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_0待收_新_流出.流出净待收,2)+'</td>' +    
    '</tr>';
	//第6行
	html += '<tr>' +   
    '    <td>'+weekAgo+' 后发生首次投资用户 流入</td>' +   
    '    <td>'+data_map.高净值_首投_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_首投_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_首投_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.高净值_首投_流入.流入净待收,2)+'</td>' +    
    '    <td></td>' +   
    '    <td></td>' +   
    '    <td></td>' +   
    '    <td></td>' +   
    '    <td></td>' +   
    '</tr>'; 
	//第7行 --新用户
	html += '<tr><td rowspan="4">新用户</td>'+
	'	 <td rowspan="4">'+data_map.新_留存用户+'</td>'+
	'	 <td rowspan="4">'+ formatNumber(data_map.新_留存_上周待收,2) +'</td>' + 
	'	 <td rowspan="4">'+ formatNumber(data_map.新_留存_本周待收,2) + '</td>' +  
    '    <td rowspan="4">'+ formatNumber(data_map.新_留存_净增待收,2) + '</td>' +  

    '    <td>总增量 流入</td>' +   
    '    <td>'+data_map.新_总_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_总_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_总_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_总_流入.流入净待收,2)+'</td>' +    
    
    '    <td>总减量 流出</td>' +   
    '    <td>'+data_map.新_总_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_总_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_总_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_总_流出.流出净待收,2)+'</td>' +    
    
    '    <td rowspan="4">'+ (data_map.新_总_流入.人数-data_map.新_总_流出.人数) +'</td>' +    
    '    <td rowspan="4">'+ formatNumber((data_map.新_总_流入.流入净待收 + data_map.新_总_流出.流出净待收),2) +'</td>' +    
    '</tr>'; 
	//第8行 --新用户
	html += '<tr>'+
    '    <td>高净值用户 流入</td>' +   
    '    <td>'+data_map.新_高净值_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_高净值_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_高净值_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_高净值_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 成熟用户</td>' +   
    '    <td>'+data_map.新_成熟_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_成熟_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_成熟_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_成熟_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	//第9行 --新用户
	html += '<tr>'+
    '    <td>'+weekAgo+' 后发生首次投资用户 流入</td>' +   
    '    <td>'+data_map.新_首投_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_首投_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_首投_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_首投_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 高净值用户 </td>' +   
    '    <td>'+data_map.新_高净值_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_高净值_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_高净值_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_高净值_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	//第10行 --新用户
	html += '<tr>'+
    '    <td>0待收新用户 流入 </td>' +   
    '    <td>'+data_map.新_0待收_新_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_0待收_新_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_0待收_新_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_0待收_新_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 0待收的新用户（提现完）</td>' +   
    '    <td>'+data_map.新_0待收_新_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.新_0待收_新_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_0待收_新_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.新_0待收_新_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	
	//第11行 --成熟用户
	html += '<tr><td rowspan="6">成熟用户</td>'+
	'	 <td rowspan="6">'+data_map.成熟_留存用户+'</td>'+
	'	 <td rowspan="6">'+ formatNumber(data_map.成熟_留存_上周待收,2) +'</td>' + 
	'	 <td rowspan="6">'+ formatNumber(data_map.成熟_留存_本周待收,2) + '</td>' +  
    '    <td rowspan="6">'+ formatNumber(data_map.成熟_留存_净增待收,2) + '</td>' +  

    '    <td>总增量 流入</td>' +   
    '    <td>'+data_map.成熟_总_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_总_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_总_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_总_流入.流入净待收,2)+'</td>' +    
    
    '    <td>总减量 流出</td>' +   
    '    <td>'+data_map.成熟_总_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_总_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_总_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_总_流出.流出净待收,2)+'</td>' +    
    
    '    <td rowspan="6">'+ (data_map.成熟_总_流入.人数-data_map.成熟_总_流出.人数) +'</td>' +    
    '    <td rowspan="6">'+ formatNumber((data_map.成熟_总_流入.流入净待收 + data_map.成熟_总_流出.流出净待收),2) +'</td>' +    
    '</tr>'; 
	//第12行
	html += '<tr>' +   
    '    <td>新用户 流入</td>' +   
    '    <td>'+data_map.成熟_新_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_新_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_新_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_新_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 高净值用户 </td>' +   
    '    <td>'+data_map.成熟_高净值_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_高净值_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_高净值_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_高净值_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	
	//第13行
	html += '<tr>' +   
    '    <td>高净值用户 流入</td>' +   
    '    <td>'+data_map.成熟_高净值_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_高净值_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_高净值_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_高净值_流入.流入净待收,2)+'</td>' +    
    '    <td>流出 0待收的成熟用户（提现完）</td>' +   
    '    <td>'+data_map.成熟_0待收_成熟_流出.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_成熟_流出.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_成熟_流出.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_成熟_流出.流出净待收,2)+'</td>' +    
    '</tr>'; 
	//第14行
	html += '<tr>' +   
    '    <td>'+weekAgo+' 后发生首次投资用户 流入</td>' +   
    '    <td>'+data_map.成熟_首投_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_首投_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_首投_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_首投_流入.流入净待收,2)+'</td>' +    
    '    <td></td>' +  
    '    <td></td>' +  
    '    <td></td>' +  
    '    <td></td>' +  
    '    <td></td>' +  
    '</tr>'; 
	//第15行
	html += '<tr>' +   
    '    <td>0待收 新用户 流入 </td>' +   
    '    <td>'+data_map.成熟_0待收_新_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_新_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_新_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_新_流入.流入净待收,2)+'</td>' +    
    '    <td></td>' +  
    '    <td></td>' +  
    '    <td></td>' +  
    '    <td></td>' +  
    '    <td></td>' +     
    '</tr>';
	//第16行
	html += '<tr>' +   
    '    <td>0待收 成熟用户流入</td>' +   
    '    <td>'+data_map.成熟_0待收_成熟_流入.人数+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_成熟_流入.本周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_成熟_流入.上周待收,2)+'</td>' +    
    '    <td>'+formatNumber(data_map.成熟_0待收_成熟_流入.流入净待收,2)+'</td>' +    
    '    <td></td>' +   
    '    <td></td>' +   
    '    <td></td>' +   
    '    <td></td>' +   
    '    <td></td>' +   
    '</tr>';
	$("#dapan_table").html( getTableHead(data_map) + html);
}
function getLastWeekHuanbi(lastweek, curr){
	var html = '';
    html += '<tr>' +   
    '	 <td>与上周环比</td>' +    
    '    <td>'+ formatNumber((curr.平台总投资用户-lastweek.平台总投资用户)*100/lastweek.平台总投资用户,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平台总待收金额-lastweek.平台总待收金额)*100/lastweek.平台总待收金额,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.平台红包使用总金额-lastweek.平台红包使用总金额)*100/lastweek.平台红包使用总金额,2) +'%</td>' +  
   
    '    <td>'+ formatNumber((curr.自然用户数-lastweek.自然用户数)*100/lastweek.自然用户数,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.占比_自然用户数-lastweek.占比_自然用户数)*100/lastweek.占比_自然用户数,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.自然用户待收-lastweek.自然用户待收)*100/lastweek.自然用户待收,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.占比_自然用户待收-lastweek.占比_自然用户待收)*100/lastweek.占比_自然用户待收,2) +'%</td>' +    
    '    <td>'+ formatNumber((curr.自然用户红包使用-lastweek.自然用户红包使用)*100/lastweek.自然用户红包使用,2) +'%</td>' +   
    '    <td>'+ formatNumber((curr.占比_自然用户红包使用-lastweek.占比_自然用户红包使用)*100/lastweek.占比_自然用户红包使用,2) +'%</td>' +  
    
    '    <td>'+ formatNumber((curr.自然用户项目平均投资期限-lastweek.自然用户项目平均投资期限)*100/lastweek.自然用户项目平均投资期限,2) +'%</td>' +    
    
    '</tr>'; 
    return html;
}

function getTableHead(data_map){
	var statPeriod = data_map.statPeriod;
	var weekAgo = data_map.weekAgo;
	var html = '';
	html += ' <tr> ' ;
	html += ' <th colspan="17">'+statPeriod+' 当期有待收资金用户分布（待收>0）<th> ' ;
	html += ' </tr> ';
	
	
	html += ' <tr> ' +
	' <th rowspan="2">类别</th> ' +
	' <th rowspan="2">'+weekAgo+' 留存用户</th> ' +
	' <th rowspan="2">'+weekAgo+' 待收资金</th> ' +
	' <th rowspan="2">'+statPeriod+' 待收资金</th> ' +
	' <th rowspan="2">留存用户净增待收</th> ' +
	
	' <th colspan="5">'+statPeriod+' 增量流入 （待收>0）</th> ' +
	' <th colspan="5">'+weekAgo+' 减量流出 </th> ' +
	
	' <th rowspan="2">净增用户</th> ' +
	' <th rowspan="2">净增待收</th> ' +
	' </tr> ';
	
	html += ' <tr> ' +
	' <th>层级</th> ' +  
	' <th>人数</th>  ' +
	' <th>'+statPeriod+' 待收</th> ' +
	' <th>'+weekAgo+' 待收</th> ' +
	' <th>流入净待收</th> ' +
	' <th>层级</th> ' +  
	' <th>人数</th>  ' +
	' <th>'+weekAgo+' 待收</th> ' +
	' <th>'+statPeriod+' 待收</th> ' +
	' <th>流出净待收</th> ' +  
	' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}