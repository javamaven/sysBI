$(function(){
	initDateList();//初始化时间下拉框
	initYunyingTable();
	initShichangRegTable();
//	initShichangCost();
	initTimeEvent();
});

function initDateList(){
	var currDay = getCurrDate();
	var year = currDay.substring(0, 4);
	var month_ = currDay.substring(5, 7);
	month_ = parseInt(month_);
	var html = '';
	for (var i = 1; i <= 12; i++) {
		if(i <= 6){//7月份前的不允许查询
			continue;
		}
		if(i == month_){
			if(month_ < 10){
				month_ = '0' + i;
			}
			html += '<option value ="'+year+'-'+month_+'" selected="selected">'+year+' 年 '+i+' 月</option>';
		}else{
			var month = i;
			if(i < 10){
				month = '0' + i;
			}
			html += '<option value ="'+year+'-'+month+'" >'+year+' 年 '+i+' 月</option>';
		}
	}
	$("#list_select").html(html);
}

function initTimeEvent(){
	$("#stat_period").datetimepicker({
		format: 'hh:ii',
		startView: 'day',
		maxView: 'day',
		minView: 'hour',
		language: 'zh-CN',
		autoclose:true
	}).on("click",function(){
	});
}

function query(){
	loading();
	queryYunying();
	queryRegFirstInvest();
}
function queryYunying(){
	var params = getParams();
	$("#jqGrid").jqGrid("clearGridData");
	$("#jqGrid").jqGrid('setGridParam',{ 
		datatype:'json', 
		url: '../yunying/zixiao/list',
        postData: getParams()
    }).trigger("reloadGrid");
}
function queryRegFirstInvest(){
	var params = getParams();
	$("#shichang_reg").jqGrid("clearGridData");
	$("#shichang_reg").jqGrid('setGridParam',{ 
		datatype:'json', 
		url: '../yunying/zixiao/regFirstInvestList',
        postData: getParams()
    }).trigger("reloadGrid");
}
function getParams(){
	var params = {
        	'statPeriod': $("#list_select").val()
	};
	return params;
}
function hideDiv(){
	$("#caiwu_luru_div").hide();
	$("#shichang_luru_phone_cost_div").hide();
	$("#queren_div").hide();
	$("#caiwu_luru_btn").hide();
	$("#yunying_queren_btn").hide();
	$("#shichang_queren_btn").hide();
	$("#shichang_luru_channel_cost_div").hide();
	
}
/**
 * 打开输入框
 * @param type
 * @returns
 */
var curr_queren_index; //打开窗口，当前准备确认的指标
var curr_select_row;//当前选中行
var shichang_curr_operation;//录入，确认
function checkAuth( type , indexName, rowId, status){
	hasAuth( type , indexName, rowId, status);
}
function openWindow( type , indexName, rowId, status){
	$("#complete_value").val('');
	$("#description").val('');
	hideDiv();
	if(type == 'caiwu_luru'){
		curr_select_row = $("#jqGrid").jqGrid('getRowData',rowId);
		$("#window_title").html('录入完成值')
		$("#caiwu_luru_div").show();
		$("#caiwu_luru_btn").show();
		$("#window_body").css('height', '70px');
		$("#shichang_queren_btn").hide();
	}else if(type == 'yunying_queren'){
		curr_select_row = $("#jqGrid").jqGrid('getRowData',rowId);
		$("#window_title").html('确认')
		$("#queren_div").show();
		$("#yunying_queren_btn").show();
		$("#window_body").css('height', '120px');
	}else if(type == 'shichang_luru_phone_cost'){//市场部录入电销成本
		shichang_curr_operation = '录入';
		$("#window_title").html('录入电销成本')
		$("#shichang_luru_phone_cost_div").show();
		$("#shichang_queren_btn").show();
		$("#window_body").css('height', '70px');
		curr_select_row = $("#shichang_reg").jqGrid('getRowData',rowId);
	}else if(type == 'shichang_queren'){
		shichang_curr_operation = '确认';
		curr_select_row = $("#shichang_reg").jqGrid('getRowData',rowId);
		$("#window_title").html('确认')
		$("#queren_div").show();
		$("#shichang_queren_btn").show();
		$("#window_body").css('height', '120px');
	}else if(type == 'shichang_luru_channel_cost'){//渠道成本录入
		shichang_curr_operation = '录入渠道成本';
		$("#window_title").html('录入渠道成本');
		$("#shichang_luru_channel_cost_div").show();
		$("#shichang_queren_btn").show();
		$("#window_body").css('height', '70px');
		curr_select_row = $("#shichang_reg").jqGrid('getRowData',rowId);
	}
	curr_queren_index = indexName;
	$('#open_window').modal('show')
}
/**
 * 确定输入完成值
 * @returns
 */
