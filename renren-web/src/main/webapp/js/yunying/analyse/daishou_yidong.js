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
		executePost('../analyse/exportDaishouYidong', {'params' : JSON.stringify(params)});  
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
	 $.ajax({
		    type: "POST",
		    url: "../analyse/queryDaishouYidong",
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(msg) {
		    	
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
			    
			    '    <td>'+ formatNumber(row.净待收金额,2) +'</td>' +    
			    '    <td>'+ row.用户总数 +'</td>' +   
			    '    <td>'+row.高净值用户+'</td>' +    
			    '    <td>'+row.沉默用户+'</td>' +   
			    
			    '    <td>'+row.新用户+'</td>' +    
			    '    <td>'+row.成熟用户+'</td>' +   
			    
			    '    <td>'+row.自然用户+'</td>' +   
			    '    <td>'+row.其他+'</td>' +   
			    '</tr>'; 
	}
	
	$("#dapan_table").html( getTableHead() + html + getLastWeekHuanbi(data_list[0], data_list[1]));
}
function getLastWeekHuanbi(lastweek, curr){
	var html = '';
	 html += '<tr>' +   
	    '	 <td>与上周环比</td>' +    
	    '    <td>'+ formatNumber((curr.净待收金额-lastweek.净待收金额)*100/lastweek.净待收金额,2) +'%</td>' +   
	    '    <td>'+ (curr.用户总数-lastweek.用户总数) +'</td>' +   
	    '    <td>'+ (curr.高净值用户-lastweek.高净值用户) +'</td>' + 
	    '    <td>'+ (curr.沉默用户-lastweek.沉默用户) +'</td>' + 
	    '    <td>'+ (curr.新用户-lastweek.新用户) +'</td>' +
	    '    <td>'+ (curr.成熟用户-lastweek.成熟用户) +'</td>' +
	    '    <td>'+ (curr.自然用户-lastweek.自然用户) +'</td>' +
	    '    <td>'+ (curr.其他-lastweek.其他) +'</td>' +
	    '</tr>'; 
//    html += '<tr>' +   
//    '	 <td>与上周环比</td>' +    
//    '    <td>'+ formatNumber((curr.净待收金额-lastweek.净待收金额)*100/lastweek.净待收金额,2) +'%</td>' +   
//    '    <td>'+ formatNumber((curr.用户总数-lastweek.用户总数)*100/lastweek.用户总数,2) +'%</td>' +   
//    
//    '    <td>'+ formatNumber((curr.高净值用户-lastweek.高净值用户)*100/lastweek.高净值用户,2) +'%</td>' +   
//    '    <td>'+ formatNumber((curr.沉默用户-lastweek.沉默用户)*100/lastweek.沉默用户,2) +'%</td>' +   
//    '    <td>'+ formatNumber((curr.新用户-lastweek.新用户)*100/lastweek.新用户,2) +'%</td>' +   
//    
//    '    <td>'+ formatNumber((curr.成熟用户-lastweek.成熟用户)*100/lastweek.成熟用户,2) +'%</td>' +   
//    '    <td>'+ formatNumber((curr.自然用户-lastweek.自然用户)*100/lastweek.自然用户,2) +'%</td>' +   
//    '    <td>'+ formatNumber((curr.其他-lastweek.其他)*100/lastweek.其他,2) +'%</td>' +  
//    '</tr>'; 
    return html;
}

function getTableHead(){
	var html = '';
	html += ' <tr> ' +
		' <td rowspan="2">日期</td> ' +
		' <td rowspan="2">待收金额</td> ' +
		' <td colspan="7">待收资金用户分布(待收资金>0)</td> ' +
		' </tr> ' +
		' <tr>   ' +
		' <td>用户总数</td> ' +  
		' <td>高净值用户</td> ' +
		' <td>沉默用户</td> ' +  
		' <td>新用户</td>  ' +
		' <td>成熟用户</td>   ' +
		' <td>自然用户</td>   ' +		
		' <td>其他</td>   ' +			
		' </tr> ';
	return html;
}

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}