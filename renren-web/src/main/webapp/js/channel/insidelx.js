
//// 获取渠道信息（查找）
//function getChannelName(){
//    var arrStr = new Array();
//    $(".select2-selection__choice").each(function(){
//        arrStr.push($(this).attr("title"))
//        });
//    return  arrStr;
//}
//默认时间
function getDate(datatype){
    var today = new Date(new Date()-24*60*60*1000*1);
    var halfYearAgo = new Date(new Date()-24*60*60*1000*182);
    var startdate;
    var enddate;
    startdate = (today.getFullYear()) +"-" +
        (today.getMonth() + 1 >9  ? (today.getMonth() + 1 ) : "0"+(today.getMonth() + 1 )) + "-" +
        (today.getDate() > 9 ? today.getDate() : "0" + today.getDate());
    enddate = (halfYearAgo.getFullYear()) +"-" +
        (halfYearAgo.getMonth() + 1 >9  ? (halfYearAgo.getMonth() + 1 ) : "0"+(halfYearAgo.getMonth() + 1 ))+"-01";
    return datatype==1 ? startdate : enddate;



};
//时间格式
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
//document.getElementById("STAT_PERIOD").value=getDate(2);
document.getElementById("STAT_PERIOD").value=getDate(1);


// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
//        STAT_PERIOD  : document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
        STAT_PERIOD : document.getElementById("STAT_PERIOD").value.replace(/-/g,"")
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
//function loadChannel(){
//    var str = '';
//    var i = 0;
//    $.ajax({
//      //请求方式
//        type: "POST",
//          //发送请求的地址
//        url: "../channel/channelAll/getChannel",
//        //stringifya
//        data: JSON.stringify(),
//        contentType: "application/json;charset=utf-8",
//        success : function(msg) {
//            console.log(msg);
//            for(var list in msg.Channel){
//                for(var key in msg.Channel[list]){
//                    if(key == "channelName")
//                        str += '<option value="'+(i++)+'">' + msg.Channel[list][key] + "</option>";
//                }
//            }
//
//            $("#id_select").select2({
//                maximumSelectionLength: 1
//            });
//            $("#id_select").append(str);
//        }
//     });
//}

function getParams(){
	var params = {
        	'statPeriod': $("#STAT_PERIOD").val(),

	};
	return params;
}


function loadTableAjax(){
 $.ajax({
  //请求方式
    type: "POST",
    //发送请求的地址
    url: "../dmreportinsidelx/list",
    data: JSON.stringify(pageInfo),
    contentType: "application/json;charset=utf-8",
    success : function(msg) {
        console.log(msg);
        var a = '';
        for(var list in msg.page){
            var d = '{'
            for(var key in msg.page[list]){
                d += '"' + key + '":"' + msg.page[list][key] + '",';
            }
            d = d.substring(0,d.length-1) + '},';
            a += d;
        };
        a = '['+a.substring(0,a.length-1)+']';
//        alert(a);
        var b = '['+
        '{field:"statPeriod",title:"统计日期",align:"center",valign:"middle" },'+
        '{field:"lxName",title:"员工姓名",align:"center",valign:"middle"},'+
        '{field:"laxDep",title:"　　　部门　　　",align:"center",valign:"middle"},'+
        '{field:"lxUserCou",title:"当季度有效拉新人数",align:"center",valign:"middle"},'+
        '{field:"lxUserTg",title:"当季度拉新目标人数",align:"center",valign:"middle"},'+
        '{field:"reach",title:"当季度有效拉新人数指标达成率",align:"center",valign:"middle"},'+
        '{field:"lxDs",title:"满足日均待收金额的人数",align:"center",valign:"middle"},'+
        '{field:"lxUserPw",title:"当季度累计有效拉新人数排名",align:"center",valign:"middle"},'+
        '{field:"jf",title:"季度度新增积分值",align:"center",valign:"middle"},'+
        '{field:"jfpw",title:"季度度新增积分值排名",align:"center",valign:"middle"},'+
        ']';

        //加载数据
        loadTable(b,a);

        $(window).resize(function () {
            $('#reportTable').bootstrapTable('resetView');
        });


        }
    });
}

$("#searchButton").click(function(){
    // 显示之前，先把当前表格销毁
      $('#reportTable').bootstrapTable('destroy');
        //添加样式
        $(".spinners li").addClass("active");
    // 查询条件

     pageInfo = {
            page  : 1,
            limit : 10,
//            channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
//             indicatorsName :document.getElementById("indicatorsName").value,
            STAT_PERIOD: document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
//            reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
        };
    //加载数据
    loadTableAjax();
});

$(function(){

//    loadChannel();
    loadTableAjax();
    initExportFunction();
//$('#btn_exports').click(function(){
////    executePost('../channel/daily/partExport', {'params' : JSON.stringify(params)});
//    window.open("../channel/daily/partExport","_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
//});

});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportinsidelx/partExport', {'params' : JSON.stringify(params)});
	});

}