function inputCompleteValue(){
	var value = $("#complete_value").val();
	if(!value){
		alert('请输入完成值');
		return;
	}
	if(!isIntOrDoubleValue(value)){
		alert('请输入正确的数字');
		return;
	}
	var select_time = $("#list_select").val();
	var month = select_time.substring(5, 7);
	month = parseInt(month) + '';
    $.ajax({
        type: "POST",
        url: "../yunying/zixiao/inputCompleteValue",
        data: JSON.stringify({'completeValue': value, 'month': month}),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
           if(msg.code == 'success'){
        	   alert('录入成功')
        	   queryYunying();
        	   $('#open_window').modal('hide');
           }else{
        	   alert('录入失败')
           }
        }
     });
}
/**
 * 运营部确认
 * @returns
 */
function hasAuth( type , indexName, rowId, status){
    $.ajax({
        type: "POST",
        async: false,
        url: "../yunying/zixiao/hasAuth",
        data: JSON.stringify({'状态': status}),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
           if(!msg.hasAuth){
        	   alert('没有权限')
           }else{
        	   openWindow( type , indexName, rowId, status);
           }
        }
     });
}
/**
 * 运营部确认
 * @returns
 */
function yunyingQueren(){
	curr_select_row.description = $("#description").val();
	var select_time = $("#list_select").val();
	var month = select_time.substring(5, 7);
	month = parseInt(month);
	curr_select_row.month = month;
    $.ajax({
        type: "POST",
        url: "../yunying/zixiao/queren",
        data: JSON.stringify(curr_select_row),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
           if(msg.code == 'success'){
        	   alert('确认成功')
        	   queryYunying();
        	   $('#open_window').modal('hide');
           }else{
        	   alert('确认失败')
           }
        }
     });
}

function exportDetail(index_name, month){
	var data = {'indexName': index_name, 'month': month } ;
	executePost('../yunying/zixiao/exportExcel', {'data': JSON.stringify(data)});  
}
/**
 * 市场部确认
 * @returns
 */
