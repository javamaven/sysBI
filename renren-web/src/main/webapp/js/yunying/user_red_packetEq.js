$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initCountTableGrid();
	hongbaoTableGrid();
	loadChannel();
	loadhuodong();
	loadhongBaoName();
	loadUserType();
	loadRate();
	loadType();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#query_cond_div1").show();
			$("#query_cond_div2").hide();
			$("#query_cond_div3").hide();
			$("#vip_count_div").hide();
			$("#vip_detail_div").show();
			$("#hongbao_div").hide();
		}else if(select == 'vip_count'){
			$("#query_cond_div1").hide();
			$("#query_cond_div2").show();
			$("#query_cond_div3").hide();
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			$("#hongbao_div").hide();
		}else if(select == 'hongbao'){
			$("#query_cond_div1").hide();
			$("#query_cond_div2").hide();
			$("#query_cond_div3").show();
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#hongbao_div").show();
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
    
    
    $("#begin_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#begin_time").val(addDate(getCurrDate(), -16));
    
    
    $("#touzi_begin").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_begin").val(addDate(getCurrDate(), -16));
    
    $("#touzi_end").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_end").val(addDate(getCurrDate(), -1));
    
  
    
    $("#end_time2").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#end_time2").val(addDate(getCurrDate(), -1));
    
    
    $("#begin_time2").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#begin_time2").val(addDate(getCurrDate(), -16));
    
    
    $("#touzi_begin2").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_begin2").val(addDate(getCurrDate(), -16));
    
    $("#touzi_end2").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_end2").val(addDate(getCurrDate(), -1));
    
    $("#end_time3").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#end_time3").val(addDate(getCurrDate(), -1));
    
    
    $("#begin_time3").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#begin_time3").val(addDate(getCurrDate(), -16));
    
    
    $("#touzi_begin3").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_begin3").val(addDate(getCurrDate(), -16));
    
    $("#touzi_end3").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#touzi_end3").val(addDate(getCurrDate(), -1));
    
  
    
}




function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		console.info('++++btn_exports+++')
		executePost('../yunying/userredeq/exportExcel', {'params' : JSON.stringify(params)});
	});
	$('#btn_exports2').click(function(){
		var params = getParams();
		console.info('++++btn_exports2+++')
		executePost('../yunying/userredeq/exportExcel2', {'params' : JSON.stringify(params)});
	});
	$('#btn_exports3').click(function(){
		var params = getParams();
		console.info('++++btn_exports3+++')
		executePost('../yunying/userredeq/exportExcel3', {'params' : JSON.stringify(params)});
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '查询日期', name: 'TIME', index: '$TYPE', width: 90,align:'right',sortable:false},
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$NUM', width: 90 ,align:'right',sortable:false}, 			
			{ label: '渠道标记', name: 'BIAOJI', index: '$SUM', width: 90 ,align:'right',sortable:false}, 
			{ label: '获得红包用户数', name: 'USERNUM', index: '$BORROW_USER', width: 90 ,align:'right',sortable:false}, 			
			{ label: '获取红包个数', name: 'HONGBAO', index: '$BORROW_CAPITAL', width: 90 ,align:'right',sortable:false}, 	
			{ label: '使用红包的个数', name: 'SHIYONG', index: '$NUMM', width: 130,align:'right' ,sortable:false},
			{ label: '获得红包总金额', name: 'HDMONEY', index: '$SUMM', width: 90 ,align:'right',sortable:false},		
			{ label: '使用红包总金额', name: 'SYMONEY', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '剩余可使用红包金额', name: 'SYMONEYZ', index: '$NUMS', width: 120,align:'right' ,sortable:false},
			{ label: '使用红包金额占比', name: 'HBZHANBI', index: '$AVGS', width: 120 ,align:'right',sortable:false},		
			{ label: '使用红包个数占比', name: 'HONGBAOGS', index: '$AVGLI', width: 120 ,align:'right',sortable:false},
			
			{ label: '投资次数中红包使用次数占比', name: 'VOUCHETIME', index: '$TYPE', width: 120,align:'right' ,sortable:false},
			{ label: '使用红包投资的总金额', name: 'CAPITAL', index: '$NUM', width: 120 ,align:'right',sortable:false}, 			
			{ label: '使用红包投资的年化投资金额', name: 'TENDER', index: '$SUM', width: 120 ,align:'right',sortable:false}, 
			{ label: '使用红包的ROI', name: 'ROI', index: '$BORROW_USER', width: 120 ,align:'right',sortable:false}, 			
			{ label: '使用红包的年化ROI', name: 'NROI', index: '$BORROW_CAPITAL', width: 170 ,align:'right',sortable:false}	
		
					
        ],
		viewrecords: true,  //页面的 页码和页数和总数的显示
        height: $(window).height()-330,
        rowNum: 500,
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
			{ label: '系统日期', name: 'TIME', index: '$TYPE', width: 90,align:'right' ,sortable:false},
			{ label: '活动时间', name: 'TITIME', index: '$NUM', width: 90 ,align:'right',sortable:false}, 			
			{ label: '活动目的', name: 'PURPOSE', index: '$SUM', width: 90 ,align:'right',sortable:false}, 
			{ label: '红包名称', name: 'NAME', index: '$BORROW_USER', width: 90 ,align:'right',sortable:false}, 			
			{ label: '红包模板ID', name: 'PTID', index: '$BORROW_CAPITAL', width: 90 ,align:'right',sortable:false}, 	
			{ label: '杠杆', name: 'RATE', index: '$NUMM', width: 130,align:'right' ,sortable:false},
			{ label: '红包类型', name: 'AN', index: '$SUMM', width: 150 ,align:'right',sortable:false},		
			{ label: '红包金额', name: 'MONEY', index: '$SUMM', width: 150 ,align:'right',sortable:false},	
			{ label: '获得红包用户数', name: 'USERNUM', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '获取红包个数', name: 'HBCOUNT', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '使用红包的个数', name: 'COUNTHB', index: '$NUMS', width: 120,align:'right',sortable:false },
			{ label: '获得红包总金额', name: 'HBMONEY', index: '$AVGS', width: 120 ,align:'right',sortable:false},		
			{ label: '使用红包总金额', name: 'USEMONEY', index: '$AVGLI', width: 120 ,align:'right',sortable:false},
			
			{ label: '剩余可使用红包金额', name: 'SYMONEY', index: '$TYPE', width: 120,align:'right' ,sortable:false},
			{ label: '使用红包金额占比', name: 'SYMONEYZ', index: '$TYPE', width: 120,align:'right',sortable:false },
			{ label: '使用红包个数占比', name: 'SYGESHU', index: '$NUM', width: 140 ,align:'right',sortable:false}, 			
			{ label: '投资次数中红包使用次数占比', name: 'TZZHANBI', index: '$SUM', width: 120 ,align:'right',sortable:false}, 
			{ label: '使用红包投资的总金额', name: 'CAPITAL', index: '$BORROW_USER', width: 140 ,align:'right',sortable:false}, 			
			{ label: '使用红包投资的年化投资金额', name: 'SUMTENDER', index: '$BORROW_CAPITAL', width: 120 ,align:'right',sortable:false}, 	
			{ label: '使用红包的ROI', name: 'ROI', index: '$NUMM', width: 150,align:'right' ,sortable:false},
			{ label: '使用红包的年化ROI', name: 'NROI', index: '$SUMM', width: 100 ,align:'right',sortable:false}	
			
        ],
		viewrecords: true,
        height: $(window).height()-330,
        rowNum: 500,
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

