$(function () {

//	var brow = $.browser;
//	var bInfo = "";
//	console.info(brow)
//	if (brow.msie) {
//		bInfo = "MicrosoftInternetExplorer" + brow.version;
//	}
//	if (brow.mozilla) {
//		bInfo = "MozillaFirefox" + brow.version;
//	}
//	if (brow.safari) {
//		bInfo = "AppleSafari" + brow.version;
//	}
//	if (brow.opera) {
//		bInfo = "Opera" + brow.version;
//	}
//	alert(bInfo);
	
	browser = getBrowserType();
	initDataGrid();
	initExportFunction();
	initTime();
});
var browser = "";
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
		    { label: '渠道名称', name: '渠道名称', width: 130, key: true,align: 'right',frozen:true },
		    { label: '渠道标记', name: '渠道标记', width: 130, key: true,align: 'right',frozen:true  },
		    { label: '周期内提现用户', name: '周期内提现用户', width: 130, key: true,align: 'right' },
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
        	{ label: '周期内投资总金额', name: '周期内投资总金额', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return formatNumber(cellvalue,2);
		    	} 
		    },
        	{ label: '提现占投资比例', name: '提现占投资比例', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue*100,2) + '%';
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
        	{ label: '人均提现金额', name: '人均提现金额', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		return formatNumber(cellvalue,2);
		    	} 
		    },
        	{ label: '当月首投用户占比', name: '当月首投用户占比', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue*100,2) + '%';
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
        	{ label: '上月首投用户占比', name: '上月首投用户占比', width: 130, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue*100,2) + '%';
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
        	
        	{ label: '两月前首投用户占比', name: '两月前首投用户占比', width: 140, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue*100,2) + '%';
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
        	{ label: '三月前首投用户占比', name: '三月前首投用户占比', width: 140, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue*100,2) + '%';
		    		}else{
		    			return '';
		    		}
		    	} 
		    },
        	{ label: '其余', name: '其他用户占比', width: 80, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue){
		    			return formatNumber(cellvalue*100,2) + '%';
		    		}else{
		    			return '';
		    		}
		    	} 
        	}
        	
        ],
        height:  $(window).height()-130,
        rowNum: 1000,
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
        },
        loadComplete: function (){
//        	 resetHeight("jqGrid");
//        	 hackHeight("#jqGrid");
        }
        
    });
    $("#jqGrid").jqGrid('setFrozenColumns');
//    resetHeight("jqGrid");
//    hackHeight("#jqGrid");
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

/**
 * 获取浏览器类型
 * @returns
 */
function getBrowserType(){
	var explorer = navigator.userAgent ;
	//ie 
	if (explorer.indexOf("MSIE") >= 0) {
		return "ie";
	}
	//firefox 
	else if (explorer.indexOf("Firefox") >= 0) {
	    return "Firefox";
	}
	//Chrome
	else if(explorer.indexOf("Chrome") >= 0){
	 	return "Chrome";
	}
	//Opera
	else if(explorer.indexOf("Opera") >= 0){
		return "Opera";
	}
	//Safari
	else if(explorer.indexOf("Safari") >= 0){
		return "Safari";
	} 
	//Netscape
	else if(explorer.indexOf("Netscape")>= 0) { 
		return "Netscape";
	} 
}
function hackHeight(listId) {
	console.info(1111)
    $(listId + '_frozen tr').slice(1).each(function() {
 
        var rowId = $(this).attr('id');
 
        var frozenTdHeight = parseFloat($('td:first', this).height());
        var normalHeight = parseFloat($(listId + ' #' + $(this).attr('id')).find('td:first').height());
 
        // 如果冻结的列高度小于未冻结列的高度则hack之
        if (frozenTdHeight < normalHeight) {
 
            $('td', this).each(function() {
 
                /*
                 浏览器差异高度hack
                 */
                var space = 0; // opera默认使用0就可以
                if (browser == 'Chrome') {
                    space = 0.6;
                } else if (browser == 'ie') {
                    space = -0.2;
                } else if (browser == 'Firefox') {
                    space = 0.5;
                }
 
                if (!$(this).attr('style') || $(this).attr('style').indexOf('height:') == -1) {
                    $(this).attr('style', $(this).attr('style') + ";height:" + (normalHeight + space) + "px !important");
                }
            });
        }
    });
}
/**
 * 冻结列导致高度不一致问题
 * @param gridId
 * @returns
 */
function resetHeight(gridId){
	var browser = getBrowserType();
    var divTop = -1;
    var bdivTop = -1;
//    divTop = -0.5;
//    bdivTop = -0.5;
    // opera 不需要hack
//    if (browser == 'Opera') {
        divTop = 0;
        bdivTop = 0;
//    }
    console.info("+++++++browser+++++++"+browser)
    $('#gview_' + gridId + ' .frozen-div').css({
        top: $('#gview_' + gridId + ' .frozen-div').position().top + divTop
    });
    $('#gview_' + gridId + ' .frozen-bdiv').css({
        top: $('#gview_' + gridId + ' .frozen-bdiv').position().top + bdivTop
    });
}

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