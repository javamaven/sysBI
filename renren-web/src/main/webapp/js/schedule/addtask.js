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
//};
function addTask(taskType) {
	var task_name = $("#task_name").val();
	if (!task_name) {
		alert('任务名称不能为空');
		return;
	}
	var send_rate = $("#send_rate").val();
	if (!send_rate) {
		alert('调度频率不能为空');
		return;
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
	if(taskType == 10 || taskType == 11 || taskType == 12 || taskType == 13 || taskType == 14){
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