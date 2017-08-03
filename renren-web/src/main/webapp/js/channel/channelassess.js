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
    $("#end_time").val(addDate(getCurrDate(), -1));
}
function initTimeCond1(){
    $("#stat_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#stat_time").val(addDate(getCurrDate(), -7));
}


function getDate(datatype){
    var today = new Date(new Date()-24*60*60*1000);
    var halfYearAgo = new Date(new Date()-24*60*60*1000*2);
    var startdate;
    var enddate;
    startdate = (today.getFullYear()) +"-" +
        (today.getMonth() + 1 >9  ? (today.getMonth() + 1 ) : "0"+(today.getMonth() + 1 )) + "-" +
        (today.getDate() > 10 ? today.getDate() : "0" + today.getDate());
    enddate = (halfYearAgo.getFullYear()) +"-" +
        (halfYearAgo.getMonth() + 1 >9  ? (halfYearAgo.getMonth() + 1 ) : "0"+(halfYearAgo.getMonth() + 1 ))+"-01";
    return datatype==1 ? startdate : enddate;
};

//初始化时间
document.getElementById("stat_time").value=getDate(1);
document.getElementById("end_time").value = getYesterday(1);

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		var select = $("#list_select").children('option:selected').val();
			executePost('../channel/assess/exportExcel', {'params' : JSON.stringify(params)});
	});

}  
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '负责人', name: 'CHANNELHEAD', index: '$CHANNELHEAD', width: 70,align:'right',sortable:false },
			{ label: '渠道名称', name: 'CHANNELNAME', index: '$CHANNELNAME', width: 70 ,align:'right',sortable:false }, 			
			{ label: '渠道标签', name: 'CHANNELLABEL', index: '$CHANNELLABEL', width: 70 ,align:'right',sortable:false }, 
			{ label: '注册人数', name: 'REGISTERED', index: '$REGISTERED', width: 70 ,align:'right',sortable:false }, 			
			{ label: '实名人数', name: 'CGNUM', index: '$CGNUM', width: 70 ,align:'right',sortable:false }, 	
			{ label: '充值人数', name: 'CZNUM', index: '$CZNUM', width: 90,align:'right' ,sortable:false },
			{ label: '充值金额(万)', name: 'CZMONEY', index: '$CZMONEY', width: 120 ,align:'right',sortable:false },		
			{ label: '提现金额(万)', name: 'TXMONEY', index: '$TXMONEY', width: 120 ,align:'right',sortable:false },
			{ label: '充提差(万)', name: 'CTMONEY', index: '$CTMONEY', width: 90,align:'right' ,sortable:false },
			{ label: '首投人数', name: 'SHOUTOU', index: '$INVESTNUM', width: 90 ,align:'right',sortable:false },	
			{ label: '投资人数', name: 'INVESTNUM', index: '$INVESTNUM', width: 90 ,align:'right',sortable:false },
			{ label: '投资金额', name: 'INVESTMONEY', index: '$INVESTMONEY', width: 90 ,align:'right',sortable:false },
			{ label: '加权平均期限', name: 'AVGP', index: '$INVESTMONEY', width: 90 ,align:'right',sortable:false },
			{ label: '账户余额(万)', name: 'ZHMONEY', index: '$ZHMONEY', width: 100 ,align:'right',sortable:false },		
			{ label: '待收金额(万)', name: 'DSMONEY', index: '$DSMONEY', width: 100 ,align:'right',sortable:false },
			{ label: '待收流失人数', name: 'DSLSNUM', index: '$DSLSNUM', width: 110 ,align:'right',sortable:false },
			{ label: '投资用户流失率', name: 'INVESTLS', index: '$INVESTLS', width: 120 ,align:'right',sortable:false 
				,formatter: function(value, options, row){
					if(row.负责人 == '汇总'){
						return value;
					}else if(value == null || value == ''){
						return '';
					}else{
						return formatNumber(value*100,2) + '%';
					}
				} 
			},
			{ label: '资产留存率', name: 'ZICHAN', index: '$ZICHAN', width: 110 ,align:'right',sortable:false 
				,formatter: function(value, options, row){
					if(row.负责人 == '汇总'){
						return value;
					}else if(value == null || value == ''){
						return '';
					}else{
						return formatNumber(value*100,2) + '%';
					}
				} 
			},
			{ label: '充值金额留存率', name: 'CHONGZHI', index: '$CHONGZHI', width: 120 ,align:'right',sortable:false 
				,formatter: function(value, options, row){
					if(row.负责人 == '汇总'){
						return value;
					}else if(value == null || value == ''){
						return '';
					}else{
						return formatNumber(value*100,2) + '%';
					}
				} 
			},
			{ label: '最近注册天数', name: 'TIMER', index: '$CHONGZHI', width: 120 ,align:'right',sortable:false }
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
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



var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDdzRemain: {}
	},
	methods: {	
	reload: function (event) {
		loading();
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
