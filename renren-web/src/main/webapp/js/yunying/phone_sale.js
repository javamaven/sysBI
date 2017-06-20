$(function () {
	initTimeCond();
	initDayListTableGrid();
	
	initMonthListTableGrid();
	initMonthTotalTableGrid();
	initExportFunction();
	initUpload();
	initSelectEvent();
});



function initSelectEvent(){
	//日报，月报切换
	$("#day_or_month_report").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'day'){
			$("#month_div").hide();
			$("#day_list_div").show();
			$("#report_type_select_tr").hide();
			$("#is_day_report_count_tr").hide();
			$("#is_day_report_count_tr_height").hide();
		}else if(select == 'month'){
			$("#report_type_select_tr").show();
			$("#month_div").show();
			$("#day_list_div").hide();
			$('#invest_end_time').attr("disabled",true); 
			$("#is_day_report_count_tr").show();
			$("#is_day_report_count_tr_height").show();
			var se = $("#report_type_select").children('option:selected').val();
			monthSelect(se);
		}
	});
	//月报列表，月报汇总切换
	$("#report_type_select").change(function(){
		var select = $(this).children('option:selected').val();
		monthSelect(select);
	});
	
}

function monthSelect(select){
	if(select == 'total'){//汇总信息
		$("#month_list_div").hide();
		$("#month_total_div").show();
		$("#invest_end_time").val("");
		$('#invest_end_time').attr("disabled",true); 
	}else if(select == 'list'){//明细信息
		$("#month_list_div").show();
		$("#month_total_div").hide();
		$("#invest_end_time").val("");
		$('#invest_end_time').attr("disabled",true); 
	}else if(select == 'day_report_total'){
		$("#month_list_div").hide();
		$("#month_total_div").show();
		$('#invest_end_time').attr("disabled", false); 
	}
}

function initExportFunction(){
	$('#btn_exports').click(function(){
		var reportType = $("#day_or_month_report").children('option:selected').val();
		var selectType = $("#report_type_select").children('option:selected').val();
		if(selectType == 'day_report_total'){
			if(!$("#invest_end_time").val()){
				alert('请选择投资结束时间');
				return;
			}
		}
		var params = getParams();
		params.selectType = selectType;
		params.reportType = reportType;
		executePost('../yunying/phonesale/exportExcel', {'params' : JSON.stringify(params)});  
	});

}

