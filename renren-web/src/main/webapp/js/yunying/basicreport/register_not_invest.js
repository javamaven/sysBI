$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		initTimeCond(select);
	});
}

function initTimeCond(type){
	
	var format = 'yyyy-mm-dd hh';
	var view = 'day';
	if(type && type == 'day'){
		 format = 'yyyy-mm-dd';
		 view = 'month';
	}
	$('#registerStartTime').datetimepicker('remove');
	$('#registerEndTime').datetimepicker('remove');
    $("#registerStartTime").datetimepicker({
        format: format,
        minView: view,
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#registerEndTime").datetimepicker({
        format: format,
        minView: view,
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
    if(type && type == 'day'){
    	$("#registerStartTime").val(addDate(getCurrDate(), -3));
		$("#registerEndTime").val(addDate(getCurrDate(), -1));
		
		$("#hour_task").hide();
		$("#day_task").show();
	}else{
		$("#registerStartTime").val(addHours(-2));
		$("#registerEndTime").val(addHours(-2));
		
		$("#hour_task").show();
		$("#day_task").hide();
	}
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		if(!$("#registerStartTime").val()){
			alert('请先选择注册开始时间')
			return;
		}
		if(!$("#registerEndTime").val()){
			alert('请先选择注册结束时间')
			return;
		}
		var params = getParams();
		var select = $(this).children('option:selected').val();
		if(select == 'hour'){
			executePost('../basicreport/exportExcel', {'params' : JSON.stringify(params)});  
		}else{
			executePost('../basicreport/exportRegisterThreeDaysExcel', {'params' : JSON.stringify(params)});  
		}
	});

}

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../basicreport/registNotInvest',
        datatype: "json",
        colModel: [		
			{ label: '用户ID', name: '用户ID', index: 'registerTime', width: 100,align:'right' },
			{ label: '用户名', name: '用户名', index: '$CODE', width: 100,align:'right' }, 			
			{ label: '手机号', name: '手机号', index: '$NAME', width: 110 ,align:'right'}, 			
			{ label: '注册时间', name: '注册时间', index: '$COST', width: 160 ,align:'right'}, 			
			{ label: '用户来源', name: '用户来源', index: '$COST_SOURCE', width: 140 ,align:'right'},
			
			{ label: '实名认证', name: '实名认证', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '真实姓名', name: '真实姓名', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '是否投资', name: '是否投资', index: '$COST_SOURCE', width: 80 ,align:'right'},
			
			{ label: '投资次数', name: '投资次数', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '最近一次投资时间', name: '最近一次投资时间', index: '$COST_SOURCE', width: 140 ,align:'right'},
			{ label: '最近一次投资期限', name: '最近一次投资期限', index: '$COST_SOURCE', width: 140 ,align:'right'},
			{ label: '账户余额', name: '账户余额', index: '$COST_SOURCE', width: 80 ,align:'right'}
        ],
		viewrecords: true,
        height:  $(window).height()-170,
        rowNum: 20,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDdzRemain: {}
	},
	methods: {
		reload: function (event) {
			vm.showList = true;
			if(!$("#registerStartTime").val()){
				alert('请先选择注册开始时间')
				return;
			}
			if(!$("#registerEndTime").val()){
				alert('请先选择注册结束时间')
				return;
			}
			var url = '';
			var select = $("#list_select").children('option:selected').val();
			if(select == 'hour'){
				url = '../basicreport/registNotInvest';
			}else if(select == 'day'){
				url = '../basicreport/registThreeDaysNotInvest';
			}
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: url,
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {};
	var select = $("#list_select").children('option:selected').val();
	if(select == 'hour'){
		params.registerStartTime = $("#registerStartTime").val() + ':00:00';
		params.registerEndTime = $("#registerEndTime").val() + ':59:59';
	}else if(select == 'day'){
		params.registerStartTime = $("#registerStartTime").val() + ' 00:00:00';
		params.registerEndTime = $("#registerEndTime").val() + ' 23:59:59';
	}
	return params;
}