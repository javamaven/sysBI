$(function () {
	loadChannel();
	initTimeCond();
	initExportFunction();
    initTableGrid();
//    queryTotalInfo();
    initEvent();
});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportinvestmentdaily/exportExcel', {'params' : JSON.stringify(params)});  
	});

}
function initTimeCond(){
    $("#start_invest_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#end_invest_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
}
function queryTotalInfo(){
	var params = getParams();
	 $.ajax({
	        type: "POST",
	        url: "../dmreportinvestmentdaily/totalList",
	        async: true,
	        data: JSON.stringify(params),
	        contentType: "application/json;charset=utf-8",
	        success : function(retData) {
	        	var data = retData.data;
	        	$("#total_invest_amount").html(formatNumber(data.tenderCapital, 2));
	        	$("#curr_have_amount").html(formatNumber(data.recoverAccountWait, 2));
	        }
	     });
	 
}
function initTableGrid(){
	$("#jqGrid").jqGrid({
//        url: '../dmreportinvestmentdaily/list',
        datatype: "json",
        mtype: 'GET',
        postData: {'statPeriod': getYesterday()},
        colModel: [			
//			{ label: '统计日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '用户ID', name: 'userId', index: '$USER_ID', width: 80,align:'right' }, 			
			{ label: '用户名称', name: 'username', index: '$USERNAME', width: 100,align:'right' }, 			
			{ label: '渠道ID', name: 'channelId', index: '$CHANNEL_ID', width: 80,align:'right' }, 			
			{ label: '渠道名称', name: 'channelName', index: '$CHANNEL_NAME', width: 120,align:'right' }, 			
			{ label: '渠道标记', name: 'activityTag', index: '$ACTIVITY_TAG', width: 80,align:'right' }, 			
			{ label: '操作平台', name: 'tenderFrom', index: '$TENDER_FROM', width: 80,align:'right'
//				,
//				formatter:function(cellvalue, options, rowObject){
//					if(cellvalue == 0){
//						return '移动端无法区分';
//					}else if(cellvalue == 1){
//						return 'PC';
//					}else if(cellvalue == 2){
//						return 'IOS';
//					}else if(cellvalue == 3){
//						return 'Android';
//					}else if(cellvalue == 4){
//						return 'M站';
//					}else if(cellvalue == 5){
//						return '微信或后台';
//					}else if(cellvalue == -1){
//						return '系统';
//					}
//				}
			},			
			{ label: '投资时间', name: 'addtime', index: '$ADDTIME', width: 150 ,align:'right'}, 			
			{ label: '涉及项目类型', name: 'borrowType', index: '$BORROW_TYPE', width: 100,align:'right' }, 			
			{ label: '投资记录ID', name: 'pid', index: '$PID', width: 85,align:'right' }, 			
			{ label: '涉及项目名称', name: 'projectName', index: '$PROJECT_NAME', width: 150,align:'right' }, 			
			{ label: '涉及项目本金', name: 'tenderCapital', index: '$TENDER_CAPITAL', width: 110,align:'right',
				formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2) + "元";
					}else{
						return '0.00元';
					}
				}
			},
			{ label: '涉及项目期限', name: 'borrowPeriod', index: '$BORROW_PERIOD', width: 85,align:'right',
				formatter:function(cellvalue, options, rowObject){
					return cellvalue + "天";
				}
			},
			{ label: '当前持有', name: 'recoverAccountWait', index: '$RECOVER_ACCOUNT_WAIT', width: 110,align:'right',
				formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2) + "元";
					}else{
						return '0.00元';
					}
				}
			},
			{ label: '目前状态', name: 'stage', index: '$STAGE', width: 80,align:'right'}
			
//			STAGE 项目阶段（1待提交审核 2审核中待发布、3审核通过待排标、4已发布筹款中、5还款中、8已结清、9流标） 
//			,				
//			{ label: 'CIA', name: 'cia', index: '$CIA', width: 80 ,align:'right'}			
        ],
		viewrecords: true,
        height: 390,
        rowNum: 10,
//		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
        multiselect: false,
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
        },
        loadComplete: function(){
        	$("#query_cond").removeAttr("disabled");
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportInvestmentDaily: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportInvestmentDaily = {};
		},
		update: function (event) {
			var statPeriod = getSelectedRow();
			if(statPeriod == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(statPeriod)
		},
		saveOrUpdate: function (event) {
			var url = vm.dmReportInvestmentDaily.statPeriod == null ? "../dmreportinvestmentdaily/save" : "../dmreportinvestmentdaily/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportInvestmentDaily),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var statPeriods = getSelectedRows();
			if(statPeriods == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../dmreportinvestmentdaily/delete",
				    data: JSON.stringify(statPeriods),
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
		getInfo: function(statPeriod){
			$.get("../dmreportinvestmentdaily/info/"+statPeriod, function(r){
                vm.dmReportInvestmentDaily = r.dmReportInvestmentDaily;
            });
		},
		reload: function (event) {
			vm.showList = true;
			resetTotalInfo();
			$("#query_cond").attr({"disabled":"disabled"});
			$("#jqGrid").jqGrid("clearGridData");
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../dmreportinvestmentdaily/list',
	            postData: getParams(), //发送数据  
	            page:page 
            }).trigger("reloadGrid");
			queryTotalInfo();
		}
	}
});

function resetTotalInfo(){
	var char = '-';
	$("#total_invest_amount").html(char);
	$("#curr_have_amount").html(char);
}
//获取渠道信息
function getChannelName(){
    var arrStr = '';
    $(".select2-selection__choice").each(function(){
        arrStr += $(this).attr("title") + "^";
    });
    return arrStr;
};
//加载渠道数据
function loadChannel(){
	   var str = '';
	    var i = 0;
	    $.ajax({
	        type: "POST",
	        url: "../channel/queryChannelName",
	        data: JSON.stringify(),
	        contentType: "application/json;charset=utf-8",
	        success : function(msg) {
	            for(var list in msg.Channel){
	                for(var key in msg.Channel[list]){
	                    if(key == "channelName")
	                        str += '<option value="'+(i++)+'">' + msg.Channel[list][key] + "</option>";
	                }
	            }

	            $("#id_select").select2({
	                maximumSelectionLength: 3,
	                width:'170'
	            });
	            $("#id_select").append(str);
	        }
	     });

};
function getParams(){
	var params = {
        	'userId': $("#user_id").val(), 
        	'userName': $("#user_name").val(),
        	'operPlatform': $("#oper_platform").val() ,
        	'withProType': $("#with_pro_type").val(),
        	'channelId': $("#channel_id").val(),
        	'investStartTime': $("#start_invest_time").val(),
        	'investEndTime': $("#end_invest_time").val(),
        	'channelName': getChannelName().toString().length == "0" ? null : getChannelName()
	};
	return params;
}