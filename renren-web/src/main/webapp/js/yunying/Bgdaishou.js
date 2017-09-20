$(function(){
	ajaxQuery();
});


function showList(o) {
    hideList("dropdown-content" + o.id);
//     document.getElementById("dropdown-" + o.id).classList.toggle("show");
//     $("#" + o.id).html('标题' + o.id + '-');
//     $("#" + o.id).html('标题' + o.id + '-');
	var html =  $("#" + o.id).html();
    $("#dropdown-" + o.id).toggle();
    if(o.id==a){
    if(html.indexOf("+") > -1){//有加号
    	$("#" + o.id).html("累计待收" + " -");
    }else{
    	$("#" + o.id).html("累计待收" + " +");
    }
    }
    
    ajaxQuery(o.id);
}
function getParams(){
	var params = {};
	params.startDay = '2017-08-25';
	params.endDay = '2017-08-30';
	return params;
}
function ajaxQuery(){
	var str = '';
    var i = 0;
    var params = getParams();
    $.ajax({
        type: "POST",
        url: "../yunying/Bgdaishou/list",
        data: JSON.stringify(params),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
        	msg = eval('(' + msg + ')');
        	var dataList = msg.data;
        	var html = "";
			for (var i = 0;i < dataList.length; i++){
				var dayData = dataList[i];//每天5条的数据全部在里面
				html += buildline(dayData, i);
			}
        	$("#table_html").html(html);
        	$('tr.parent').click(function(){   // 获取所谓的父行
        		$(this).toggleClass("selected")   // 添加/删除高亮
        			.siblings('.child_'+this.id).toggle();  // 隐藏/显示所谓的子行
        	});
        	for (var i = 0; i < dataList.length; i++) {
        		$(".child_row_" + i).hide();
			}
        }
    });
}

function buildline(dayData, id){
	var html = "";
	for (var i = 0; i < dayData.length; i++) {
		var data = dayData[i];
		if(i == 0){
			html += '<tr class="parent" id="row_'+id+'" style="font-family:simsun ;"><td>'+data.日期+'</td><td>'+data.累计待收+'</td>'+
			'<td>'+data.新增待收+'</td><td>'+data.减少待收+'</td><td>'+data.待收投资人数+'</td></tr>';
		}else if(i == 1){
			html += '<tr class="child_row_'+id+'" ><td>'+data.日期+'</td><td>累计待收本金</td>'+
			'<td>新增待收本金</td><td>减少待收本金</td><td>各分层待收投资人数</td></tr>';
		}else if(i == 2){
			html += '<tr class="child_row_'+id+'" ><td>'+data.日期+'</td><td>'+data.累计待收+'</td>'+
			'<td>'+data.新增待收+'</td><td>'+data.减少待收+'</td><td>'+data.待收投资人数+'</td></tr>';
		}else if(i == 3){
			html += '<tr class="child_row_'+id+'" ><td>'+data.日期+'</td><td>累计待收利息</td>'+
			'<td>新增待收利息</td><td>减少待收利息</td><td>各分层待收</td></tr>';
		}else if(i == 4){
			html += '<tr class="child_row_'+id+'" ><td>'+data.日期+'</td><td>'+data.累计待收+'</td>'+
			'<td>'+data.新增待收+'</td><td>'+data.减少待收+'</td><td>'+data.待收投资人数+'</td></tr>';
		}
	}
	return html;
}