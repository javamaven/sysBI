$(function(){
    initExportFunction();
    initEvent();
//    initTableGrid();
});
function queryParams(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
      limit: params.pageSize,   //页面大小
      page: params.pageNumber,  //页码
      minSize: $("#leftLabel").val(),
      maxSize: $("#rightLabel").val(),
      minPrice: $("#priceleftLabel").val(),
      maxPrice: $("#pricerightLabel").val(),
      statPeriod: document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
      sourcecaseno: $("#SOURCECASENO").val(),
      customername: $("#CUSTOMERNAME").val(),
      iscompleted: $("#ISCOMPLETED").val(),
      isstamp: $("#ISSTAMP").val()
    };
    return temp;
  }
function initTableGrid(){
	//初始化Table
    $('#reportTable').bootstrapTable({
        url: "../dmreportcgreport/list", //请求后台的URL（*）
        data: JSON.stringify(getQueryParams()),
        dataType: "json",
        method: 'get',                      //请求方式（*）
        //toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams, //参数
        queryParamsType: "page", //参数格式,发送标准的RESTFul类型的参数请求
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 20,                       //每页的记录行数（*）并控制分页
        pageList: [20, 50, 100, 200],        //可供选择的每页的行数（*）
//        search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
//        showColumns: true,                  //是否显示所有的列
//        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        height: tableHeight(),                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
//        showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
//        cardView: false,                    //是否显示详细视图
        detailView: false ,                  //是否显示父子表
        formatNoMatches: function () {  //没有匹配的结果
            return '无符合条件的记录';
          },

        columns:  [
            {field:"statPeriod",title:"统计日期",align:"center",valign:"middle",sortable:"true"},//居中对齐
            {field:"sourcecaseno",title:"平台项目编号",align:"center",valign:"middle",sortable:"true"},//居中对齐
            {field:"department",title:"　部门　",align:"center",valign:"middle",sortable:"true"},//居中对齐
            {field:"projectBelong",title:"项目归属",align:"center",valign:"middle",sortable:"true"},//居中对齐
            {field:"projectType",title:"项目类型",align:"center",valign:"middle",sortable:"true"},
            {field:"customername",title:"借款人",align:"center",valign:"middle",sortable:"true"},
            {field:"payformoney",title:"金额",align:"center",valign:"middle",sortable:"true"},
            {field:"loanrate",title:"利率",align:"center",valign:"middle",sortable:"true"},
            {field:"loanyearlimit",title:"期限-月",align:"center",valign:"middle",sortable:"true"},
            {field:"payforlimittime",title:"期限-日",align:"center",valign:"middle",sortable:"true"},
            {field:"giveoutmoneytime",title:"　满标放款日　",align:"center",valign:"middle",sortable:"true"},
            {field:"willgetmoneydate",title:"　　到期日　　",align:"center",valign:"middle",sortable:"true"},
            {field:"iscompleted",title:"资料签名、盖章是否完全",align:"center",valign:"middle",sortable:"true"},
            {field:"sendDeadline",title:"纸质文本最迟寄送日（发标5个工作日内）",align:"center",valign:"middle",sortable:"true"},
            {field:"isstamp",title:"纸文本是否盖章",align:"center",valign:"middle",sortable:"true"}
        ]

    });
}



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


function getQueryParams(){

	return {
	        page  :1,
	        limit :20,
	         statPeriod: document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
             sourcecaseno: $("#SOURCECASENO").val(),
             customername: $("#CUSTOMERNAME").val(),
             iscompleted: $("#ISCOMPLETED").val(),
             isstamp: $("#ISSTAMP").val()
	    };
}



function getParams(){
	var params = {
        	 statPeriod: document.getElementById("STAT_PERIOD").value.replace(/-/g,""),
             sourcecaseno: $("#SOURCECASENO").val(),
             customername: $("#CUSTOMERNAME").val(),
             iscompleted: $("#ISCOMPLETED").val(),
             isstamp: $("#ISSTAMP").val()

	};
	return params;
}



$("#searchButton").click(function(){
//reload();
	getQueryParams();
    // 显示之前，先把当前表格销毁
      $('#reportTable').bootstrapTable('destroy');




    //加载数据
    initTableGrid();


});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportcgreport/partExport', {'params' : JSON.stringify(params)});
	});

}


