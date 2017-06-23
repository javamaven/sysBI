$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
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
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
    $("#stat_period").val(addDate(getCurrDate(), -3));
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../basicreport/exportFirstInvestNotMultiExcel', {'params' : JSON.stringify(params)});  
	});

}

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../basicreport/firstInvestNotMulti',
        datatype: "json",
        colModel: [			
			{ label: '用户名', name: 'USERNAME', index: '$STAT_PERIOD', width: 50,align:'right' },
			{ label: '存管ID', name: 'CG_USER_ID', index: '$CODE', width: 80,align:'right' }, 			
			{ label: '姓名', name: 'REALNAME', index: '$NAME', width: 80 ,align:'right'}, 			
			{ label: '手机', name: 'PHONE', index: '$COST', width: 80 ,align:'right'}, 			
			{ label: '注册时间', name: 'REGISTER_TIME', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '首投时间', name: 'DEPOSITORY_FIRSTINVEST_TIME', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '首投金额', name: 'DEPOSITORY_FIRSTINVEST_BALANCE', index: '$COST_SOURCE', width: 80 ,align:'right'}	,
			{ label: '首投期限', name: 'BORROW_PERIOD', index: '$COST_SOURCE', width: 80 ,align:'right'}
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
				url: '../basicreport/firstInvestNotMulti',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}