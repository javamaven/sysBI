
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
            STAT_PERIOD: document.getElementById("STAT_PERIOD").value,
            SOURCECASENO :document.getElementById("SOURCECASENO").value,
            CUSTOMERNAME :document.getElementById("CUSTOMERNAME").value,
            PLANREPAYDATE :document.getElementById("PLANREPAYDATE").value,
            REALREDATE :document.getElementById("REALREDATE").value
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
    url: "../dmreportfinrepaymentdetail/list",
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
        '{field:"sourcecaseno",title:"项目合同编号",align:"center",valign:"middle"},'+
        '{field:"customername",title:"借款人",align:"center",valign:"middle"},'+
        '{field:"payformoney",title:"借款金额",align:"center",valign:"middle"},'+
        '{field:"payformoneyout",title:"放款金额",align:"center",valign:"middle"},'+
        '{field:"totpmts",title:"总期数",align:"center",valign:"middle"},'+
        '{field:"reindex",title:"还款期数",align:"center",valign:"middle"},'+
        '{field:"planrepaydate",title:"计划还款日",align:"center",valign:"middle"},'+
        '{field:"realredate",title:"实际还款日期",align:"center",valign:"middle"},'+
        '{field:"remain",title:"应还本金",align:"center",valign:"middle"},'+
        '{field:"reinterest",title:"应还利息",align:"center",valign:"middle"},'+
        '{field:"rebackmain",title:"已还本金",align:"center",valign:"middle"},'+
        '{field:"rebackinterest",title:"已还利息",align:"center",valign:"middle"},'+
        '{field:"reamercedmoney",title:"已还罚息",align:"center",valign:"middle"},'+
        '{field:"reamercedmoney3",title:"已还违约金",align:"center",valign:"middle"},'+
        '{field:"realgetmoneydate",title:"项目结清日",align:"center",valign:"middle"},'+
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
            STAT_PERIOD: document.getElementById("STAT_PERIOD").value,
            SOURCECASENO :document.getElementById("SOURCECASENO").value,
            CUSTOMERNAME :document.getElementById("CUSTOMERNAME").value,
            PLANREPAYDATE :document.getElementById("PLANREPAYDATE").value,
            REALREDATE :document.getElementById("REALREDATE").value
//            reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
        };
    //加载数据
    loadTableAjax();
});

$(function(){

//    loadChannel();
//    loadTableAjax();
 $(".spinners li").removeClass("active");
    initExportFunction();
//$('#btn_exports').click(function(){
////    executePost('../channel/daily/partExport', {'params' : JSON.stringify(params)});
//    window.open("../channel/daily/partExport","_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
//});

});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportfinrepaymentdetail/partExport', {'params' : JSON.stringify(params)});
	});

}