$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initCountTableGrid();
	initTimeCond1();
	initTimeCond2();
	initTimeCond3();
	initTimeCond4();
	initTimeCond5();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#query_cond_div1").show();
			$("#query_cond_div2").hide();
			$("#vip_count_div").hide();
			$("#vip_detail_div").show();
		}else if(select == 'vip_count'){
			$("#query_cond_div1").hide();
			$("#query_cond_div2").show();
			
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			
		}
	});
}

function initTimeCond(){
    $("#end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#end_time").val(addDate(getCurrDate(), -1));
}

function initTimeCond2(){
    $("#yingxiao_begin").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#yingxiao_begin").val(addDate(getCurrDate(), -16));
}

function initTimeCond3(){
    $("#yingxiao_end").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#yingxiao_end").val(addDate(getCurrDate(), -1));
}

function initTimeCond4(){
    $("#touzi_begin").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_begin").val(addDate(getCurrDate(), -16));
}

function initTimeCond5(){
    $("#touzi_end").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_end").val(addDate(getCurrDate(), -1));
}
function initTimeCond1(){
    $("#begin_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#begin_time").val(addDate(getCurrDate(), -10));
}

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../yunying/userred/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/userred/exportExcel2', {'params' : JSON.stringify(params)});
		}
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '查询日期', name: 'D_DATE', index: '$TYPE', width: 90,align:'right' },
			{ label: '用户id', name: 'ID', index: '$NUM', width: 90 ,align:'right'}, 			
			{ label: '用户名', name: 'USERNAME', index: '$SUM', width: 90 ,align:'right'}, 
			{ label: '用户姓名', name: 'REALNAME', index: '$BORROW_USER', width: 90 ,align:'right'}, 			
			{ label: '用户手机号', name: 'PHONE', index: '$BORROW_CAPITAL', width: 90 ,align:'right'}, 	
			{ label: '用户参与活动描述', name: 'PURPOSE', index: '$NUMM', width: 130,align:'right' },
			{ label: '红包名称', name: 'NAME', index: '$SUMM', width: 90 ,align:'right'},		
			{ label: '红包模板id', name: 'WEIZHI', index: '$AVGG', width: 90 ,align:'right'},
			{ label: '参与活动日期', name: 'HBTIME', index: '$NUMS', width: 120,align:'right' },
			{ label: '收到红包金额', name: 'HBMONEY', index: '$AVGS', width: 120 ,align:'right'},		
			{ label: '使用红包金额', name: 'USEMONEY', index: '$AVGLI', width: 120 ,align:'right'},
			
			{ label: '红包抵扣率', name: 'RATE', index: '$TYPE', width: 120,align:'right' },
			{ label: '是否现金红包', name: 'ANNUALIZED', index: '$NUM', width: 120 ,align:'right'}, 			
			{ label: '获取红包时间', name: 'RETIME', index: '$SUM', width: 120 ,align:'right'}, 
			{ label: '红包失效时间', name: 'ENDTIME', index: '$BORROW_USER', width: 120 ,align:'right'}, 			
			{ label: '红包使用次数', name: 'USERTIMES', index: '$BORROW_CAPITAL', width: 120 ,align:'right'}, 	
			{ label: '使用红包投资金额', name: 'TENDER', index: '$NUMM', width: 150,align:'right' },
			{ label: '使用红包年化投资金额', name: 'SUMTENDER', index: '$SUMM', width: 150 ,align:'right'},		
			{ label: 'ROI', name: 'ROI', index: '$ROI', width: 90 ,align:'right'},
			{ label: '年化ROI', name: 'NROI', index: '$NUMS', width: 90,align:'right' },
			{ label: '用户待收金额', name: 'AW', index: '$AVGS', width: 120 ,align:'right'},		
			{ label: '用户分类标签', name: 'TYPE', index: '$AVGLI', width: 120 ,align:'right'},
			
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$YUQI', width: 90 ,align:'right'}
					
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
//        autoScroll: false,
//        multiselect: false,
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
        },
        loadComplete:function(){
       	 loaded();//去掉遮罩
       }
    });
}

