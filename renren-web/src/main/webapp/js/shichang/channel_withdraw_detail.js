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
		executePost('../channel/st/exportWithdrawDetailExcel', {'params' : JSON.stringify(params)});
	});

}

function initDataGrid(){
    $("#jqGrid").jqGrid({
        datatype: "json",
        colModel: [			
        	{ label: '用户ID', name: '用户ID', width: 100, key: true,align: 'right' },
		    { label: '渠道名称', name: '渠道名称', width: 100, key: true,align: 'right' },
		    { label: '渠道标记', name: '渠道标记', width: 100, key: true,align: 'right' },
		    { label: '类型', name: '类型', width: 100, key: true,align: 'right' },
		    { label: '付费方式', name: '付费方式', width: 100, key: true,align: 'right' },
		    { label: '注册日期', name: '注册日期', width: 100, key: true,align: 'right' },
		    { label: '首投日期', name: '首投日期', width: 100, key: true,align: 'right' },
		    { label: '周期内投资金额', name: '周期内投资金额', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return formatNumber(cellvalue,2);
		    	} 
		    },
		    { label: '周期内投资次数', name: '周期内投资次数', width: 130, key: true,align: 'right'},
		    { label: '周期内红包使用金额', name: '周期内红包使用金额', width: 150, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return formatNumber(cellvalue,2);
		    	} 
		    },
		    
		    { label: '周期末日待收', name: '周期末日待收', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return formatNumber(cellvalue,2);
		    	} 
		    },
		    { label: '周期内提现金额', name: '周期内提现金额', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return formatNumber(cellvalue,2);
		    	} 
		    },
		    { label: '周期内提现次数', name: '周期内提现次数', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return cellvalue + '     ';
		    	} 
		    }
        	
        ],
        height:  $(window).height()-130,
        rowNum: 20,
//		rowList : [10,30,50],
//        rownumbers: true, 
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
				url: '../channel/st/channelWithdrawDetailList',
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