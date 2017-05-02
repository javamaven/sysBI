$(function(){
    loadChannel();
//    loadTableAjax();
    
    // 移除loading样式
    $(".spinners li").removeClass("active");
});

function loadTableAjax(){
 $.ajax({
    type: "POST",
    url: "../channel/renew/queryChannelRenewDataList",
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
       
        //加载数据
        loadTable(null ,a);

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
document.getElementById("date").value=getDate(1);


// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
        date : document.getElementById("date").value.replace(/-/g,"")
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
//        columns: eval("("+columnsData+")"),
        columns:  [
            {field:"channelName",title:"渠道名称",align:"center",valign:"middle",sortable:"true"},//居中对齐
            {field:"channelLabel",title:"渠道标签",align:"center",valign:"middle",sortable:"true"},
            {field:"channelType",title:"渠道分类",align:"center",valign:"middle",sortable:"true"},
            
            {field:"day30Cost",title:"30天费用",align:"center",valign:"middle",sortable:"true"},
            {field:"day60Cost",title:"60天费用",align:"center",valign:"middle",sortable:"true"},
            {field:"day90Cost",title:"90天费用",align:"center",valign:"middle",sortable:"true"},
            
            {field:"onlineTime",title:"上线时间",align:"center",valign:"middle",sortable:"true"},
          
            {field:"day30YearAmount",title:"30日年化投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"day60YearAmount",title:"60日年化投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"day90YearAmount",title:"90日年化投资金额",align:"center",valign:"middle",sortable:"true"},

            {field:"day30YearRoi",title:"30日年化ROI",align:"center",valign:"middle",sortable:"true"},
            {field:"day60YearRoi",title:"60日年化ROI",align:"center",valign:"middle",sortable:"true"},
            {field:"day90YearRoi",title:"90日年化ROI",align:"center",valign:"middle",sortable:"true"},
            
            {field:"day30FirstInvestUserNum",title:"30日首投用户数",align:"center",valign:"middle",sortable:"true"},
            {field:"day60FirstInvestUserNum",title:"60日首投用户数",align:"center",valign:"middle",sortable:"true"},
            {field:"day90FirstInvestUserNum",title:"90日首投用户数",align:"center",valign:"middle",sortable:"true"},
            
            {field:"day30MultiInvestUserNum",title:"30日复投用户数",align:"center",valign:"middle",sortable:"true"},
            {field:"day60MultiInvestUserNum",title:"60日复投用户数",align:"center",valign:"middle",sortable:"true"},
            {field:"day90MultiInvestUserNum",title:"90日复投用户数",align:"center",valign:"middle",sortable:"true"},

            {field:"day30MultiRateText",title:"30日复投率",align:"center",valign:"middle",sortable:"true"},
            {field:"day60MultiRateText",title:"60日复投率",align:"center",valign:"middle",sortable:"true"},
            {field:"day90MultiRateText",title:"90日复投率",align:"center",valign:"middle",sortable:"true"},
            
            {field:"day30MultiInvestAmountRateText",title:"30日复投金额比",align:"center",valign:"middle",sortable:"true"},
            {field:"day60MultiInvestAmountRateText",title:"60日复投金额比",align:"center",valign:"middle",sortable:"true"},
            {field:"day90MultiInvestAmountRateText",title:"90日复投金额比",align:"center",valign:"middle",sortable:"true"},

            {field:"day30FirstInvestYearAmount",title:"30日首投年化金额",align:"center",valign:"middle",sortable:"true"},
            {field:"day60FirstInvestYearAmount",title:"60日首投年化金额",align:"center",valign:"middle",sortable:"true"},
            {field:"day90FirstInvestYearAmount",title:"90日首投年化金额",align:"center",valign:"middle",sortable:"true"},
            
            {field:"day30perFirstInvestYearAmount",title:"30日人均首投年化金额",align:"center",valign:"middle",sortable:"true"},
            {field:"day60perFirstInvestYearAmount",title:"60日人均首投年化金额",align:"center",valign:"middle",sortable:"true"},
            {field:"day90perFirstInvestYearAmount",title:"90日人均首投年化金额",align:"center",valign:"middle",sortable:"true"},
            
            {field:"day30FirstInvestYearRoi",title:"30日首投年化ROI",align:"center",valign:"middle",sortable:"true"},
            {field:"day60FirstInvestYearRoi",title:"60日首投年化ROI",align:"center",valign:"middle",sortable:"true"},
            {field:"day90FirstInvestYearRoi",title:"90日首投年化ROI",align:"center",valign:"middle",sortable:"true"}
        ],
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
//        	console.info(msg);
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
    pageInfo = {
            page  : 1,
            limit : 1000000,
            channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
            date:document.getElementById("date").value.replace(/-/g,"")
        };
    //加载数据
    loadTableAjax();
});


