
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
            STAT_PERIOD: document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
            SOURCECASENO :document.getElementById("SOURCECASENO").value,
            CUSTOMERNAME :document.getElementById("CUSTOMERNAME").value,
            ISCOMPLETED :document.getElementById("ISCOMPLETED").value,
            ISSTAMP :document.getElementById("ISSTAMP").value
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
    url: "../dmreportcgreport/list",
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
        '{field:"sourcecaseno",title:"平台项目编号",align:"center",valign:"middle"},'+
        '{field:"department",title:"部门",align:"center",valign:"middle"},'+
        '{field:"projectBelong",title:"项目归属",align:"center",valign:"middle"},'+
        '{field:"projectType",title:"项目类型",align:"center",valign:"middle"},'+
        '{field:"customername",title:"借款人",align:"center",valign:"middle"},'+
        '{field:"payformoney",title:"金额",align:"center",valign:"middle"},'+
        '{field:"loanrate",title:"利率",align:"center",valign:"middle"},'+
        '{field:"loanyearlimit",title:"期限-月",align:"center",valign:"middle"},'+
        '{field:"payforlimittime",title:"期限-日",align:"center",valign:"middle"},'+
        '{field:"giveoutmoneytime",title:"满标放款日",align:"center",valign:"middle"},'+
        '{field:"willgetmoneydate",title:"到期日",align:"center",valign:"middle"},'+
        '{field:"iscompleted",title:"资料签名、盖章是否完全",align:"center",valign:"middle"},'+
        '{field:"sendDeadline",title:"纸质文本最迟寄送日（发标5个工作日内）",align:"center",valign:"middle"},'+
        '{field:"isstamp",title:"纸文本是否盖章",align:"center",valign:"middle"},'+
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
            STAT_PERIOD: document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
            SOURCECASENO :document.getElementById("SOURCECASENO").value,
            CUSTOMERNAME :document.getElementById("CUSTOMERNAME").value,
            ISCOMPLETED :document.getElementById("ISCOMPLETED").value,
            ISSTAMP :document.getElementById("ISSTAMP").value


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
		executePost('../dmreportcgreport/partExport', {'params' : JSON.stringify(params)});
	});

}