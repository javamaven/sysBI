
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
    var halfYearAgo = new Date(new Date()-24*60*60*1000*2);
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
document.getElementById("reg_begindate").value=getDate(2);
document.getElementById("reg_enddate").value = getYesterday(1);


// 自适应高度
function tableHeight() {
	return $(window).height();
}


function getParams(){
	var params = {
        	'statPeriod': $("#statPeriod").val(),


	};
	return params;
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
function loadChannel(){
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
            };
            $("#id_select").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#id_select").append(str);
        }
     });
};

function loadTableAjax(){
 $.ajax({
    type: "POST",
    url: "../market/list",
    data: JSON.stringify(pageInfo),
    contentType: "application/json;charset=utf-8",
    success : function(msg) {
        console.log(msg);
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
        var b = '['+
        '{field:"statPeriod",title:"日期",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
        '{field:"channelHead",title:"主负责人",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"type",title:"渠道类型",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"channelName",title:"渠道名称",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"actualCost",title:"实际消费",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"regCou",title:"新增注册人",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstinvestCou",title:"新增首投人数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstinvestMoney",title:"首投金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstinvestYMoney",title:"首投年化金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"invCou",title:"投资总人数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"invMoney",title:"投资总金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"invYMoney",title:"年化投资总金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"ddzMoney",title:"点点赚购买金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"regCost",title:"注册成本",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstinvestCost",title:"首投成本",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"avgFirstinvestMoney",title:"人均首投",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"regInvConversion",title:"注册人投资转化率",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"firstinvestRot",title:"首投ROI",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"cumulativeRot",title:"累计ROI",align:"center",valign:"middle",sortable:"true"}'+
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
            channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
            channelName_a : getChannelName().toString().length == "0" ? null : getChannelName(),
            channelHead :document.getElementById("channelHead").value,
            reg_begindate: document.getElementById("reg_begindate").value.replace(/-/g,""),
            reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
        };
    //加载数据
    loadTableAjax();
});

$(function(){

    loadChannel();
    loadTableAjax();
initExportFunction();
//$('#btn_exports').click(function(){
//    window.open("../market/partExport","_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
//});

});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../market/partExport', {'params' : JSON.stringify(params)});
	});

}