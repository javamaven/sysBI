function getAction(select_id){
    var reslist=$("#"+select_id).select2("data"); //多选
    var arrStr = new Array();
    for(i=0;i<reslist.length;i++){

        arrStr.push(reslist[i].id)
    }
    return  arrStr;
};




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
//document.getElementById("STAT_PERIOD").value=getDate(1);


// 自适应高度
function tableHeight() {
	return $(window).height();
}

// 查询条件
var pageInfo = {
        page  : 1,
        limit : 10,
        userID :document.getElementById("user_Id").value,
        userName :document.getElementById("user_name").value,
//         channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
//      actionPlatform : getActionPlatform().toString().length == "0" ? null : getActionPlatform(),
        //action : getAction().toString().length == "0" ? null : getAction(),
        start_action_time  : document.getElementById("start_action_time").value,
        end_action_time : document.getElementById("end_action_time").value
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
    var i = 20;
    $.ajax({
        type: "POST",
        url: "../logUserBehavior/getAction",
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.Action){
                for(var key in msg.Action[list]){
                    if(key == "num")
                        str +='<option value="'+msg.Action[list][key]+'">' + msg.Action[list]["action"] + "</option>"
                }
            };
            $("#id_select").select2({
                maximumSelectionLength: 2,
                width:'100px'
            });
            $("#id_select").append(str);
        }
     });
};

function loadChannell(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
        url: "../logUserBehavior/getActionPlatform",
        data: JSON.stringify(),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.ActionPlatform){
                for(var key in msg.ActionPlatform[list]){
                   if(key == "num")
                        str +='<option value="'+msg.ActionPlatform[list][key]+'">' + msg.ActionPlatform[list]["actionPlatform"] + "</option>"
                }

            };
            $("#id_selects").select2({
                maximumSelectionLength: 2,
                width:'100px'
            });
            $("#id_selects").append(str);
        }
     });
};


function getParams(){
	var params = {
        	'userID': $("#user_Id").val(),


	};
	return params;
}

function loadTableAjax(){
 $.ajax({
  //请求方式
    type: "POST",
    //发送请求的地址
    url: "../logUserBehavior/list",
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
        '{field:"userID",title:"用户ID",align:"center",valign:"middle",class:""},'+
        '{field:"userName",title:"用户名",align:"center",valign:"middle"},'+
        '{field:"channlName",title:"渠道名称",align:"center",valign:"middle"},'+
        '{field:"channlMark",title:"渠道标记",align:"center",valign:"middle"},'+
        '{field:"actionTime",title:"操作时间",align:"center",valign:"middle"},'+
        '{field:"actionPlatform",title:"操作平台",align:"center",valign:"middle"},'+
        '{field:"action",title:"行为",align:"center",valign:"middle"},'+
        '{field:"projectType",title:"涉及项目类型",align:"center",valign:"middle",class:""},'+
        '{field:"projectAmount",title:"涉及项目本金",align:"center",valign:"middle"},'+
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
            actionPlatform : getAction("id_selects"),
             action : getAction("id_select"),
             userID :document.getElementById("user_Id").value,
              userName :document.getElementById("user_name").value,
              start_action_time: document.getElementById("start_action_time").value+" 00:00:00",
              end_action_time:document.getElementById("end_action_time").value+" 23:59:59"
          };
    //加载数据

    loadTableAjax();
});

$(function(){
     loadChannell();
    loadChannel();
    loadTableAjax();
    initExportFunction();
//$('#btn_exports').click(function(){
//    window.open("../logUserBehavior/partExport","_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
//});

});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../logUserBehavior/partExport', {'params' : JSON.stringify(params)});
	});

}