function initUpload(){
    new AjaxUpload('#upload', {
        action: '../yunying/phonesale/upload',
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
        },
        onComplete : function(file, r){
            if(r.code == 0){
                alert('数据导入成功');
            }else{
                alert(r.msg);
            }
        }
    });
}
function initTimeCond(){
    $("#invest_end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    document.getElementById("invest_end_time").value = getYesterday();
}

function initDayListTableGrid(){
    $("#jqGrid_day_list").jqGrid({
//        url: '../yunying/phonesale/daylist',
        datatype: "json",
        postData: getParams(),
        colModel: [			
        	{ label: '统计日期', name: '统计日期', index: '$用户名', width: 90, key: true,align:'right' },
        	{ label: '用户名', name: '用户名', index: '$用户名', width: 100, key: true,align:'right' },
			{ label: '用户姓名', name: '用户姓名', index: '$USER_ID', width: 90,align:'right' }, 			
			{ label: '是否双系统', name: '是否双系统', index: '$是否双系统', width: 90,align:'right' }, 			
			{ label: '电销人员', name: '电销人员', index: '$USERNAME', width: 90,align:'right' }, 			
			{ label: '电销结果', name: '电销结果', index: '$PHONE', width: 80 ,align:'right'}, 			
			{ label: '电销日期', name: '电销日期', index: '$PROJECT_ID', width: 80,align:'right' }, 			
			{ label: '是否投资', name: '是否投资', index: '$MONEY', width: 70 ,align:'right'}, 			
			{ label: '接通后-实际投资金额', name: '接通后-实际投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			
			{ label: '接通后-销售奖励金额', name: '接通后-销售奖励金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通后-年化投资金额', name: '接通后-年化投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通后-投资次数', name: '接通后-投资次数', index: '$BORROW_PERIOD', width: 120 ,align:'right'},	
			
			{ label: '接通后-首次投资时间', name: '接通后-首次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通后-末次投资时间', name: '接通后-末次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-实际投资金额', name: '接通前-实际投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			
			{ label: '接通前-销售奖励金额', name: '接通前-销售奖励金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-年化投资金额', name: '接通前-年化投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-投资次数', name: '接通前-投资次数', index: '$BORROW_PERIOD', width: 120 ,align:'right'},	
			
			{ label: '接通前-首次投资时间', name: '接通前-首次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-末次投资时间', name: '接通前-末次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			
			{ label: '当月是否投资', name: '当月是否投资', index: '$BORROW_PERIOD', width: 110 ,align:'right'},	
			{ label: '当月投资次数', name: '当月投资次数', index: '$BORROW_PERIOD', width: 110 ,align:'right'},
			{ label: '当月实际投资金额', name: '当月投资次数', index: '$BORROW_PERIOD', width: 130 ,align:'right'},	
			{ label: '当月销售奖励金额', name: '当月销售奖励金额', index: '$BORROW_PERIOD', width: 130 ,align:'right'},	

			{ label: '当月年化投资金额', name: '当月年化投资金额', index: '$BORROW_PERIOD', width: 130 ,align:'right'},
			{ label: '当月首次投资时间', name: '当月首次投资时间', index: '$BORROW_PERIOD', width: 130 ,align:'right'},
			{ label: '当月末次投资时间', name: '当月末次投资时间', index: '$BORROW_PERIOD', width: 130 ,align:'right'},
			
			{ label: '当天是否投资', name: '当天是否投资', index: '$BORROW_PERIOD', width: 110 ,align:'right'},
			{ label: '当天实际投资金额', name: '当天实际投资金额', index: '$BORROW_PERIOD', width: 130 ,align:'right'},	
			{ label: '当天销售奖励金额', name: '当天销售奖励金额', index: '$BORROW_PERIOD', width: 130 ,align:'right'},	
			
			{ label: '当天年化投资金额', name: '当天年化投资金额', index: '$BORROW_PERIOD', width: 140 ,align:'right'},	
			{ label: '当天投资次数', name: '当天投资次数', index: '$BORROW_PERIOD', width: 110 ,align:'right'}
			
        ],
		viewrecords: true,
        height: $(window).height()-200,
        rowNum: 20,
		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
        multiselect: false,
        pager: "#jqGridPager_day_list",
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

function initMonthListTableGrid(){
    $("#jqGrid_month_list").jqGrid({
//        url: '../yunying/phonesale/monthlist',
        datatype: "json",
        colModel: [			
			{ label: '用户名', name: '用户名', index: '$用户名', width: 90, key: true,align:'right' },
			{ label: '用户姓名', name: '用户姓名', index: '$USER_ID', width: 90,align:'right' }, 			
			{ label: '是否双系统', name: '是否双系统', index: '$是否双系统', width: 90,align:'right' }, 			
			{ label: '电销人员', name: '电销人员', index: '$USERNAME', width: 90,align:'right' }, 			
			{ label: '电销结果', name: '电销结果', index: '$PHONE', width: 80 ,align:'right'}, 			
			{ label: '电销日期', name: '电销日期', index: '$PROJECT_ID', width: 90,align:'right' }, 			
			{ label: '是否投资', name: '是否投资', index: '$MONEY', width: 70 ,align:'right'}, 			
			{ label: '接通后-实际投资金额', name: '接通后-实际投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			
			{ label: '接通后-销售奖励金额', name: '接通后-销售奖励金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通后-年化投资金额', name: '接通后-年化投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通后-投资次数', name: '接通后-投资次数', index: '$BORROW_PERIOD', width: 140 ,align:'right'},	
			
			{ label: '接通后-首次投资时间', name: '接通后-首次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通后-末次投资时间', name: '接通后-末次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-实际投资金额', name: '接通前-实际投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			
			{ label: '接通前-销售奖励金额', name: '接通前-销售奖励金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-年化投资金额', name: '接通前-年化投资金额', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-投资次数', name: '接通前-投资次数', index: '$BORROW_PERIOD', width: 140 ,align:'right'},	
			
			{ label: '接通前-首次投资时间', name: '接通前-首次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'},	
			{ label: '接通前-末次投资时间', name: '接通前-末次投资时间', index: '$BORROW_PERIOD', width: 150 ,align:'right'}	
			
        ],
		viewrecords: true,
        height:  $(window).height()-230,
        rowNum: 20,
		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
        multiselect: false,
        pager: "#jqGridPager_month_list",
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    
    $("#month_list_div").hide();
}


/**
 * 月报汇总信息列表
 * @returns
 */
function initMonthTotalTableGrid(){
    $("#jqGrid_month_total").jqGrid({
        datatype: "json",
        colModel: [			
			{ label: '电销人员', name: '电销人员', index: '$用户名', width: 90, key: true,align:'right' },
			{ label: '客户总量(人)', name: '客户总量(人)', index: '$USER_ID', width: 90,align:'right' }, 			
			{ label: '有效客户外呼数(人)', name: '有效客户外呼数(人)', index: '$是否双系统', width: 90,align:'right' }, 			
			{ label: '投资人数(人)', name: '投资人数(人)', index: '$USERNAME', width: 90,align:'right' }, 			
			{ label: '投资总额(元)', name: '投资总额(元)', index: '$PHONE', width: 100 ,align:'right'}, 			
			{ label: '年化投资总额(元)', name: '年化投资总额(元)', index: '$PROJECT_ID', width: 100,align:'right' }, 			
			{ label: '人均年化投资额(元)', name: '人均年化投资额(元)', index: '$MONEY', width: 80 ,align:'right'}, 			
			{ label: '接通率', name: '接通率', index: '$BORROW_PERIOD', width: 80 ,align:'right'},	
			
			{ label: '有效外呼转化率', name: '有效外呼转化率', index: '$BORROW_PERIOD', width: 80 ,align:'right'},	
			{ label: '总体转化率', name: '总体转化率', index: '$BORROW_PERIOD', width: 80 ,align:'right'}

		],
        viewrecords: true,
		height:  $(window).height()-230,
        rowNum: 20,
		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
        multiselect: false,
        pager: "#jqGridPager_month_total",
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    $("#month_total_div").hide();
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportRecoverData: {}
	},
	methods: {
		reload: function (event) {
			var reportType = $("#day_or_month_report").children('option:selected').val();
			if("day" == reportType){
				$("#jqGrid_day_list").jqGrid("clearGridData");
				$("#jqGrid_day_list").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/phonesale/daylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if("month" == reportType){
				var selectType = $("#report_type_select").children('option:selected').val();
				if(selectType == 'list'){
					$("#jqGrid_month_list").jqGrid("clearGridData");
					$("#jqGrid_month_list").jqGrid('setGridParam',{ 
						datatype:'json', 
						url: '../yunying/phonesale/monthlist',
			            postData: getParams()
		            }).trigger("reloadGrid");
				}else if(selectType == 'total'){
					$("#jqGrid_month_total").jqGrid("clearGridData");
					$("#jqGrid_month_total").jqGrid('setGridParam',{ 
						datatype:'json',
						url: '../yunying/phonesale/monthTotalList',
			            postData: getParams()
		            }).trigger("reloadGrid");
				}else if(selectType == 'day_report_total'){
					if(!$("#invest_end_time").val()){
						alert('请选择投资结束时间');
						return;
					}
					$("#jqGrid_month_total").jqGrid("clearGridData");
					$("#jqGrid_month_total").jqGrid('setGridParam',{ 
						datatype:'json',
						url: '../yunying/phonesale/monthTotalList',
			            postData: getParams()
		            }).trigger("reloadGrid");
				}
			}
		}
	}
});

function getParams(){
	var params = {
			investEndTime: $("#invest_end_time").val()
	};
	return params;
}