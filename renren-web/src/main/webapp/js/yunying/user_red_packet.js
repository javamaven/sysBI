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
	loadChannel();
	loadChannel2();
	loadChannel3();
	loadChannel4();
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
		console.info('++++btn_exports+++')
		executePost('../yunying/userred/exportExcel', {'params' : JSON.stringify(params)});
	});
	$('#btn_exports2').click(function(){
		var params = getParams();
		console.info('++++btn_exports2+++')
		executePost('../yunying/userred/exportExcel2', {'params' : JSON.stringify(params)});
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '查询日期', name: 'D_DATE', index: '$TYPE', width: 90,align:'right',sortable:false},
			{ label: '用户id', name: 'ID', index: '$NUM', width: 90 ,align:'right',sortable:false}, 			
			{ label: '用户名', name: 'USERNAME', index: '$SUM', width: 90 ,align:'right',sortable:false}, 
			{ label: '用户姓名', name: 'REALNAME', index: '$BORROW_USER', width: 90 ,align:'right',sortable:false}, 			
			{ label: '用户手机号', name: 'PHONE', index: '$BORROW_CAPITAL', width: 90 ,align:'right',sortable:false}, 	
			{ label: '用户参与活动描述', name: 'PURPOSE', index: '$NUMM', width: 130,align:'right' ,sortable:false},
			{ label: '红包名称', name: 'NAME', index: '$SUMM', width: 90 ,align:'right',sortable:false},		
			{ label: '红包模板id', name: 'WEIZHI', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '参与活动日期', name: 'HBTIME', index: '$NUMS', width: 120,align:'right' ,sortable:false},
			{ label: '收到红包金额', name: 'HBMONEY', index: '$AVGS', width: 120 ,align:'right',sortable:false},		
			{ label: '使用红包金额', name: 'USEMONEY', index: '$AVGLI', width: 120 ,align:'right',sortable:false},
			
			{ label: '红包抵扣率', name: 'RATE', index: '$TYPE', width: 120,align:'right' ,sortable:false},
			{ label: '是否现金红包', name: 'ANNUALIZED', index: '$NUM', width: 120 ,align:'right',sortable:false}, 			
			{ label: '获取红包时间', name: 'RETIME', index: '$SUM', width: 120 ,align:'right',sortable:false}, 
			{ label: '红包失效时间', name: 'ENDTIME', index: '$BORROW_USER', width: 120 ,align:'right',sortable:false}, 			
			{ label: '红包使用次数', name: 'USERTIMES', index: '$BORROW_CAPITAL', width: 120 ,align:'right',sortable:false}, 	
			{ label: '使用红包投资金额', name: 'TENDER', index: '$NUMM', width: 150,align:'right' ,sortable:false},
			{ label: '使用红包年化投资金额', name: 'SUMTENDER', index: '$SUMM', width: 150 ,align:'right',sortable:false},		
			{ label: 'ROI', name: 'ROI', index: '$ROI', width: 90 ,align:'right',sortable:false},
			{ label: '年化ROI', name: 'NROI', index: '$NUMS', width: 90,align:'right',sortable:false },
			{ label: '用户待收金额', name: 'AW', index: '$AVGS', width: 120 ,align:'right',sortable:false},		
			{ label: '用户分类标签', name: 'TYPE', index: '$AVGLI', width: 120 ,align:'right',sortable:false},
			
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$YUQI', width: 90 ,align:'right',sortable:false}
					
        ],
		viewrecords: true,  //页面的 页码和页数和总数的显示
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


//加载活动名称
function loadChannel2(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/hongbaohuodong',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.hongbao){
                for(var key in msg.hongbao[list]){
                    if(key == "PURPOSE")
                        str += '<option value="'+(i++)+'">' + msg.hongbao[list][key] + "</option>";
                }
            };
            $("#huodong_name").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#huodong_name").append(str);
        }
     });
};
//加载红包名称
function loadChannel3(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/hongbaoname',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.hongbaoname){
                for(var key in msg.hongbaoname[list]){
                    if(key == "NAME")
                        str += '<option value="'+(i++)+'">' + msg.hongbaoname[list][key] + "</option>";
                }
            };
            $("#hongbao_name").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#hongbao_name").append(str);
        }
     });
};

//加载用户分类标签
function loadChannel4(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/userbiaoqian',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.userbiaoqian){
                for(var key in msg.userbiaoqian[list]){
                    if(key == "USER_LEVEL")
                        str += '<option value="'+(i++)+'">' + msg.userbiaoqian[list][key] + "</option>";
                }
            };
            $("#user_type").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#user_type").append(str);
            $("#userType").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#userType").append(str);
        }
     });
};

//加载渠道数据
function loadChannel(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: "../channel/queryChannelNameByAuth",
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
//            console.log(msg);
            for(var list in msg.Channel){
                for(var key in msg.Channel[list]){
                    if(key == "channelName")
                        str += '<option value="'+(i++)+'">' + msg.Channel[list][key] + "</option>";
                }
            };
            $("#id_select").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#id_select").append(str);
            $("#channelName1").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#channelName1").append(str);
        }
     });
};

