$(function () {
	initTotalTableGrid();
	initTimeCond();
	initExportFunction();
	initDetailTableGrid();
	initTabEvent();
//	loadChannel();
});

var currTab = 'total';//默认是统计页 切换之后是detail页

function initTabEvent(){
	$('.nav-tabs li').click(function(){
		　$(this).addClass('active').siblings().removeClass('active');
		　　var _id = $(this).attr('data-id');
		　　$('.tabs-contents').find('#'+_id).addClass('active').siblings().removeClass('active');
		　　switch(_id){
		　　　　case "tabContent1":
				currTab = 'total';
		     	$("#total_div").show();
		     	$("#detail_div").hide();
		　　　　　　break;
		　　　　case "tabContent2":
				currTab = 'detail';
				$("#total_div").hide();
		     	$("#detail_div").show();
		　　　　　　break;
		　　　　default:
		　　　　　　break;
		　　}
	});	
}

function clickTab(){
//	　$("#tab_div").addClass('active').siblings().removeClass('active');
	
	　$("#total_tab_div").removeClass('active');
	　$("#detail_tab_div").addClass('active');
	　　var _id = $("#detail_tab_div").attr('data-id');
//	　　$("#detail_tab_div").addClass('active').siblings().removeClass('active');
	　　switch(_id){
	　　　　case "tabContent1":
			currTab = 'total';
	     	$("#total_div").show();
	     	$("#detail_div").hide();
	　　　　　　break;
	　　　　case "tabContent2":
			currTab = 'detail';
			$("#total_div").hide();
	     	$("#detail_div").show();
	　　　　　　break;
	　　　　default:
	　　　　　　break;
	　　}
}

function initTimeCond(){
    $("#registerStartTime").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#registerEndTime").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#firstInvestStartTime").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#firstInvestEndTime").datetimepicker({
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
		if(currTab == 'total'){
			executePost('../channel/channel_stat/exportTotalExcel', {'params' : JSON.stringify(params)});  
		}else if(currTab == 'detail'){
			executePost('../channel/channel_stat/exportDetailExcel', {'params' : JSON.stringify(params)});  
		}
	});

}

function queryTotalInfo(){
	 $.ajax({
		    type: "POST",
		    url: '../channel/channel_stat/total',
		    data: JSON.stringify(getParams()),
		    contentType: "application/json;charset=utf-8",
		    success : function(data) {
		    	var msg = data.total;
		    	$("#register_user_num").html(msg.注册人数);
		    	$("#first_invest_user_num").html(msg.首投人数);
		    	$("#first_invest_rate").html(formatNumber(msg.转化率*100,2) + "%");
		    	$("#first_invest_amount").html(formatNumber(msg.首投金额, 2));
		    	$("#total_invest_amount").html(formatNumber(msg.累投金额, 2));
		    	$("#multi_invest_user_num").html(msg.复投人数);
		    	$("#multi_invest_rate").html(formatNumber(msg.复投率*100,2) + "%");
		    }
	 });
}

function resetTotalInfo(){
	var cc = '-';
	$("#register_user_num").html(cc);
	$("#first_invest_user_num").html(cc);
	$("#first_invest_rate").html(cc);
	$("#first_invest_amount").html(cc);
	$("#total_invest_amount").html(cc);
	$("#multi_invest_user_num").html(cc);
	$("#multi_invest_rate").html(cc);
}

