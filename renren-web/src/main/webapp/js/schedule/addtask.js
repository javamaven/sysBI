//var taskType = {
//	1:'CHANNEL_STFT',
//	2:'CHANNEL_LOSS',
//	3:'CHANNEL_INVEST_TIMES',
//	4:'CHANNEL_RENEW',
//	5:'USER_ACTIVE_INFO',
//	6:'USER_INVEST_INFO'
//	7: market_channel
//	8: channel_all
//	9: user_behavior
//	10: licai_plan
//	11: every_day_basic_data
//	12: every_day_acc_transfer
//	13: every_day_await_data
//	14: every_day_get_cash
//	15: every_day_recover_data
//  16:Project_parameter
//  17:Project_parameter_sum
//  18:Depository_total
//  19:performance_his
//  20:performance_parameter
//  21:ddz_user
//  22:channel_cost
//  23:vip_user
//  24:register_not_invest
//  25:register_not_invest_day
//  26:register_not_invest_day
//  27.old_data
//  28.Monthly_Report
//	29.Daily_Report_data
//  30.vip_pot
//  31.phone_sale_invited_send 注册5天未投资(邀请渠道)
//	32.phone_sale_cps_send注册7天未投资(CPS渠道)
//	33.phone_sale_pay_send注册7天未投资(付费渠道)
//};
function addTask(taskType) {
	var task_name = $("#task_name").val();
	if (!task_name) {
		alert('任务名称不能为空');
		return;
	}
//	var send_rate = $("#send_rate").val();
//	if (!send_rate) {
//		alert('调度频率不能为空');
//		return;
//	}
	var sendRate1 = $("#sendRate1").val();
	var sendRate2 = $("#sendRate2").val();
	var checked = $('input:radio[name="Fruit"]:checked').val();
	var send_rate = '';
	if(checked == 'time'){
		send_rate = sendRate1;
	}else if(checked == 'crontab'){
		send_rate = sendRate2;
	}
	var receive_email = $("#receive_email").val();
	if (!receive_email) {
		alert('收件人不能为空');
		return;
	}
	if(receive_email.indexOf(',') > -1){
		receive_email = receive_email.split(",");
		for (var i = 0; i < receive_email.length; i++) {
			var email = receive_email[i];
			if(!isEmail(email)){
				alert('收件人邮箱地址不正确');
				return;
			}
		}
	}else{
		if(!isEmail(receive_email)){
			alert('收件人邮箱地址不正确');
			return;
		}
	}
	
	var chaosong_email = $("#chaosong_email").val();
	if(chaosong_email && chaosong_email.indexOf(',')){
		chaosong_email = chaosong_email.split(",");
		for (var i = 0; i < chaosong_email.length; i++) {
			var email = chaosong_email[i];
			if(!isEmail(email)){
				alert('抄送人邮箱地址不正确');
				return;
			}
		}
	}else if(chaosong_email){
		if(!isEmail(chaosong_email)){
			alert('抄送人邮箱地址不正确');
			return;
		}
	}
	
	var params = getParams();
	var date_offset_num = $("#date_offset_num").val();
	var date_offset = $("#date_offset").val();
	if(date_offset_num){
		params['date_offset_num'] = date_offset_num + "-" + date_offset;
	}
	var task = {
		'taskName' : task_name,
		'sendType' : $("#send_type").val(),
		'taskType' : taskType,
		'sendRate' : send_rate,
		'condition': params,
		'receiveEmail' :receive_email,
		'isRunning' : 0,
		'chaosong_email' : $("#chaosong_email").val()
	};
	var url = "../schedule/schedulereporttask/addTask"; 
	if(taskType == 10 || taskType == 11 || taskType == 12 || taskType == 13 || taskType == 14 || taskType == 15
			|| taskType == 24 || taskType == 25 || taskType == 26|| taskType == 27|| taskType == 29||taskType == 30
			|| taskType == 31 || taskType == 32|| taskType == 33 ){
		url = "../../schedule/schedulereporttask/addTask"; 
	}
	$.ajax({
		type : "POST",
		url : url,
		data : JSON.stringify(task),
		contentType : "application/json;charset=utf-8",
		success : function(msg) {
			if (msg.code == 0 && msg.total== 0) {
				alert('新增任务成功!');
				$('#schedule_add_task').modal('hide')
			}else if(msg.code == 0 && msg.cron == 'error'){
				alert('Cron表达式不正确！');
			}else if(msg.code == 0 && msg.total > 0){
				alert('任务名字已经存在!');
			}else{
				alert('新增任务失败!');
			}
		}
	});
}

