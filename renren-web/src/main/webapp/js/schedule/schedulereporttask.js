var jobType = {
	1:'渠道首投复投情况',//CHANNEL_STFT
	2:'渠道流失分析',//CHANNEL_LOSS
	3:'渠道投资次数分析',//CHANNEL_INVEST_TIMES
	4:'渠道续费数据汇总',//CHANNEL_RENEW
	5:'用户激活情况',//USER_ACTIVE_INFO
	6:'用户投资情况',//USER_INVEST_INFO
	7:'渠道负责人情况',//market_channel
	8:'渠道分次投资情况'//market_channel
};

$(function () {
   initTableGrid();
});

/**
 * 启动任务
 * @param taskId
 * @returns
 */
function startTask(taskId){
	 $.ajax({
		type : "POST",
		url : "../schedule/schedulereporttask/startTask",
		data : JSON.stringify({'id' : taskId}),
		contentType : "application/json;charset=utf-8",
		success : function(msg) {
			if(msg.code == 0){
				alert('启动任务成功!');
			}
			reload();
		}
	});

}

function reload(){
	var page = $("#jqGrid").jqGrid('getGridParam','page');
	$("#jqGrid").jqGrid('setGridParam',{ 
        page:page
    }).trigger("reloadGrid");
}

/**
 * 停止任务
 * 
 * @param taskId
 * @returns
 */
function stopTask(taskId){
	 $.ajax({
			type : "POST",
			url : "../schedule/schedulereporttask/stopTask",
			data : JSON.stringify({'id' : taskId}),
			contentType : "application/json;charset=utf-8",
			success : function(msg) {
				if(msg.code){
					alert('停止任务成功!');
				}
				reload();
			}
		});
}
/**
 * 任务日志
 * @returns
 */
function taskLog(taskId){
	 $.ajax({
			type : "POST",
			url : "../schedule/schedulereporttask/taskLog",
			data : JSON.stringify({'id' : taskId}),
			contentType : "application/json;charset=utf-8",
			success : function(msg) {
				if(msg.code){
					alert('停止任务成功!');
				}
				reload();
			}
		});
}

function initTableGrid(){
	 $("#jqGrid").jqGrid({
	        url: '../schedule/schedulereporttask/list',
	        datatype: "json",
	        colModel: [			
				{ label: '任务ID', name: 'id', index: '$ID', width: 40, key: true, align: 'right'  },
				{ label: '任务名称', name: 'taskName', index: '$TASK_NAME', width: 100, align: 'right' }, 
				{ label: '任务类型', name: 'taskType', index: '$TASK_TYPE', width: 85, align: 'right' 
					,formatter:function(cellvalue, options, rowObject){
						if(jobType[cellvalue]){
							return jobType[cellvalue];
						}
						return '';
					}  
				},					
//				{ label: '任务内容', name: 'taskConetent', index: '$TASK_CONETENT', width: 80, align: 'right'  }, 			
				{ label: '上次推送时间', name: 'lastSendTime', index: '$LAST_SEND_TIME', width: 100, align: 'right'  }, 			
				{ label: '发送方式', name: 'sendType', index: '$SEND_TYPE', width: 45, align: 'right'
					,formatter:function(cellvalue, options, rowObject){
						if(cellvalue == 'email'){
							return '邮件';
						}else{
							return '';
						}
					}  
				},
				{ label: '调度频率', name: 'sendRate', index: '$SEND_RATE', width: 70, align: 'right'  }, 			
				{ label: '查询条件', name: 'condition', index: '$CONDITION', width: 80, align: 'right'  
					,formatter:function(value, options, rowObject){
						var title = jsonFormat(value);
						var html = "<a><font color='black' style='font-weight: normal;' title='"+title+"'>"+value+"</font></a>";
						return html;
					}  
				},
				{ label: '下次执行时间', name: 'nextRunTime', width: 100, align: 'right'  }, 
				{ label: '状态', name: 'isRunning', index: '$IS_RUNNING', width: 50 , align: 'right' 
					,formatter:function(cellvalue, options, rowObject){
						var text = '';
						var btnClass = '';
						if(cellvalue == '0'){
							text = '未运行';
							btnClass = 'btn-info';
						}else if(cellvalue == '1'){
							text = '正在运行';
							btnClass = 'btn-success';
						}
						var html = '<font color="green" class="btn btn-primary '+btnClass+' btn-xs">'+text+'</font>';
						return html;
					}  
				},
				{ label: '', name: '', index: '', width: 90 , align: 'center' 	,
					formatter:function(cellvalue, options, rowObject){
						var btnText = "";
						if(rowObject && rowObject.isRunning == "1"){
							btnText = 'onclick="stopTask('+rowObject.id+')" value="停止"';
						}else{
							btnText = 'onclick="startTask('+rowObject.id+')" value="启动"';
						}
						var html = '<input type="button" class="btn btn-primary btn-inverse btn-xs" '+btnText+'/>';
						html += '  <a class="btn btn-primary btn-inverse btn-xs"  href="schedule_report_task_log.html?taskId='+rowObject.id+'">查看日志</a>';
						return html;// onclick="taskLog('+rowObject.id+')"
					} 
				}
	        ],
			viewrecords: true,
	        height: 400,
	        rowNum: 10,
			rowList : [10,30,50],
	        rownumbers: true, 
	        rownumWidth: 25, 
	        autowidth:true,
	        multiselect: true,
	        pager: "#jqGridPager",
	        jsonReader : {
	            root: "page.list",
	            page: "page.currPage",
	            total: "page.totalPage",
	            records: "page.totalCount"
	        },
	        prmNames : {
	            page:"page", 
	            rows:"limit", 
	            order: "order"
	        },
	        gridComplete:function(){
	        	//隐藏grid底部滚动条
//	        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
	        }
	    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		scheduleReportTask: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.scheduleReportTask = {'sendType' : 'email', 'sendRate' : '* * * * * ?'};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.scheduleReportTask.id == null ? "../schedule/schedulereporttask/save" : "../schedule/schedulereporttask/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.scheduleReportTask),
			    success: function(r){
			    	
					if(r.code == 0 && r.cron == 'error'){
						alert('Cron表达式不正确！');
					}else if(r.code == 0 && r.total > 0){
						alert('任务名字已经存在!');
					}else{
						if(r.code === 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../schedule/schedulereporttask/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get("../schedule/schedulereporttask/info/"+id, function(r){
                vm.scheduleReportTask = r.scheduleReportTask;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page, 
                datatype:'json', 
	            postData: {taskName: $("#task_name_cond").val()}
                
            }).trigger("reloadGrid");
		}
	}
});