function initCountTableGrid(){
    $("#jqGrid_count").jqGrid({
        datatype: "json",
        colModel: [
			{ label: '查询日期', name: 'D_DAY', index: '$TYPE', width: 90,align:'right' },
			{ label: '用户id', name: 'ID', index: '$NUM', width: 90 ,align:'right'}, 			
			{ label: '用户名', name: 'USERNAME', index: '$SUM', width: 90 ,align:'right'}, 
			{ label: '用户姓名', name: 'REALNAME', index: '$BORROW_USER', width: 90 ,align:'right'}, 			
			{ label: '用户手机号', name: 'PHONE', index: '$BORROW_CAPITAL', width: 90 ,align:'right'}, 	
			{ label: '最近一次营销日期', name: 'ATI_TIME', index: '$NUMM', width: 130,align:'right' },
			{ label: '最近一次营销距今天数', name: 'DIF', index: '$SUMM', width: 150 ,align:'right'},		
			{ label: '获得红包个数', name: 'COUT_HB', index: '$AVGG', width: 90 ,align:'right'},
			{ label: '使用红包个数', name: 'COUNTU', index: '$AVGG', width: 90 ,align:'right'},
			{ label: '获得红包总金额', name: 'GMONEY', index: '$NUMS', width: 120,align:'right' },
			{ label: '使用红包总金额', name: 'USEMONEY', index: '$AVGS', width: 120 ,align:'right'},		
			{ label: '剩余可用红包金额', name: 'USEABLEMONEY', index: '$AVGLI', width: 120 ,align:'right'},
			
			{ label: '使用红包金额占比', name: 'MONEYI', index: '$TYPE', width: 120,align:'right' },
			{ label: '使用红包个数占比', name: 'USERTIME', index: '$TYPE', width: 120,align:'right' },
			{ label: '投资使用红包次数占比', name: 'TIMEI', index: '$NUM', width: 140 ,align:'right'}, 			
			{ label: '使用红包投资金额', name: 'TENDER', index: '$SUM', width: 120 ,align:'right'}, 
			{ label: '使用红包年化投资金额', name: 'SUMTENDER', index: '$BORROW_USER', width: 140 ,align:'right'}, 			
			{ label: '使用红包投资ROI', name: 'ROI', index: '$BORROW_CAPITAL', width: 120 ,align:'right'}, 	
			{ label: '使用红包年化投资ROI', name: 'NROI', index: '$NUMM', width: 150,align:'right' },
			{ label: '用户待收金额', name: 'AW', index: '$SUMM', width: 100 ,align:'right'},	
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$NUMS', width: 90,align:'right' },
			{ label: '用户分类标签', name: 'TYPE', index: '$ROI', width: 90 ,align:'right'}


        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
        pager: "#jqGridPager_count",
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
        },
        loadComplete:function(){
       	 loaded();//去掉遮罩
       }
    });
    $("#vip_count_div").hide();
}

function reload() {
	loading();
	var select = $("#list_select").children('option:selected').val();
	if(select == 'vip_detail'){
		$("#jqGrid").jqGrid("clearGridData");
		$("#jqGrid").jqGrid('setGridParam',{ 
			datatype:'json', 		
			url: '../yunying/userred/list',
            postData: getParams()
        }).trigger("reloadGrid");
	}else if(select == 'vip_count'){
		$("#jqGrid_count").jqGrid("clearGridData");
		console.info("sdasdsa")
		$("#jqGrid_count").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/userred/ddylist',
            postData: getParams()
        }).trigger("reloadGrid");
	}
}
function getParams(){
	var params = {
        	'end_time': $("#end_time").val(),
        	'begin_time': $("#begin_time").val(),
        	'huodong_name': $("#huodong_name").val(),
        	'hongbao_name': $("#hongbao_name").val(),
        	'hongbao_id': $("#hongbao_id").val(),
        	'user_type': $("#user_type").val(),
        	'channelName': $("#channelName").val(),
        	'userName': $("#userName").val(),
        	
        	'yingxiao_begin': $("#yingxiao_begin").val(),
        	'yingxiao_end': $("#yingxiao_end").val(),
        	'touzi_begin': $("#touzi_begin").val(),
        	'touzi_end': $("#touzi_end").val(),
        	'hongbao_begin': $("#hongbao_begin").val(),
        	'hongbao_end': $("#hongbao_end").val(),
        	'userType': $("#userType").val(),
        	'userName1': $("#userName1").val(),
        	'channelName1': $("#channelName1").val(),
        	'userId1': $("#userId1").val(),
        	'userId': $("#userId").val()
	};
	return params;
}
