$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initUpload();
	initSelectEvent();
	$("#stat_period").val(addDate(getCurrDate(), -3));
});

function initSelectEvent(){
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == '1'){//首投后3天未复投
			$("#date_text").html("首投日期：");
			$("#stat_period").val(addDate(getCurrDate(), -3));
			$("#upload").hide();
		}else{
			$("#stat_period").val('');
			$("#upload").show();
			$("#date_text").html("电销开始日期：");
		}
	});
}

function initUpload(){
	
    new AjaxUpload('#upload', {
        action: '../basicreport/importPhoneSaleUser?type=' + $("#list_select").val(),
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
        	this.setData({type:$("#list_select").val()});
        },
        onComplete : function(file, r){
            if(r.code == 0){
                alert('数据导入成功');
            }else{
                alert(r.msg);
            }
        }
    });
    
    $("#upload").hide();
}

function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
//    $("#stat_period").val(addDate(getCurrDate(), -3));
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../basicreport/phoneSaleCgUserListExport', {'params' : JSON.stringify(params)});  
	});

}

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../basicreport/queryPhoneSaleCgUserList',
        datatype: "json",
        colModel: [			
        	{ label: '用户ID', name: 'user_id', index: '$COST', width: 80 ,align:'right'}, 			
        	{ label: '电话', name: 'phone', index: '$COST', width: 80 ,align:'right'}, 			
			{ label: '用户名', name: 'user_name', index: '$STAT_PERIOD', width: 80,align:'right' },
			{ label: '用户姓名', name: 'real_name', index: '$NAME', width: 80 ,align:'right'}, 			
			{ label: '电销日期', name: 'call_date', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '数据提供日期', name: 'give_date', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '存管ID', name: 'cg_user_id', index: '$CODE', width: 80,align:'right' }, 			
			{ label: '存管开户时间', name: 'depository_open_time', index: '$COST_SOURCE', width: 80 ,align:'right'}
        ],
		viewrecords: true,
        height: $(window).height()-130,
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
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
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../basicreport/queryPhoneSaleCgUserList',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'date': $("#stat_period").val(),
        	'type': $("#list_select").val(),
        	'isKaitongCg': $("#cg_select").val()
	};
	return params;
}