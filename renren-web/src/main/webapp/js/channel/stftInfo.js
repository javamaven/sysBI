$(function(){
    loadChannel();
    initExportFunction();
    
//    loadTableAjax();
    // 移除loading样式
    $(".spinners li").removeClass("active");
});

var currDataList;
function initExportFunction(){
	$('#btn_exports').click(function(){
//	    window.open("../channel/stft/exportExcel?list=" + JSON.stringify(currDataList),"_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
		if(currDataList){
			executePost('../channel/stft/exportExcel', {'list': currDataList } );  
		}else {
			alert('请先查询数据!');
		}
	});

}

     
function loadTableAjax(){
 $.ajax({
    type: "POST",
    url: "../channel/stft/stftInfo",
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
document.getElementById("inv_begindate").value = addDate(getCurrDate(), -90);
document.getElementById("inv_enddate").value = getYesterday();


// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
        invBeginDate  : document.getElementById("inv_begindate").value.replace(/-/g,""),
//        invBeginDate  : document.getElementById("reg_begindate").value.replace(/-/g,""),
        invEndDate : document.getElementById("inv_enddate").value.replace(/-/g,""),
//        regEndDate : document.getElementById("reg_enddate").value.replace(/-/g,"")
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
            {field:"registerUserNum",title:"注册人数",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestUserNum",title:"首投人数",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestAmount",title:"首投金额",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestYearAmount",title:"首投年化投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestPer",title:"人均首投",align:"center",valign:"middle",sortable:"true"},
//            {field:"conversionRate",title:"转化率",align:"center",valign:"middle",sortable:"true"},
            {field:"conversionRateText",title:"转化率",align:"center",valign:"middle",sortable:"true"},
            {field:"multipleUser",title:"复投人数",align:"center",valign:"middle",sortable:"true"},
//            {field:"multipleRate",title:"复投率",align:"center",valign:"middle",sortable:"true"},
            {field:"multipleRateText",title:"复投率",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestProAmount",title:"首投用户项目投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"proInvestAmount",title:"项目投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"proMultiInvestAmount",title:"项目复投金额",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestUserAmount",title:"首投用户投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"userInvestAmount",title:"用户投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"userInvestYearAmount",title:"用户年化投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestPerTime",title:"首投平均期限",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestUserPerProAmount",title:"首投用户平均项目投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"perProInvestAmont",title:"平均项目投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"perProMultiInvestAmount",title:"平均项目复投金额",align:"center",valign:"middle",sortable:"true"},
            {field:"firstInvestUserPerAmount",title:"首投用户平均投资金额",align:"center",valign:"middle",sortable:"true"},
            {field:"perInvestAmount",title:"平均投资金额",align:"center",valign:"middle",sortable:"true"},
            
//            {field:"amountMultiRate",title:"金额复投率",align:"center",valign:"middle",sortable:"true"}
            {field:"amountMultiRateText",title:"金额复投率",align:"center",valign:"middle",sortable:"true"}
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
	   var str = '';
	    var i = 0;
	    $.ajax({
	        type: "POST",
	        url: "../channel/channelAll/getChannel",
	        data: JSON.stringify(),
	        contentType: "application/json;charset=utf-8",
	        success : function(msg) {
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
	var params = {
            page  : 1,
            limit : 10000,
            channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
	        invEndDate : document.getElementById("inv_enddate").value.replace(/-/g,""),
            invBeginDate: document.getElementById("inv_begindate").value.replace(/-/g,""),
//            regEndDate:document.getElementById("reg_enddate").value.replace(/-/g,"")
        };
	return params;
}


