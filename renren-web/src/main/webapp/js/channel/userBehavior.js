$(function(){
	$(".spinners li").removeClass("active");
	//    loadTableAjax();
	initExportFunction();

});

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
document.getElementById("endStatDate").value=getDate(1);
document.getElementById("beginStatDate").value=getDate(1);

// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
            beginStatDate: document.getElementById("beginStatDate").value,
            endStatDate: document.getElementById("endStatDate").value,
            userName: document.getElementById("userName").value
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

function loadTableAjax(){
	var userName =  document.getElementById("userName").value;
	if(!userName){
	    userName = null;
	}
	var notUserName =  document.getElementById("notUserName").value;
	if(!notUserName){
		notUserName = null;
	}
	var beginStatDate =  document.getElementById("beginStatDate").value;
	if(!beginStatDate){
	    beginStatDate = null;
	}
	var endStatDate =  document.getElementById("endStatDate").value;
	if(!endStatDate){
	    endStatDate = null;
	}
	pageInfo = {
	        page  : 1,
	        limit : 10,
	        beginStatDate:beginStatDate,
	        endStatDate:endStatDate,
	            userName: userName,
	            notUserName: notUserName
	    };
	 $.ajax({
  //请求方式
    type: "POST",
    //发送请求的地址
    url: "../black/userBehavior/first",
    data: JSON.stringify(pageInfo),
    contentType: "application/json;charset=utf-8",
    success : function(msg) {
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
             '{field:"userName",title:"用户名",align:"center",valign:"middle"},'+ //class:"active" (控制底色）
             '{field:"statDate",title:"访问时间",align:"center",valign:"middle"},'+
             '{field:"reportType",title:"访问报表",align:"center",valign:"middle"},'+
             '{field:"seeTimes",title:"查看次数",align:"center",valign:"middle"},'+
             '{field:"exportTimes",title:"导出次数",align:"center",valign:"middle"},'+
             '{field:"editTimes",title:"修改次数",align:"center",valign:"middle"},'+
             '{field:"deleteTimes",title:"删除次数",align:"center",valign:"middle"},'+
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
      var pageInfo = {
        page  : 1,
        limit : 10,
            beginStatDate: document.getElementById("beginStatDate").value,
             endStatDate: document.getElementById("endStatDate").value,
            userName: document.getElementById("userName").value,
            notUserName: document.getElementById("notUserName").value
    };


    //加载数据
    loadTableAjax();
});




function getParams(){
	var params = {
          'beginStatDate': $("#beginStatDate").val(),
          'userName': $("#userName").val()
    };
	return params;
}


 function initExportFunction(){
 var userName =  document.getElementById("userName").value;
 if(!userName){
     userName = null;
 }
 var beginStatDate =  document.getElementById("beginStatDate").value;
 if(!beginStatDate){
     beginStatDate = null;
 }
 pageInfo = {
         page  : 1,
         limit : 10,
         beginStatDate:beginStatDate,
             userName: userName
     };
 	$('#btn_exports').click(function(){
 		var params = getParams();
 		executePost('../black/userBehavior/partExport', {'params' : JSON.stringify(params)});
 	});

 }
//function initExportFunction(){
//	$('#btn_exports').click(function(){
//		var params = getParams();
//		executePost('../black/userBehavior/exportExcel', {'params' : JSON.stringify(params)});
//	});
//
//}