function hongbaoTableGrid(){
    $("#jqGrid_hongbao").jqGrid({
        datatype: "json",
        colModel: [
			{ label: '系统日期', name: 'TIME', index: '$TYPE', width: 90,align:'right' ,sortable:false},
			{ label: '获得红包的金额', name: 'MONEY', index: '$NUM', width: 90 ,align:'right',sortable:false}, 			
			{ label: '杠杆', name: 'RATE', index: '$SUM', width: 90 ,align:'right',sortable:false}, 
			{ label: '红包类型', name: 'AN', index: '$BORROW_USER', width: 90 ,align:'right',sortable:false}, 			
			{ label: '获得红包用户数', name: 'HDHONGBAO', index: '$BORROW_CAPITAL', width: 90 ,align:'right',sortable:false}, 	
			{ label: '获取红包个数', name: 'HBGESHU', index: '$NUMM', width: 130,align:'right' ,sortable:false},
			{ label: '使用红包的个数', name: 'SYHONGBAO', index: '$SUMM', width: 150 ,align:'right',sortable:false},		
			{ label: '获得红包总金额', name: 'HDMONEY', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '使用红包总金额', name: 'SYMONEY', index: '$AVGG', width: 90 ,align:'right',sortable:false},
			{ label: '剩余可使用红包金额', name: 'SYYMONEY', index: '$NUMS', width: 120,align:'right',sortable:false },
			{ label: '使用红包金额占比', name: 'USEMONEY', index: '$AVGS', width: 120 ,align:'right',sortable:false},		
			{ label: '使用红包个数占比', name: 'SYZHANBI', index: '$AVGLI', width: 120 ,align:'right',sortable:false},
			
			{ label: '投资次数中红包使用次数占比', name: 'TIMEI', index: '$TYPE', width: 120,align:'right' ,sortable:false},
			{ label: '使用红包投资的总金额', name: 'CAPITAL', index: '$TYPE', width: 120,align:'right',sortable:false },
			{ label: '使用红包投资的年化投资金额', name: 'NSUM', index: '$NUM', width: 140 ,align:'right',sortable:false}, 			
			{ label: '使用红包的ROI', name: 'ROI', index: '$SUM', width: 120 ,align:'right',sortable:false}, 
			{ label: '使用红包的年化ROI', name: 'NROI', index: '$BORROW_USER', width: 140 ,align:'right',sortable:false}
        ],
		viewrecords: true,
        height: $(window).height()-330,
        rowNum: 500,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
        pager: "#jqGridPager_hongbao",
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
    $("#hongbao_div").hide();
}


function reload() {
	loading();
	var select = $("#list_select").children('option:selected').val();
	if(select == 'vip_detail'){
		$("#jqGrid").jqGrid("clearGridData");
		$("#jqGrid").jqGrid('setGridParam',{ 
			datatype:'json', 		
			url: '../yunying/userredeq/list',
            postData: getParams()
        }).trigger("reloadGrid");
	}else if(select == 'vip_count'){
		$("#jqGrid_count").jqGrid("clearGridData");
		$("#jqGrid_count").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/userredeq/ddylist',
            postData: getParams()
        }).trigger("reloadGrid");
	}else if(select == 'hongbao'){
		$("#jqGrid_hongbao").jqGrid("clearGridData");
		$("#jqGrid_hongbao").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../yunying/userredeq/ddylist2',
            postData: getParams()
        }).trigger("reloadGrid");
	}
}