function initTotalTableGrid(){
	   $("#jqGrid").jqGrid({
//	        url: '../channel/channel_stat/list',
	        datatype: "json",
	        colModel: [			
				{ label: '渠道名称', name: 'channel_name', index: '$STAT_PERIOD', width: 150 ,align:'right',
					formatter:function(value, options, row){
						if(!value){
							value = '未知';
						}
						var picHtml = '<a onclick="queryChannelDetail(\''+row.channel_label+'\',\''+value+'\')" style="text-decoration:underline;color: black"><font color="black" style="font-size: 12px;font-weight: normal">'+value+'</font></a>';
						return picHtml;
					} 
				},	
				{ label: '渠道标记', name: 'channel_label', index: '$USERNAME', width: 150 ,align:'right'}, 			
				{ label: '注册人数', name: '注册人数', index: '$REALNAME', width: 80,align:'right' },		
				{ label: '首投人数', name: '首投人数', index: '$PHONE', width: 80,align:'right' },
				{ label: '转化率', name: '转化率', index: '$PHONE', width: 80,align:'right' },
				{ label: '复投人数', name: '复投人数', index: '$PHONE', width: 80,align:'right' },
				{ label: '复投率', name: '复投率', index: '$PHONE', width: 80,align:'right' },
				{ label: '首投金额（万元）', name: '首投金额（万元）', index: '$PHONE', width: 120,align:'right' },
				{ label: '累投金额（万元）', name: '累投金额（万元）', index: '$PHONE', width: 120,align:'right' },
				{ label: '复投金额（万元）', name: '复投金额（万元）', index: '$PHONE', width: 120,align:'right' },
				{ label: '首投年化投资金额（万元）', name: '首投年化投资金额（万元）', index: '$PHONE', width: 180,align:'right' },
				{ label: '年化累投金额（万元）', name: '年化累投金额（万元）', index: '$PHONE', width: 150,align:'right' },
				{ label: '年化复投金额（万元）', name: '年化复投金额（万元）', index: '$PHONE', width: 150,align:'right' },
				{ label: '待收>100人数', name: '待收>100人数', index: '$PHONE', width: 120,align:'right' }
	        ],				

			viewrecords: true,
	        height: $(window).height()-205,
	        rowNum: 20,
	        rownumbers: true, 
	        rownumWidth: 25, 
	        autowidth:true,
	        shrinkToFit: false,
	        autoScroll: false,
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

function queryChannelDetail(channelLabel, channelName){
	clickTab();
	var params = getParams();
	params.channelLabel = channelLabel;
	params.channelName = channelName;
	$("#channel_name").val(channelName);
	$("#jqGrid_detail").jqGrid("clearGridData");
	$("#jqGrid_detail").jqGrid('setGridParam',{ 
		datatype:'json', 
		url: '../channel/channel_stat/detailList',
        postData: params
    }).trigger("reloadGrid");
}

function initDetailTableGrid(){
	   $("#jqGrid_detail").jqGrid({
//	        url: '../channel/channel_stat/list',
	        datatype: "json",
	        colModel: [			
				{ label: '用户ID', name: 'user_id', index: '$STAT_PERIOD', width: 120 ,align:'right'},
				{ label: '用户名', name: 'user_name', index: '$USERNAME', width: 120 ,align:'right'}, 			
				{ label: '渠道名称', name: 'channel_name', index: '$REALNAME', width: 150,align:'right'},
				{ label: '渠道标记', name: 'activity_tag', index: '$PHONE', width: 150,align:'right' },
				{ label: '注册时间', name: 'register_time', index: '$PHONE', width: 150,align:'right' },
				{ label: '操作平台', name: 'register_from', index: '$PHONE', width: 80,align:'right' },
				{ label: '实名', name: '实名', index: '$PHONE', width: 80,align:'right' },
				{ label: '绑卡', name: '绑卡', index: '$PHONE', width: 80,align:'right' },
				{ label: '首投时间', name: 'st_time', index: '$PHONE', width: 150,align:'right' },
				{ label: '首投金额（元）', name: 'st_money', index: '$PHONE', width: 180,align:'right' },
				{ label: '首投期限（天）', name: 'days', index: '$PHONE', width: 150,align:'right' },
				{ label: '首投年化投资金额', name: '首投年化投资金额', index: '$PHONE', width: 150,align:'right' },
				{ label: '复投金额', name: '复投金额', index: '$PHONE', width: 120,align:'right' },
				{ label: '投资次数', name: '投资次数', index: '$PHONE', width: 120,align:'right' },
				{ label: '累投金额', name: '累投金额', index: '$PHONE', width: 120,align:'right' },
				{ label: '年化累投金额', name: '年化累投金额', index: '$PHONE', width: 120,align:'right' },
				{ label: '年化复投金额', name: '年化复投金额', index: '$PHONE', width: 120,align:'right' },
				{ label: '账户资产', name: '账户资产', index: '$PHONE', width: 120,align:'right' }
				
	        ],				

			viewrecords: true,
			 height: $(window).height()-205,
	        rowNum: 20,
	        rownumbers: true, 
	        rownumWidth: 25, 
	        autowidth:true,
	        shrinkToFit: false,
	        autoScroll: false,
//	        multiselect: true,
	        pager: "#jqGridPager_detail",
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
	   
	   $("#detail_div").hide();
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
			resetTotalInfo();
			vm.showList = true;
			queryTotalInfo();
			if(currTab == 'total'){
				$("#jqGrid").jqGrid("clearGridData");
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../channel/channel_stat/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(currTab == 'detail'){
				$("#jqGrid_detail").jqGrid("clearGridData");
				$("#jqGrid_detail").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../channel/channel_stat/detailList',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
			
		}
	}
});
function loadChannel(){
	   var str = '';
	    var i = 0;
	    $.ajax({
	        type: "POST",
	        url: "../channel/channelAll/getChannel",
	        data: JSON.stringify(),
	        contentType: "application/json;charset=utf-8",
	        success : function(msg) {
	            for(var list in msg.Channel){
	                for(var key in msg.Channel[list]){
	                    if(key == "channelName")
	                        str += '<option value="'+(i++)+'">' + msg.Channel[list][key] + "</option>";
	                }
	            }

	            $("#id_select").select2({
	                maximumSelectionLength: 3,
	                width:'200'
	            });
	            $("#id_select").append(str);
	        }
	     });

};
//获取渠道信息
function getChannelName(){
    var arrStr = '';
    $(".select2-selection__choice").each(function(){
        arrStr += $(this).attr("title") + ",";
    });
    if(arrStr.indexOf(",") > 0){
    	return arrStr.substring(0, arrStr.length-1);
    }
    return arrStr;
};
function getParams(){
	var params = {
        	'registerStartTime': $("#registerStartTime").val(),
        	'registerEndTime': $("#registerEndTime").val(),
        	'firstInvestStartTime': $("#firstInvestStartTime").val(),
        	'firstInvestEndTime': $("#firstInvestEndTime").val(),
        	'channelName' : $("#channel_name").val()
	};
	return params;
}