$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
});

function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../yunying/dmreportddzremain/exportExcel', {'params' : JSON.stringify(params)});  
	});

}

function initTableGrid(){
	   $("#jqGrid").jqGrid({
//	        url: '../yunying/dmreportddzremain/list',
	        datatype: "json",
	        colModel: [			
				{ label: '时间', name: 'statPeriod', index: '$STAT_PERIOD', width: 70 ,align:'right'},
				{ label: '用户名', name: 'username', index: '$USERNAME', width: 80 ,align:'right'}, 			
				{ label: '用户姓名', name: 'realname', index: '$REALNAME', width: 80,align:'right' },		
				{ label: '电话', name: 'phone', index: '$PHONE', width: 80,align:'right' }, 			
				{ label: '注册时间', name: 'regTime', index: '$REG_TIME', width: 80 ,align:'right'}, 			
				{ label: '点点赚余额', name: 'availableAmount', index: '$AVAILABLE_AMOUNT_', width: 80,align:'right' }, 			
				{ label: '点点赚持有天数', name: 'cou', index: '$COU', width: 80 ,align:'right'}, 			
				{ label: '项目累计投资额', name: 'xmInvMoney', index: '$XM_INV_MONEY', width: 100,align:'right' }, 			
				{ label: '是否内部员工', name: 'isInternal', index: '$IS_INTERNAL', width: 80,align:'right' }, 			
				{ label: '用户是否内部员工邀请', name: 'isInternalTuijian', index: '$IS_INTERNAL_TUIJIAN', width: 130 ,align:'right'} 			
	        ],				

			viewrecords: true,
	        height: 390,
	        rowNum: 10,
//			rowList : [10,30,50],
	        rownumbers: true, 
	        rownumWidth: 25, 
	        autowidth:true,
//	        multiselect: true,
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
		dmReportDdzRemain: {}
	},
	methods: {
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../yunying/dmreportddzremain/list',
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