function openTaskWin(){
	var params = getParams();
	params = JSON.stringify(params);
	params = jsonFormat(params);
	$("#query_params").val(params);
}


var currSelectType = 'one';
function selectType(type){
	currSelectType = type;
	$("#crontab_text").html('');
	$("#last_five_time").html('');
	if(type == 'one'){
		$("#one_time_cond1").show();
		$("#one_time_cond2").hide();
		$("#hour_cond").hide();
		$("#day_cond").hide();
		$("#week_cond").hide();
		$("#week_list_cond").hide();
		$("#month_cond").hide();
	}else if(type == 'hour'){
		$("#one_time_cond1").hide();
//		$("#one_time_cond2").show();
		showTime();
		$("#hour_cond").show();
		$("#day_cond").hide();
		$("#week_cond").hide();
		$("#week_list_cond").hide();
		$("#month_cond").hide();
	}else if(type == 'day'){
		$("#one_time_cond1").hide();
//		$("#one_time_cond2").show();
		showTime();
		$("#hour_cond").hide();
		$("#day_cond").show();
		$("#week_cond").hide();
		$("#week_list_cond").hide();
		$("#month_cond").hide();
	}else if(type == 'week'){
		$("#one_time_cond1").hide();
//		$("#one_time_cond2").show();
		showTime();
		$("#hour_cond").hide();
		$("#day_cond").hide();
		$("#week_cond").show();
		$("#week_list_cond").show();
		$("#month_cond").hide();
	}else if(type == 'month'){
		$("#one_time_cond1").hide();
//		$("#one_time_cond2").show();
		showTime();
		$("#hour_cond").hide();
		$("#day_cond").hide();
		$("#week_cond").hide();
		$("#week_list_cond").hide();
		$("#month_cond").show();
	}
}

function showTime(){
	$("#one_time_cond2").show();
	$("#one_time_cond2").css("display","inline");
}
/**
 * 验证一下
 * @returns
 */
function validateCron(type){
	var params = {};
	var crontab = '';
	if('one' == currSelectType){
		var one_time1 = $("#one_time1").val();
		if(one_time1){//2017-06-06 09:00
			var year = one_time1.substring(0, 4);
			var month = one_time1.substring(5, 7);
			var day = one_time1.substring(8, 10);
			var hour = one_time1.substring(11, 13);
			var minute = one_time1.substring(14, 16);
			crontab = '0 '+minute+' '+hour+' '+day+' '+month+' ? ' + year;
			params.date = crontab;
			queryNextFiveRunTime(params, type);
		}else{
			alert('请先选择执行时间')
			return;
		}
	}else if('hour' == currSelectType){
		var one_time2 = $("#one_time2").val();
		var hour_cond_value = $("#hour_cond_value").val();
		if(one_time2){//09:00
			var hour = one_time2.substring(0, 2);
			var minute = one_time2.substring(3, 5);
			if(hour_cond_value && hour_cond_value>0){
				crontab = '0 '+minute+' '+hour+'/'+hour_cond_value+' * * ? ';
			}else{
				crontab = '0 '+minute+' '+hour+' * * ? ';
			}
			params.date = crontab;
			queryNextFiveRunTime(params, type);
		}else{
			alert('请先选择执行时间')
			return;
		}
	}else if('day' == currSelectType){
		var one_time2 = $("#one_time2").val();
		var day_cond_value = $("#day_cond_value").val();
		if(one_time2){//09:00
			var hour = one_time2.substring(0, 2);
			var minute = one_time2.substring(3, 5);
			if(day_cond_value && day_cond_value>0){
				crontab = '0 '+minute+' '+hour+' */'+day_cond_value+' * ? ';
			}else{
				crontab = '0 '+minute+' '+hour+' * * ? ';
			}
			params.date = crontab;
			queryNextFiveRunTime(params, type);
		}else{
			alert('请先选择执行时间')
			return;
		}
	}else if('week' == currSelectType){
		var one_time2 = $("#one_time2").val();
		if(one_time2){//09:00
			var hour = one_time2.substring(0, 2);
			var minute = one_time2.substring(3, 5);
			var week = getWeeks();
			if(week.length > 0){
				crontab = '0 '+minute+' '+hour+' ? * ' + week;
			}else{
				crontab = '0 '+minute+' '+hour+' * * ?';
			}
			params.date = crontab;
			queryNextFiveRunTime(params, type);
		}else{
			alert('请先选择执行时间')
			return;
		}
	}else if('month' == currSelectType){
		var one_time2 = $("#one_time2").val();
		if(one_time2){//09:00
			var hour = one_time2.substring(0, 2);
			var minute = one_time2.substring(3, 5);
			var days = getMonthDays();
			if(days.length > 0){
				crontab = '0 '+minute+' '+hour+' '+days+' * ?';
			}else{
				crontab = '0 '+minute+' '+hour+' * * ?';
			}
			params.date = crontab;
			queryNextFiveRunTime(params, type);
		}else{
			alert('请先选择执行时间')
			return;
		}
	}
	$("#crontab_text").html(crontab);
}
/**
 * 点击确定按钮，保存当前的crontab表达式
 * @returns
 */
