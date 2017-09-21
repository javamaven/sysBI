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
		executePost('../channel/st/exportWithdrawExcel', {'params' : JSON.stringify(params)});
	});

}

function initDataGrid(){
    $("#jqGrid").jqGrid({
        datatype: "json",
        colModel: [			
		    { label: '渠道名称', name: '渠道名称', width: 130, key: true },
		    { label: '渠道标记', name: '渠道标记', width: 130, key: true },
		    { label: '周期内提现用户', name: '周期内提现用户', width: 130, key: true },
		    { label: '周期末日待收', name: '周期末日待收', width: 130, key: true },
		    { label: '周期内提现金额', name: '周期内提现金额', width: 130, key: true },
        	{ label: '周期内投资总金额', name: '周期内投资总金额', width: 80, key: true},
        	
        	{ label: '提现占投资比例', name: '提现占投资比例', width: 80, key: true},
        	{ label: '人均提现金额', name: '人均提现金额', width: 80, key: true},
        	{ label: '当月首投用户占比', name: '当月首投用户占比', width: 80, key: true},
        	{ label: '上月首投用户占比', name: '上月首投用户占比', width: 80, key: true},
        	
        	{ label: '两月前首投用户占比', name: '两月前首投用户占比', width: 80, key: true},
        	{ label: '三月前首投用户占比', name: '三月前首投用户占比', width: 80, key: true},
        	{ label: '其他用户占比', name: '其他用户占比', width: 80, key: true}
        	
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
				url: '../channel/st/channelWithdrawList',
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