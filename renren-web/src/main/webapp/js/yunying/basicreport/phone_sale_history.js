$(function () {
	initTimeCond();
	initTableGrid();
	initExportFunction();
	initUpload();
});

function initUpload(){
    new AjaxUpload('#upload', {
        action: '../phonesale/upload',
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
    $("#register_start_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
    $("#register_end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
    $("#register_start_time").val(addDate(getCurrDate(), -30));
    $("#register_end_time").val(getCurrDate());
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		if(!params.limitNum){
			alert('请先设置提取条数')
			return;
		}
		executePost('../basicreport/phoneSaleHistoryListExport', {'params' : JSON.stringify(params)});  
	});

}

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../basicreport/phoneSaleHistoryList',
        datatype: "json",
        colModel: [			
			{ label: '存管版用户ID', name: '存管版用户ID', index: '$STAT_PERIOD', width: 50,align:'right' },
			{ label: '用户名', name: '用户名', index: '$CODE', width: 80,align:'right' }, 			
			{ label: '电话', name: '电话', index: '$NAME', width: 80 ,align:'right'}, 			
			{ label: '注册时间', name: '注册时间', index: '$COST', width: 80 ,align:'right'}, 			
			{ label: '用户来源', name: '用户来源', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '实名认证', name: '实名认证', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '红包类型', name: '红包类型', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '有效期', name: '有效期', index: '$COST_SOURCE', width: 80 ,align:'right'},
			
			{ label: '真实姓名', name: '真实姓名', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '普通版账户余额', name: '普通版账户余额', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '存管版账户余额', name: '存管版账户余额', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '普通版用户ID', name: '普通版用户ID', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '存管版是否开户', name: '存管版是否开户', index: '$COST_SOURCE', width: 80 ,align:'right'},
			{ label: '用户ID', name: '用户ID', index: '$COST_SOURCE', width: 80 ,align:'right'}
			
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
				url: '../basicreport/phoneSaleHistoryList',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'registerStartTime': $("#register_start_time").val(),
        	'registerEndTime': $("#register_end_time").val(),
        	'limitNum': $("#limit_num").val()
	};
	return params;
}