//获取渠道信息
function getChannelName(){
    var arrStr = new Array();
    $("#id_select").each(function(){
        arrStr.push($(this).attr("title"))
        });
    return  arrStr;
};	
/**
 * 获取选中的值
 * @param id 
 * @returns
 */
function getSelectData(id){
    var hongbao = '';
    var hongbaoname='';
    var userbiaoqian='';
    var selectData = $("#" + id).select2("data");
    for (var i = 0; i < selectData.length; i++) {
		var select = selectData[i];
		hongbao += "'" + select.text + "',";
		hongbaoname += "'" + select.text + "',";
		userbiaoqian += "'" + select.text + "',";
	}
    if(hongbao.indexOf(",") >= 0){
    	hongbao = hongbao.substring(0, hongbao.length-1);
    	hongbaoname = hongbaoname.substring(0, hongbaoname.length-1);
    	userbiaoqian = userbiaoqian.substring(0, userbiaoqian.length-1);
    }
    return  hongbao;
};

function initCountTableGrid(){
    $("#jqGrid_count").jqGrid({
        datatype: "json",
        colModel: [
			{ label: '查询日期', name: 'D_DAY', index: '$TYPE', width: 90,align:'right' ,sortable:false},
			{ label: '用户id', name: 'ID', index: '$NUM', width: 90 ,align:'right',sortable:false}, 			
			{ label: '用户名', name: 'USERNAME', index: '$SUM', width: 90 ,align:'right',sortable:false}, 
			{ label: '用户姓名', name: 'REALNAME', index: '$BORROW_USER', width: 90 ,align:'right',sortable:false}, 			
			{ label: '用户手机号', name: 'PHONE', index: '$BORROW_CAPITAL', width: 90 ,align:'right',sortable:false}, 	
			{ label: '最近一次营销日期', name: 'ATI_TIME', index: '$NUMM', width: 130,align:'right' ,sortable:false},
			{ label: '最近一次营销距今天数', name: 'DIF', index: '$SUMM', width: 150 ,align:'right',sortable:false},		
			{ label: '获得红包个数', name: 'COUT_HB', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '使用红包个数', name: 'COUNTU', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '获得红包总金额', name: 'GMONEY', index: '$NUMS', width: 120,align:'right',sortable:false },
			{ label: '使用红包总金额', name: 'USEMONEY', index: '$AVGS', width: 120 ,align:'right',sortable:false},		
			{ label: '剩余可用红包金额', name: 'USEABLEMONEY', index: '$AVGLI', width: 120 ,align:'right',sortable:false},
			
			{ label: '使用红包金额占比', name: 'MONEYI', index: '$TYPE', width: 120,align:'right' ,sortable:false},
			{ label: '使用红包个数占比', name: 'USERTIME', index: '$TYPE', width: 120,align:'right',sortable:false },
			{ label: '投资使用红包次数占比', name: 'TIMEI', index: '$NUM', width: 140 ,align:'right',sortable:false}, 			
			{ label: '使用红包投资金额', name: 'TENDER', index: '$SUM', width: 120 ,align:'right',sortable:false}, 
			{ label: '使用红包年化投资金额', name: 'SUMTENDER', index: '$BORROW_USER', width: 140 ,align:'right',sortable:false}, 			
			{ label: '使用红包投资ROI', name: 'ROI', index: '$BORROW_CAPITAL', width: 120 ,align:'right',sortable:false}, 	
			{ label: '使用红包年化投资ROI', name: 'NROI', index: '$NUMM', width: 150,align:'right' ,sortable:false},
			{ label: '用户待收金额', name: 'AW', index: '$SUMM', width: 100 ,align:'right',sortable:false},	
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$NUMS', width: 90,align:'right',sortable:false },
			{ label: '用户分类标签', name: 'TYPE', index: '$ROI', width: 90 ,align:'right',sortable:false}


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
//        	'huodong_name': $("#huodong_name").val(),
        	huodong_name : getSelectData("huodong_name"),
//        	'hongbao_name': $("#hongbao_name").val(),
        	'hongbao_id': $("#hongbao_id").val(),
//        	'user_type': $("#user_type").val(),
//        	'channelName': $("#channelName").val(),
        	user_type : getSelectData("user_type"),
        	channelName : getSelectData("id_select"),
        	hongbao_name : getSelectData("hongbao_name"),
        	'userName': $("#userName").val(),
        	
        	'yingxiao_begin': $("#yingxiao_begin").val(),
        	'yingxiao_end': $("#yingxiao_end").val(),
        	'touzi_begin': $("#touzi_begin").val(),
        	'touzi_end': $("#touzi_end").val(),
        	'hongbao_begin': $("#hongbao_begin").val(),
        	'hongbao_end': $("#hongbao_end").val(),
//        	'userType': $("#userType").val(),
        	userType : getSelectData("userType"),
        	'userName1': $("#userName1").val(),
//        	'channelName1': $("#channelName1").val(),
        	channelName1 : getSelectData("channelName1"),
        	'userId1': $("#userId1").val(),
        	'userId': $("#userId").val(),
        	
	};
	return params;
}
