$(function () {
	initDataGrid();
	initExportFunction();
	initTime();
});

function initTime(){
	 $("#start_date").datetimepicker({
	        format: 'yyyy-mm-dd',
	        minView:'month',
	        language: 'zh-CN',
	        autoclose:true,
	        endDate: getYesterday()
	  }).on("click",function(){
	  });
	 $("#end_date").datetimepicker({
	        format: 'yyyy-mm-dd',
	        minView:'month',
	        language: 'zh-CN',
	        autoclose:true,
	        endDate: getYesterday()
	  }).on("click",function(){
	  });	 
}

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		if(!params.startDate){
			alert('开始日期不能为空');
			return;
		}
		if(!params.endDate){
			alert('结束日期不能为空');
			return;
		}
		executePost('../channel/st/exportZhiliangExcel', {'params' : JSON.stringify(params)});
	});

}

function initDataGrid(){
    $("#jqGrid").jqGrid({
//        url: '../channel/st/channelZhiliangList',
        datatype: "json",
        colModel: [			
		    { label: '渠道名称', name: '渠道名称', width: 130, key: true },
		    { label: '渠道标记', name: '渠道标记', width: 130, key: true },
		    { label: '类型', name: '类型', width: 130, key: true },
		    { label: '付费方式', name: '付费方式', width: 130, key: true },
		    { label: '注册用户', name: '注册用户', width: 130, key: true },
//		    { label: '首投用户', name: '首投用户', width: 130, key: true },//fst_user_cnt
		    { label: '首投用户', name: 'FST_USER_CNT', width: 130, key: true },
        	{ label: '首投用户占比', name: '首投用户占比', width: 80, key: true},
        	
        	{ label: '新手标用户', name: '新手标用户', width: 80, key: true},
        	{ label: '首投用户投资金额', name: '首投用户投资金额', width: 80, key: true},
        	{ label: '首投用户红包使用金额', name: '首投用户红包使用金额', width: 80, key: true},
        	{ label: '新手标投资额', name: '新手标投资额', width: 80, key: true},
        	
        	{ label: '新手标红包使用金额', name: '新手标红包使用金额', width: 80, key: true},
        	{ label: '首投用户投资次数', name: '首投用户投资次数', width: 80, key: true},
        	{ label: '首投用户红包使用次数', name: '首投用户红包使用次数', width: 80, key: true},
        	
        	{ label: '周期内末日待收金额', name: '周期内末日待收金额', width: 80, key: true},
        	{ label: '人均待收金额', name: '人均待收金额', width: 80, key: true},
        	{ label: '人均投资金额', name: '人均投资金额', width: 80, key: true},
        	{ label: '人均红包使用金额', name: '人均红包使用金额', width: 80, key: true}
        	
//        	{ label: '201708', name: '201708', width: 80, key: true, 
//		    	formatter:function(cellvalue, options, rowObject){
//		    		var dataValue = rowObject['月份'];
//		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0){
//		    			return cellvalue * 100 + "%";
//		    		}
//		    		if(cellvalue && cellvalue> 1000){
//		    			return formatNumber(cellvalue,2);
//		    		}else{
//		    			return cellvalue;
//		    		}
//		    	} 
//        	}
        ],
        height:  $(window).height()-130,
        rowNum: 1000,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: false,
        autoScroll: false,
		viewrecords: true,
		rowList : [10,30,50],
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
		dmReportDalilyMarketing: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			vm.showList = true;
			var params = getParams();
			if(!params.startDate){
				alert('开始日期不能为空');
				return;
			}
			if(!params.endDate){
				alert('结束日期不能为空');
				return;
			}
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../channel/st/channelZhiliangList',
	            postData: params
            }).trigger("reloadGrid");
		}
	}
});

//获取渠道信息
function getChannelName(){
    var arrStr = new Array();
    $(".select2-selection__choice").each(function(){
        arrStr.push($(this).attr("title"))
        });
    return  arrStr;
};

function getParams(){
	var params = {};
	params.startDate = $("#start_date").val();
	params.endDate = $("#end_date").val();
	return params;
}