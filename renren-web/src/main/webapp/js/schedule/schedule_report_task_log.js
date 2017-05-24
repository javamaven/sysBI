$(function () {
	var taskId = getUrlParams("taskId");
	$("#task_id").val(taskId);
	initTableGrid();
});

function getUrlParams(key) {
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

function initTableGrid(){
	 $("#jqGrid").jqGrid({
	        url: '../schedule/schedulereporttasklog/list',
	        datatype: "json",
	        postData: {'taskId': getUrlParams("taskId")},
	        colModel: [			
//				{ label: 'id', name: 'id', index: '$ID', width: 50, key: true },
				{ label: '任务ID', name: 'taskId', index: '$TASK_ID', width: 40, align: 'right' }, 			
				{ label: '耗时(毫秒)', name: 'timeCost', index: '$TIME_COST', width: 45, align: 'right' }, 			
				{ label: '发送结果', name: 'sendResult', index: '$SEND_RESULT', width: 40, align: 'right' 
					,formatter:function(value, options, row){
						return value == 'success' ? 
								'<span class="label label-success">SUCCESS</span>' :
								'<span class="label label-danger pointer" onclick="vm.showError('+row.logId+')">FAIL</span>';
					}  
				},		
				{ label: '查询参数', name: 'params', index: '$PARAMS', width: 80, align: 'right' }, 			
				{ label: '收件人', name: 'receiveEmal', index: '$RECEIVE_EMAL', width: 80, align: 'right' }, 			
				{ label: '抄送人', name: 'chaosongEmail', index: '$CHAOSONG_EMAIL', width: 80 , align: 'right'}, 			
				{ label: '邮件内容', name: 'emailValue', index: '$EMAIL_VALUE', width: 80 , align: 'right'
					,formatter:function(value, options, row){
						return value;
//						return '<a href="http://localhost:8080/bi-sys/attach-temp/a.txt">a.txt</a>';
					}  
				},		
				{ label: '发送邮件结束时间', name: 'time', index: '$TIME', width: 80 , align: 'right'}, 			
				{ label: '备注', name: 'desc', index: '$DESC', width: 80, align: 'right' }			
	        ],
			viewrecords: true,
	        height: 385,
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
	        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
	        }
	    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			jobId: null
		}
	},
	methods: {
		query: function () {
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:{'taskId': $("#task_id").val()},
                page:1 
            }).trigger("reloadGrid");
		},
		showError: function(logId) {
			$.get("../sys/scheduleLog/info/"+logId, function(r){
				parent.layer.open({
				  title:'失败信息',
				  closeBtn:0,
				  content: r.log.error
				});
			});
		},
		back: function (event) {
			history.go(-1);
		}
	}
});
