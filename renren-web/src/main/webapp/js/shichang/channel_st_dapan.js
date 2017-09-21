$(function () {
	initDataGrid();
	initExportFunction();
	loadChannel();
});

function loadChannel(){
	    $.ajax({
	        type: "POST",
	        url: "../channel/st/queryChannelList",
	        data: JSON.stringify({}),
	        contentType: "application/json;charset=utf-8",
	        success : function(data) {
	        	var channelList = data.channel_list;
	        	var html = "";
	        	for (var i = 0; i < channelList.length; i++) {
					var channelObj = channelList[i];
					if('全部渠道' == channelObj.CHANNEL_NAME){
						html += '<option value ="全部渠道" selected="selected">全部渠道</option>';
					}else{
						html += '<option value ="'+channelObj.CHANNEL_NAME+'">'+channelObj.CHANNEL_NAME+'</option>';
					}
				}
	            $("#list_select").html(html);
	        }
	     });

}

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../channel/st/exportExcel', {'params' : JSON.stringify(params)});
	});

}

function initDataGrid(){
    $("#jqGrid_day").jqGrid({
        url: '../channel/st/list',
        datatype: "json",
        colModel: [			
		    { label: '月份', name: '月份', width: 130, key: true },
        	{ label: '201701', name: '201701', width: 80, key: true, 
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100, 2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201702', name: '201702', width: 80, key: true, 
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201703', name: '201703', width: 80, key: true,
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201704', name: '201704', width: 80, key: true, 
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201705', name: '201705', width: 80, key: true, 
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201706', name: '201706', width: 80, key: true, 
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201707', name: '201707', width: 80, key: true,
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
		    },
        	{ label: '201708', name: '201708', width: 80, key: true, 
		    	formatter:function(cellvalue, options, rowObject){
		    		var dataValue = rowObject['月份'];
		    		if(dataValue.indexOf('率') > 0 || dataValue.indexOf('占') > 0 && cellvalue && cellvalue > 0){
		    			return formatNumber(cellvalue * 100,2) + "%";
		    		}
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			return cellvalue;
		    		}
		    	} 
        	}
//        	,
//        	{ label: '201709', name: '201709', width: 80, key: true, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} },
//        	{ label: '201710', name: '201710', width: 80, key: true, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} },
//        	{ label: '201711', name: '201711', width: 80, key: true, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} },
//        	{ label: '201712', name: '201712', width: 80, key: true, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}} }
//			{ label: '渠道充值', name: 'cRecharge', index: '$M_INV_MONEY', width: 160, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }
        ],
        height:  $(window).height()-130,
        rowNum: 1000,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
		viewrecords: true,
		rowList : [10,30,50],
        pager: "#jqGridPager_day",
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
        	$("#jqGrid_day").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
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
			$("#jqGrid_day").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../channel/st/list',
	            postData: getParams()
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
	params.channelName = $("#list_select").val();
	return params;
}