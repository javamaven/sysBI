
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
    var halfYearAgo = new Date(new Date()-24*60*60*1000*2);
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
document.getElementById("reg_begindate").value=getDate(2);
document.getElementById("reg_enddate").value=getDate(1);


// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
        reg_begindate  : document.getElementById("reg_begindate").value.replace(/-/g,""),
        reg_enddate : document.getElementById("reg_enddate").value.replace(/-/g,"")
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
        	'reg_begindate': $("#reg_begindate").val(),
        	'reg_enddate': $("#reg_enddate").val(),

	};
	return params;
}


function loadTableAjax(){
 $.ajax({
  //请求方式
    type: "POST",
    //发送请求的地址
    url: "../channel/daily/black",
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
        //alert(a);

        var b = '['+
        '{field:"statPeriod",title:"日期",align:"center",valign:"middle",class:""},'+
        '{field:"indicatorsName",title:"指标名字",align:"left",valign:"middle"},'+
        '{field:"indicatorsValue",title:"指标值",align:"center",valign:"middle"},'+
        '{field:"sequential",title:"环比",align:"center",valign:"middle"},'+
        '{field:"compared",title:"同比",align:"center",valign:"middle"},'+
        '{field:"monthMeanValue",title:"30天均值",align:"center",valign:"middle"},'+
        '{field:"monthMeanValueThan",title:"30天均值比",align:"center",valign:"middle"},'+
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
            reg_begindate: document.getElementById("reg_begindate").value.replace(/-/g,""),
            reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
        };
    //加载数据

    loadTableAjax();
});

$(function(){

//    loadChannel();
    loadTableAjax();
    initExportFunction();


});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../channel/daily/partExport', {'params' : JSON.stringify(params)});
	});

}