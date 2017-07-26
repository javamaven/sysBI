$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	loadChannel();
	initEvent();
	initSelectEvent();
	initTimeCond1();
	loadChannel2();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
		}else if(select == 'vip_count'){
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
}
function initTimeCond1(){
    $("#stat_time").datetimepicker({
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
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../yunying/yxp2p/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/p2p/exportExcel2', {'params' : JSON.stringify(params)});
		}
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '负责人', name: 'CHANNELHEAD', index: '$CHANNELHEAD', width: 90,align:'right' },
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$CHANNELNAME', width: 90 ,align:'right'}, 			
			{ label: '渠道标签', name: 'CHANNELLABEL', index: '$CHANNELLABEL', width: 90 ,align:'right'}, 
			{ label: '注册人数', name: 'REGISTERED', index: '$REGISTERED', width: 90 ,align:'right'}, 			
			{ label: '存管实名人数', name: 'CGNUM', index: '$CGNUM', width: 90 ,align:'right'}, 	
			{ label: '充值人数', name: 'CZNUM', index: '$CZNUM', width: 90,align:'right' },
			{ label: '充值金额万', name: 'CZMONEY', index: '$CZMONEY', width: 90 ,align:'right'},		
			{ label: '提现金额万', name: 'TXMONEY', index: '$TXMONEY', width: 90 ,align:'right'},
			{ label: '充提差万', name: 'CTMONEY', index: '$CTMONEY', width: 90,align:'right' },
			{ label: '投资人数', name: 'INVESTNUM', index: '$INVESTNUM', width: 90 ,align:'right'},		
			{ label: '投资金额', name: 'INVESTMONEY', index: '$INVESTMONEY', width: 90 ,align:'right'},
			{ label: '平台注册人数', name: 'PTZNUM', index: '$PTZNUM', width: 90 ,align:'right'},
			{ label: '平台投资人数', name: 'PTINVESTNUM', index: '$PTINVESTNUM', width: 90 ,align:'right'},
			{ label: '平台投资金额', name: 'PTINVESTMONEY', index: '$PTINVESTMONEY', width: 90,align:'right' },
			{ label: '账户余额万', name: 'ZHMONEY', index: '$ZHMONEY', width: 90 ,align:'right'},		
			{ label: '待收金额万', name: 'DSMONEY', index: '$DSMONEY', width: 90 ,align:'right'},
			{ label: '待收流失人数', name: 'DSLSNUM', index: '$DSLSNUM', width: 90 ,align:'right'},
			{ label: '投资用户流失率', name: 'INVESTLS', index: '$INVESTLS', width: 90 ,align:'right'},		
			{ label: '资产留存率', name: 'ZICHAN', index: '$ZICHAN', width: 90 ,align:'right'},
			{ label: '充值金额留存率', name: 'CHONGZHI', index: '$CHONGZHI', width: 90 ,align:'right'}
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 500,
        rownumbers: true, 
        autowidth:true,
//        shrinkToFit: false,
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
        }
    });
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
		vm.showList = true;
		$("#jqGrid").jqGrid("clearGridData");
		$("#jqGrid").jqGrid('setGridParam',{ 
			datatype:'json', 
			url: '../channel/assess/list',
            postData: getParams()
        }).trigger("reloadGrid");
	}
	}
});

//加载渠道数据
function loadChannel(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
        url: "../channel/channelAll/getChannel",
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
//function getChannelName(select_id){
//    var reslist=$("#"+select_id).select2("data"); //多选
//    var arrStr = new Array();
//    for(i=0;i<reslist.length;i++){
//
//        arrStr.push(reslist[i].id)
//    }
//    return  arrStr;
//};




//加载负责人数据
function loadChannel2(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
        url: "../channel/channelAll/getChannelHead",
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.ChannelHead){
                for(var key in msg.ChannelHead[list]){
                    if(key == "channelHead")
                        str += '<option value="'+(i++)+'">' + msg.ChannelHead[list][key] + "</option>";
                }
            };
            $("#channelHead").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#channelHead").append(str);
        }
     });
};
/**
 * 获取选中的值
 * @param id 
 * @returns
 */
function getSelectData(id){
    var channelHead = '';
    var selectData = $("#" + id).select2("data");
    for (var i = 0; i < selectData.length; i++) {
		var select = selectData[i];
		channelHead += "'" + select.text + "',";
	}
    if(channelHead.indexOf(",") >= 0){
    	channelHead = channelHead.substring(0, channelHead.length-1);
    }
    return  channelHead;
};


function getParams(){
	var params = {
		  'end_time': $("#end_time").val(),
          'stat_time': $("#stat_time").val(),
          channelHead : getSelectData("channelHead"),
          channelName : getSelectData("id_select")
		
			
        	
	};
	return params;
}
