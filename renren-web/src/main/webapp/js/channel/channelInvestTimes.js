$(function(){
    loadChannel();
    initExportFunction();
    initEvent();
});


var currDataList;

function initExportFunction(){
	$('#btn_exports').click(function(){
		if(currDataList){
			executePost('../channel/invest/exportExcel', {'list': currDataList } );  
		}else {
			alert('请先查询数据!');
		}
//	    window.open("../channel/stft/exportExcel?list=" + JSON.stringify(currDataList),"_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
	});

}

function loadTableAjax(){
 $.ajax({
    type: "POST",
    url: "../channel/invest/queryChannelInvestTimesList",
    data: JSON.stringify(pageInfo),
    contentType: "application/json;charset=utf-8",
    success : function(msg) {
         var a = '';
        for(var list in msg.page.list){
            var d = '{'
            for(var key in msg.page.list[list]){
                d += '"' + key + '":"' + msg.page.list[list][key] + '",';
            }
            d = d.substring(0,d.length-1) + '},';
            a += d;
        };
        a = '['+a.substring(0,a.length-1)+']';
        currDataList = a;
        var b = '['+
        '{field:"channelName",title:"渠道名称",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
        '{field:"channelLabel",title:"渠道标签",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"registerUserNum",title:"注册人数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstInvestUserNum",title:"首投人数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstInvestAmount",title:"首投金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"investTimes",title:"投资次数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"investUserNum",title:"投资人数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"investAmount",title:"累计投资金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"investYearAmount",title:"累计投资年华金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstInvestRedMoney",title:"首投使用红包金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"perFirstInvestRedMoney",title:"人均首投使用红包金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"allRedMoney",title:"累计使用红包金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"ddzInvestDays",title:"点点赚投资天数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"ddzPerInvestAmount",title:"点点赚平均投资金额",align:"center",valign:"middle",sortable:"true"}'+
        ']';

        //加载数据
        loadTable(b,a);

        $(window).resize(function () {
            $('#reportTable').bootstrapTable('resetView');
        });

        }
    });


}

// 获取渠道信息
function getChannelName(){
    var arrStr = new Array();
    $(".select2-selection__choice").each(function(){
        arrStr.push($(this).attr("title"))
        });
    return  arrStr;
};

function getDate(datatype){
    var today = new Date(new Date()-24*60*60*1000);
    var halfYearAgo = new Date(new Date()-24*60*60*1000*182);
    var startdate;
    var enddate;
    startdate = (today.getFullYear()) +"-" +
        (today.getMonth() + 1 >9  ? (today.getMonth() + 1 ) : "0"+(today.getMonth() + 1 )) + "-" +
        (today.getDate() > 10 ? today.getDate() : "0" + today.getDate());
    enddate = (halfYearAgo.getFullYear()) +"-" +
        (halfYearAgo.getMonth() + 1 >9  ? (halfYearAgo.getMonth() + 1 ) : "0"+(halfYearAgo.getMonth() + 1 ))+"-01";
    return datatype==1 ? startdate : enddate;
};

$(".form_datetime_2").
    datetimepicker({
    format: 'yyyy-mm-dd',
    minView:'month',
    language: 'zh-CN',
    autoclose:true,
    todayBtn : true,
    setStartDate:new Date()
});
// 初始化时间
document.getElementById("reg_begindate").value = addDate(getCurrDate(), -90);
document.getElementById("reg_enddate").value = getYesterday();


// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
        regBeginDate  : document.getElementById("reg_begindate").value.replace(/-/g,""),
        regEndDate : document.getElementById("reg_enddate").value.replace(/-/g,"")
    };

// 表格加载
function loadTable(columnsData,tableData){
    $('#reportTable').bootstrapTable({
        method: 'get',
        cache: false,
        height: tableHeight(),
        pagination: true,
        pageSize: 20,
        pageNumber:1,
        pageList: [10, 20, 50, 100, 200, 500],
        // search: true,
        // showColumns: true,
        // showExport: true,
        clickToSelect: true,
        columns: eval("("+columnsData+")"),
        data :eval("("+tableData+")") ,
        formatNoMatches: function(){
            return '无符合条件的记录';
        }
    });
        // 移除loading样式
        $(".spinners li").removeClass("active");
}
//加载渠道数据
function loadChannel(){
//    var str = '';
//    var i = 0;
//    $.ajax({
//        type: "POST",
//        url: "../channel/channelAll/queryChannelList",
//        data: JSON.stringify(),
//        contentType: "application/json;charset=utf-8",
//        success : function(msg) {
//        	var index = 0;
//        	for (var i = 0; i < msg.data.length; i++) {
//				var channelNameBack = msg.data[i];
//				 str += '<option value="'+(index++)+'">' + channelNameBack + "</option>";
//			}
//            $("#id_select").select2({
//                maximumSelectionLength: 3,
//                width:'100%'
//            });
//            $("#id_select").append(str);
//        }
//     });
	   var str = '';
	    var i = 0;
	    $.ajax({
	        type: "POST",
	        url: "../channel/channelAll/getChannel",
	        data: JSON.stringify(),
	        contentType: "application/json;charset=utf-8",
	        success : function(msg) {
	            console.log(msg);
	            for(var list in msg.Channel){
	                for(var key in msg.Channel[list]){
	                    if(key == "channelName")
	                        str += '<option value="'+(i++)+'">' + msg.Channel[list][key] + "</option>";
	                }
	            }

	            $("#id_select").select2({
	                maximumSelectionLength: 3,
	                width:'100%'
	            });
	            $("#id_select").append(str);
	        }
	     });

};

$("#searchButton").click(function(){
    // 显示之前，先把当前表格销毁
    $('#reportTable').bootstrapTable('destroy');
    //添加样式
    $(".spinners li").addClass("active");
    // 查询条件
    pageInfo = getParams();
    //加载数据
    loadTableAjax();
});

function getParams(){
	var params =  {
            page  : 1,
            limit : 10000,
            channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
            invBeginDate: document.getElementById("reg_begindate").value.replace(/-/g,""),
            invEndDate: document.getElementById("invest_enddate").value.replace(/-/g,""),
            regBeginDate: document.getElementById("reg_begindate").value.replace(/-/g,""),
            regEndDate:document.getElementById("reg_enddate").value.replace(/-/g,"")
        };
	return params;
}
