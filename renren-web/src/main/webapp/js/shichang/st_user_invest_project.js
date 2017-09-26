$(function () {
	initDataGrid();
	initExportFunction();
	loadChannel2();
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
//加载渠道数据
function loadChannel2(){
	var str = '<option value="all_channel">全部渠道</option>';
    var i = 0;
    $.ajax({
        type: "POST",
        url: "../channel/channelAll/getChannel",
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(data) {
        	var selectData = [];
        	selectData.push({ id: 0, text: '全部渠道' });
        	var channelData = data.Channel;
        	for (var i = 0; i < channelData.length; i++) {
        		var channelName = channelData[i].channelName;
				selectData.push({ id: (i+1), text: channelName });
			}
        	
        	$("#id_select").select2({
        	        data: selectData,
        	        maximumSelectionLength: 1,
        	        placeholder: "请选择渠道"
        	});
        	$("#id_select").select2('val','0');
        }
   });
};

function loadChannel(){
	    $.ajax({
	        type: "POST",
	        url: "../channel/st/queryInvestProjectChannelList",
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

function queryDaishou(){
    $.ajax({
        type: "POST",
        url: "../channel/st/queryInvestProjectDaishou",
        data: JSON.stringify(getParams()),
        contentType: "application/json;charset=utf-8",
        success : function(data) {
        	console.info(data)
        	var dataMap = data.data;
        	var wait = dataMap.WAIT;
        	var liucun_rate = dataMap.liucun_rate;
        	$("#wait_div").html(formatNumber(wait, 2));
        	$("#liucun_rate_div").html(liucun_rate);
        	
        }
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
		if(!params.channelName){
			alert('渠道不能为空');
			return;
		}
		executePost('../channel/st/exportInvestProjectExcel', {'params' : JSON.stringify(params)});
	});

}

function initDataGrid(){
    $("#jqGrid_day").jqGrid({
//        url: '../channel/st/investProjectList',
        datatype: "json",
        colModel: [			
		    { label: '项目期限', name: '项目期限', width: 80, key: true,align: 'right' },
        	{ label: '用户人数', name: '用户人数', width: 80, key: true,align: 'right', 
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue;
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    },
        	{ label: '占总首投用户比例', name: '占总首投用户比例', width: 80, key: true,align: 'right'},
        	{ label: '投资金额', name: '投资金额', width: 80, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue;
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    },
		    { label: '投资额占比', name: '投资额占比', width: 80, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue;
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    },
        	{ label: '投资次数', name: '投资次数', width: 80, key: true,align: 'right', 
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue;
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    },
        	{ label: '红包使用金额', name: '红包使用金额', width: 80, key: true,align: 'right', 
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue;
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    },
        	{ label: '红包金额占比', name: '红包金额占比', width: 80, key: true,align: 'right', 
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue;
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    },
        	{ label: '红包使用次数', name: '红包使用次数', width: 80, key: true,align: 'right',
		    	formatter:function(cellvalue, options, rowObject){
		    		if(cellvalue && cellvalue> 1000){
		    			return formatNumber(cellvalue,2);
		    		}else{
		    			if(cellvalue){
		    				return cellvalue + "  ";
		    			}else{
		    				return '';
		    			}
		    		}
		    	} 
		    }
        ],
        height:  $(window).height()-150,
        rowNum: 1000,
//		rowList : [10,30,50],
//        rownumbers: true, 
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
        },
        loadComplete: function (){
        	var ids = $(this).getDataIDs();
            for(var i=0;i<ids.length;i++){
                var rowData = $(this).getRowData(ids[i]);
                if(rowData.项目期限 == '新手标'){//如果等于新手标，则背景色置灰显示
                    $('#'+ids[i]).find("td").addClass("SelectBG");
                }
            }
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
			if(!params.channelName){
				alert('渠道不能为空');
				return;
			}
			$("#jqGrid_day").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../channel/st/investProjectList',
	            postData: getParams()
            }).trigger("reloadGrid");
			queryDaishou();
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
	params.channelName = $("#id_select").select2('data')[0].text;
//	params.channelName = $("#id_select").text();
	return params;
}