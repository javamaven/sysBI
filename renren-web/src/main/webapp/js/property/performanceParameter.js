
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
            DEPARTMENT: document.getElementById("DEPARTMENT").value
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
    url: "../dmreportperformanceledger/list",
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
        '{field:"statPeriod",title:"业务日期",align:"center",valign:"middle" },'+
        '{field:"developmanagername",title:"总监",align:"center",valign:"middle"},'+
        '{field:"department",title:"部门",align:"center",valign:"middle"},'+
        '{field:"payformoneyout",title:"放款金额",align:"center",valign:"middle"},'+
        '{field:"grossProfit",title:"毛利",align:"center",valign:"middle"},'+
        '{field:"salaryCost",title:"工资成本",align:"center",valign:"middle"},'+
        '{field:"reimbursement",title:"报销开支",align:"center",valign:"middle"},'+
        '{field:"rentShare",title:"租金分摊",align:"center",valign:"middle"},'+
        '{field:"netMargin",title:"净利",align:"center",valign:"middle"},'+
        '{field:"commissionRatio",title:"提成系数",align:"center",valign:"middle"},'+
        '{field:"availablePerformance",title:"可发绩效",align:"center",valign:"middle"},'+
        '{field:"riskReserve",title:"风险准备金",align:"center",valign:"middle"},'+
        '{field:"settledAmount",title:"结清金额",align:"center",valign:"middle"},'+
        '{field:"settledAmtRate",title:"结清金额占比",align:"center",valign:"middle"},'+
        '{field:"expectedPerformance",title:"本月应发绩效",align:"center",valign:"middle"},'+
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
             DEPARTMENT: document.getElementById("DEPARTMENT").value

        };
    //加载数据
    loadTableAjax();
});

$(function(){

//    loadChannel();
//    loadTableAjax();
 $(".spinners li").removeClass("active");
    initExportFunction();

});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportperformanceledger/partExport', {'params' : JSON.stringify(params)});
	});

}