function setCrontabValue(type){
	validateCron(type);
	$("#sendRate1").val($("#crontab_text").html());
	$("#sendRate2").val($("#crontab_text").html());
}
function getWeeks(){
	var ret = '';
	var items = document.getElementsByName("category"); 
	for (i = 0; i < items.length; i++) {                    
		if (items[i].checked) {                        
			ret += items[i].value + ",";                  
		}                
	}
	if(ret.length > 1){
		ret = ret.substring(0 ,ret.length-1);
	}
	return ret;
}
function getMonthDays(){
	var ret = '';
	var items = document.getElementsByName("month_day"); 
	for (i = 0; i < items.length; i++) {                    
		if (items[i].checked) {                        
			ret += items[i].value + ",";                  
		}                
	}
	if(ret.length > 1){
		ret = ret.substring(0 ,ret.length-1);
	}
	return ret;
}
function queryNextFiveRunTime(params, type){
	var url = "../schedule/schedulereporttask/getRunTime";
	if(type && type == 'yunying'){
		url = "../../schedule/schedulereporttask/getRunTime";
	}
	params.selectType = currSelectType;
	$.ajax({
		type: "POST",
		async: false,
		url: url,
		data: JSON.stringify(params),
		contentType: "application/json;charset=utf-8",
	    success : function(msg) {
	    	var html = '';
	    	for (var i = 0; i < msg.data.length; i++) {
				var date = msg.data[i];
				html += '<font color="green">&nbsp;&nbsp;&nbsp;'+(i+1) + '：' + date + '</font><br/>';
			}
	    	if(msg.data.length == 0){
	    		html = '<font color="red">永远不执行，请检查时间设置是否合理</font>';
	    	}
	    	$("#last_five_time").html(html);
	    }
	});
}

function print(obj){
	for(var key in obj){
		alert(key + " = " + obj[key])
	}
}

var isCheckCrontab;
function initEvent(){
	$(":radio").click(function(){
		var type = $(this).val();
		if(type == 'crontab'){
			$("#sendRate2").show();
			$("#sendRate1").hide();
			$("#edit_btn_div").hide();
		}else if(type == 'time'){
			$("#sendRate1").show();
			$("#sendRate2").hide();
			$("#edit_btn_div").show();
		}
	});
	
	initMonthDayList();//将月份的31天选择生成
	
	$("#one_time1").datetimepicker({
		language: 'zh-CN',
		autoclose:true
	}).on("click",function(){
	});
	
	$("#one_time2").datetimepicker({
		format: 'hh:ii',
		startView: 'day',
		maxView: 'day',
		minView: 'hour',
		language: 'zh-CN',
		autoclose:true
	}).on("click",function(){
	});
}


function initMonthDayList(){
	var html = '';
	for (var i = 0; i < 31; i++) {
		html += '<input type="checkbox" name="month_day" value="'+(i+1)+'" />'+(i+1)+'&nbsp;&nbsp;';
	}
	$("#month_cond").html(html);
	
}