//加载活动名称
function loadhuodong(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/hongbaohuodong',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
//            console.log(msg);
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
            
            $("#huodong_name2").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#huodong_name2").append(str);
            
            $("#huodong_name3").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#huodong_name3").append(str);
            
        }
     });
};
//加载红包名称
function loadhongBaoName(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/hongbaoname',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
//            console.log(msg);
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
            
            $("#hongbao_name2").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#hongbao_name2").append(str);
            
            $("#hongbao_name3").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#hongbao_name3").append(str);
            
        }
     });
};

//加载用户分类标签
function loadUserType(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/userbiaoqian',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
//            console.log(msg);
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
            
            $("#user_type2").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#user_type2").append(str);
        }
     });
};

//加载红包杠杆
function loadRate(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/rate',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
//            console.log(msg);
            for(var list in msg.rate){
                for(var key in msg.rate[list]){
                    if(key == "RATE")
                        str += '<option value="'+(i++)+'">' + msg.rate[list][key] + "</option>";
                }
            };
            $("#rate").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#rate").append(str);
            
       
        }
     });
};

//加载红包类型
function loadType(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: '../yunying/userredeq/type',
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.type){
                for(var key in msg.type[list]){
                    if(key == "TYPE")
                        str += '<option value="'+(i++)+'">' + msg.type[list][key] + "</option>";
                }
            };
            $("#hongbaoType").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#hongbaoType").append(str);
            
       
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
            
            $("#id_select2").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#id_select2").append(str);
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
    var rate='';
    var selectData = $("#" + id).select2("data");
    for (var i = 0; i < selectData.length; i++) {
		var select = selectData[i];
		hongbao += "'" + select.text + "',";
		hongbaoname += "'" + select.text + "',";
		userbiaoqian += "'" + select.text + "',";
		rate += " " + select.text + ",";
	}
    if(hongbao.indexOf(",") >= 0){
    	hongbao = hongbao.substring(0, hongbao.length-1);
    	hongbaoname = hongbaoname.substring(0, hongbaoname.length-1);
    	userbiaoqian = userbiaoqian.substring(0, userbiaoqian.length-1);
    }
    return  hongbao;
};


function getParams(){
	var params = {
        	'end_time': $("#end_time").val(),
        	'begin_time': $("#begin_time").val(),
        	'touzi_begin': $("#touzi_begin").val(),
        	'touzi_end': $("#touzi_end").val(),
        	huodong_name : getSelectData("huodong_name"),
        	'hongbao_id': $("#hongbao_id").val(),
        	user_type : getSelectData("user_type"),
        	'userId': $("#userId").val(),
        	channelName : getSelectData("id_select"),
        	hongbao_name : getSelectData("hongbao_name"),
        	
        	
        	'touzi_begin2': $("#touzi_begin2").val(),
        	'touzi_end2': $("#touzi_end2").val(),
           	'end_time2': $("#end_time2").val(),
           	'userId2': $("#userId2").val(),
        	'begin_time2': $("#begin_time2").val(),
        	'hongbao_id2': $("#hongbao_id2").val(),
        	user_type2 : getSelectData("user_type2"),
        	channelName2 : getSelectData("id_select2"),
        	hongbao_name2 : getSelectData("hongbao_name2"),
        	huodong_name2 : getSelectData("huodong_name2"),
        	
        	
        	'touzi_begin3': $("#touzi_begin3").val(),
        	'touzi_end3': $("#touzi_end3").val(),
           	'end_time3': $("#end_time3").val(),
           	'begin_time3': $("#begin_time3").val(),   	
           	hongbao_name3 : getSelectData("hongbao_name3"),
        	huodong_name3 : getSelectData("huodong_name3"),
        	 rate : getSelectData("rate"),
        	 hongbaoType : getSelectData("hongbaoType"),
        	'hongbao_id3': $("#hongbao_id3").val(),
        	'money': $("#money").val()
        	
        	
        	
        	
	};
	return params;
}