function shichangQueren(){
	var phone_sale_cost = $("#phone_sale_cost_value").val();
	if(shichang_curr_operation == '录入'){
		if(!phone_sale_cost){
			alert('请输入电销成本');
			return;
		}
		if(!isIntOrDoubleValue(phone_sale_cost)){
			alert('请输入正确的数字');
			return;
		}
	}
	var channel_cost_value = $("#channel_cost_value").val();
	if(shichang_curr_operation == '录入渠道成本'){
		
		if(!channel_cost_value){
			alert('请输入渠道成本');
			return;
		}
		if(!isIntOrDoubleValue(channel_cost_value)){
			alert('请输入正确的数字');
			return;
		}
	}
	var select_time = $("#list_select").val();
	var month = select_time.substring(5, 7);
	month = parseInt(month);
	curr_select_row.month = month;
	if(shichang_curr_operation == '录入'){
		curr_select_row.完成值 = phone_sale_cost;
	}else if(shichang_curr_operation == '录入渠道成本'){
		curr_select_row.完成值 = channel_cost_value;
	}
    $.ajax({
        type: "POST",
        url: "../yunying/zixiao/shichangQueren",
        data: JSON.stringify(curr_select_row),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
           if(msg.code == 'success'){
        	   alert('确认成功')
        	   queryRegFirstInvest();
        	   $('#open_window').modal('hide');
           }else{
        	   alert('确认失败')
           }
        }
     });
}
function initYunyingTable(){
    $("#jqGrid").jqGrid({
//    	url: '../yunying/zixiao/list',
        datatype: "json",
//        cellEdit:true,//是否开启单元格的编辑功能 
        colModel: [			
			{ label: '指标', name: '指标', index: '$STAT_PERIOD', width: 90,align:'right' },
			{ label: '完成值（万元）', name: '完成值', index: '$OWNER', width: 80 ,align:'right',formatter: function(value, options, row){
					if(row.指标 == '费用/年化比(17年累计)'){
						if(value){
							return value;
						}
						return '';
					}else{
						return formatNumber(value + '',2);
					}
				} 
			},
			{ label: '目标值', name: '目标值', index: '$OWNER', width: 80 ,align:'right',editable: true,formatter: function(value, options, row){
					if(row.指标 == '费用/年化比(17年累计)'){
						return value;
					}else{
						return formatNumber(value + '',2);
					}
				}  
			}, 			
			{ label: '达成率', name: '达成率', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '状态', name: '状态', index: '$MONTH_TENDER', width: 100,align:'right', formatter: function(value, options, row){
					if(value.indexOf('等待') > -1){
						return '<span class="label label-danger">'+value+'</span>';
					}else{
						return '<span class="label label-success">'+value+'</span>';
					}
				} 
			}, 			
			{ label: '操作', name: '操作', index: '$MONTH_TENDER_Y', width: 100 ,align:'right', formatter: function(value, options, row){
				var queren = '<span onclick="checkAuth(\'yunying_queren\',\''+row.指标+'\',\''+options.rowId+'\',\''+row.状态+'\')" class="btn btn-primary btn-inverse btn-xs">&nbsp;确&nbsp;&nbsp;&nbsp;认&nbsp;</span>&nbsp;&nbsp;&nbsp;&nbsp;';
				var luru = '<a onclick="checkAuth(\'caiwu_luru\',\''+row.指标+'\',\''+options.rowId+'\',\''+row.状态+'\')" class="btn btn-primary btn-inverse btn-xs">录入完成值</a> &nbsp;&nbsp;&nbsp;&nbsp;';
				
				var cancel = '';
				
//				if(row.cancel){
//					cancel = '<a onclick="cancel(\'caiwu_luru\',\''+row.指标+'\',\''+options.rowId+'\',\''+row.状态+'\')" class="btn btn-primary btn-inverse btn-xs">撤销</a> &nbsp;&nbsp;&nbsp;&nbsp;';
//				}
				if(row.状态 == '已完成'){
					return '';
				}
				if(row.指标 == '费用/年化比(17年累计)'){
					if(row.状态 != '等待财务部录入'){
						return queren + cancel;
					}else{
						return luru + cancel;
					}
				}else{
					return queren + cancel;
				}
			} } 			
        ],
		viewrecords: true,
        height: 120,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
//        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        onSelectRow: function(rowId){
        	
    	},
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        },
        loadComplete:function(){
        	
        }
        
    });
}
function initShichangRegTable(){
    $("#shichang_reg").jqGrid({
//    	url: '../yunying/zixiao/list',
        datatype: "json",
//        cellEdit:true,//是否开启单元格的编辑功能 
        colModel: [			
			{ label: '市场部指标', name: '指标', index: '$STAT_PERIOD', width: 90,align:'right' },
			{ label: '完成值', name: '完成值', index: '$OWNER', width: 80 ,align:'right',formatter: function(value, options, row){
					if(value){
						return formatNumber(value + '',2);
					}
					return '';
				} 
			},
			{ label: '目标值', name: '目标值', index: '$OWNER', width: 80 ,align:'right',editable: true,formatter: function(value, options, row){
					if(value == null || value == ''){
						return '';
					}
					if(value){
						return formatNumber(value + '',2);
					}
					return '';
				} 
			}, 			
			{ label: '达成率', name: '达成率', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '状态', name: '状态', index: '$MONTH_TENDER', width: 100,align:'right', formatter: function(value, options, row){
					if(value.indexOf('等待') > -1){
						return '<span class="label label-danger">'+value+'</span>';
					}else{
						return '<span class="label label-success">'+value+'</span>';
					}
				} 
			}, 			
			{ label: '操作', name: '操作', index: '$MONTH_TENDER_Y', width: 100 ,align:'right', formatter: function(value, options, row){
					var queren = '<span onclick="checkAuth(\'shichang_queren\',\''+row.指标+'\',\''+options.rowId+'\',\''+row.状态+'\')" class="btn btn-primary btn-inverse btn-xs">&nbsp;确&nbsp;&nbsp;&nbsp;认&nbsp;</span>&nbsp;&nbsp;&nbsp;&nbsp;';
					var luru = '<a onclick="checkAuth(\'shichang_luru_phone_cost\',\''+row.指标+'\',\''+options.rowId+'\',\''+row.状态+'\')" class="btn btn-primary btn-inverse btn-xs">录入电销成本</a> &nbsp;&nbsp;&nbsp;&nbsp;';
					var luru_channel = '<a onclick="checkAuth(\'shichang_luru_channel_cost\',\''+row.指标+'\',\''+options.rowId+'\',\''+row.状态+'\')" class="btn btn-primary btn-inverse btn-xs">录入渠道成本</a> &nbsp;&nbsp;&nbsp;&nbsp;';
					var exportHtml = '<a onclick="exportDetail(\'' + row.指标 + '\',\'' + row.month + '\')" class="btn btn-primary btn-inverse btn-xs">导出明细</a> &nbsp;&nbsp;&nbsp;&nbsp;';
					if(row.状态 == '已完成'){
						if(row.指标 == '市场部本月红包成本' || row.指标 == '市场部本月渠道成本' || row.指标 == '当月首投用户本月累投金额'){
							return exportHtml;
						}else{
							return '';
						}
					}
					if(row.指标 == '市场部电销成本'){
						if(row.状态 == '等待市场部录入'){
							return luru;
						}else{
							return queren;
						}
					}else if(row.指标 == '市场部本月渠道成本'){
						if(row.状态 == '等待市场部录入'){
							return luru_channel + exportHtml;
						}else{
							return queren + exportHtml;
						}
					}else if(row.指标 == '市场部本月红包成本' || row.指标 == '当月首投用户本月累投金额'){
						return queren + exportHtml;
					}else{
						return queren;
					}
				} 
			} 			
        ],
		viewrecords: true,
        height: 280,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
//        pager: "#jqGridPager",
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
        	$("#shichang_reg").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        },
        loadComplete:function(){
        	loaded();
        }
        
    });
}
function initShichangCost(){
    $("#shichang_cost").jqGrid({
//    	url: '../yunying/zixiao/list',
        datatype: "json",
//        cellEdit:true,//是否开启单元格的编辑功能 
        colModel: [			
			{ label: '市场部指标', name: '指标', index: '$STAT_PERIOD', width: 90,align:'right' },
			{ label: '完成值', name: '完成值', index: '$OWNER', width: 80 ,align:'right'},
			{ label: '目标值', name: '目标值', index: '$OWNER', width: 80 ,align:'right',editable: true }, 			
			{ label: '达成率', name: '达成率', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '状态', name: '状态', index: '$MONTH_TENDER', width: 100,align:'right', formatter: function(value, options, row){
					if(value == '等待运营部确认'){
						return '<span class="label label-success">'+value+'</span>';
					}else{
						return '<span class="label label-danger">'+value+'</span>';
					}
				} 
			}, 			
			{ label: '操作', name: '操作', index: '$MONTH_TENDER_Y', width: 100 ,align:'right', formatter: function(value, options, row){
				if(row.指标 == '费用/年化比( 17年累计)'){
					var html = '<span class="label label-success">录入完成值</span>&nbsp;&nbsp;&nbsp;&nbsp;';
					html += '<span class="label label-success">确认</span>';
					return html;
				}else{
					return '<span class="label label-success">确认</span>';
				}
			} } 			
        ],
		viewrecords: true,
        height: 120,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
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
        },
        loadComplete:function(){
        	
        }
        
    });
}