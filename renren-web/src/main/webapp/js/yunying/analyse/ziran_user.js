$(function () {
	initTimeCond();
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
		    url: "../analyse/queryZiranUserAnalyse",
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	loaded();
		    	buildTable(msg.data_list);
		    	
		    }
	 });

}

function buildTable(data_list){
	var html = '';
	
	for (var i = 0; i < data_list.length; i++) {
		var row = data_list[i];
        html += '<tr>' +   
			    '	 <td>'+row.日期+'</td>' +    
			    
			    '    <td>'+row.平台总投资用户+'</td>' +    
			    '    <td>'+ formatNumber(row.平台总待收金额,2) +'</td>' +   
			    '    <td>'+ formatNumber(row.平台红包使用总金额,2) + '</td>' +  
        
			    '    <td>'+row.自然用户数+'</td>' +    
			    '    <td>'+formatNumber(row.占比_自然用户数*100,2)+'%</td>' +   
			    
			    '    <td>'+formatNumber(row.自然用户待收,2)+'</td>' +    
			    '    <td>'+formatNumber(row.占比_自然用户待收*100,2)+'%</td>' +   
			    
			    '    <td>'+ formatNumber(row.自然用户红包使用,2) +'</td>' +    
			    '    <td>'+ formatNumber(row.占比_自然用户红包使用*100,2) + '%</td>' +  		
			    
			    '    <td>'+row.自然用户项目平均投资期限+'</td>' +    
			    '</tr>'; 
	}
	
	$("#dapan_table").html( getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]));
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

function getTableHead(){
	var html = '';
	html += ' <tr> ' +
	' <th rowspan="2">日期</th> ' +
	' <th rowspan="2">平台总投资用户</th> ' +
	' <th rowspan="2">平台总待收金额</th> ' +
	' <th rowspan="2">平台红包使用总金额</th> ' +
	' <th colspan="2">自然用户数</th> ' +
	' <th colspan="2">自然用户待收</th> ' +
	' <th colspan="2">自然用户红包使用</th> ' +
	' <th rowspan="2">自然用户项目平均投资期限</th> ' +
	' </tr> ' +
	' <tr>   ' +
	' <th>自然用户数</th> ' +  
	' <th>占比</th>  ' +
	' <th>自然用户待收</th> ' +
	' <th>占比</th> ' +  
	' <th>自然用户红包使用</th>  ' +
	' <th>占比</th>   ' +
		
		' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}