// 表格加载
function loadTable(columnsData,tableData,tableName){
    $(tableName).bootstrapTable({
        method: 'get',
        cache: false,
        height: tableName == "#labelTable" ? 300 : tableHeight(),
        pagination: true,
        pageSize: 20,
        pageNumber:1,
        pageList: [10, 20, 50, 100, 200, 500],
        // search: true,
        // showColumns: true,
        // showExport: true,
        // exportDataType: "all",              //basic', 'all', 'selected'.
        clickToSelect: true,
        columns: eval("("+columnsData+")"),
        data :eval("("+tableData+")") ,
        formatNoMatches: function(){
            return '无符合条件的记录';
        }
    });
    // 移除loading样式
    $(".spinners li").removeClass("active");

    if( tableName == "#reportTable"){
         // 显示导出按钮
         $("#btn_export").show();
     }

    refreshTable();
};
// 自适应高度
function tableHeight() {
	return $(window).height();
};

// 刷新表格样式
function refreshTable(){
     $('#reportTable').bootstrapTable('resetView');
     $('#labelTable').bootstrapTable('resetView');
}

function getLabelTagType(){
    // 显示之前，先把当前表格销毁
    $('#labelTable').bootstrapTable('destroy');
    document.getElementById("id_select").options.length=0;
    document.getElementById("id_select").options.add(new Option("请选择标签"));
    pageInfo = {
        type:$('#id_type option:selected').text()
     };

     $.ajax({
        type: "POST",
        url: "../labeltaguser/labelTaglist",
        data: JSON.stringify(pageInfo),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            var obj=document.getElementById('id_select');
            for(var list in msg.labelTaglist){
                for(var key in msg.labelTaglist[list]){
                    if(key == "mainlabelName"){
                        obj.options.add(new Option(msg.labelTaglist[list][key]));
                    }
                }
            };
            }
        });

};


var pageInfo = {
    mainLabelName:$('#id_select option:selected').text()
}

// 获取 标签统计条件
function getLabelList(){
    // 显示之前，先把当前表格销毁
    $('#labelTable').bootstrapTable('destroy');
    pageInfo = {
        mainLabelName:$('#id_select option:selected').text()
    };
     $.ajax({
            type: "POST",
            url: "../labeltaguser/labelTaglistdetail",
            data: JSON.stringify(pageInfo),
            contentType: "application/json;charset=utf-8",
            success : function(msg) {
                console.log(msg);
                 var a = '';
                for(var list in msg.labelList){
                    var d = '{'
                    for(var key in msg.labelList[list]){
                        d += '"' + key + '":"' + msg.labelList[list][key] + '",';
                    }
                    d = d.substring(0,d.length-1) + '},';
                    a += d;
                };
                a = '['+a.substring(0,a.length-1)+']';

                var b = '['+
                '{field:"labelID",title:"ID",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
                '{field:"labelType",title:"维度/指标",align:"center",valign:"middle",sortable:"true"},'+
                '{field:"labelContene",title:"内容",align:"center",valign:"middle",sortable:"true"},'+
                '{field:"labelLogic",title:"逻辑",align:"center",valign:"middle",sortable:"true"},'+
                '{field:"labelCondition",title:"条件",align:"center",valign:"middle",sortable:"true"},'+
                ']';

                //加载数据
                loadTable(b,a,'#labelTable');
                }
            });
}

function loadTableAjax(){
    // 显示之前，先把当前表格销毁
    $('#reportTable').bootstrapTable('destroy');
    //添加样式
    $(".spinners li").addClass("active");
    pageInfo = {
        mainLabelName:$('#id_select option:selected').text()
    };
     $.ajax({
        type: "POST",
        url: "../labeltaguser/list",
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
            var b = '['+
            '{field:"userId",title:"用户主键",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
            '{field:"cgUserId",title:"存管版主键",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
            '{field:"oldUserId",title:"普通版主键",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
            '{field:"phone",title:"手机号",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
            '{field:"username",title:"用户账号",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
            '{field:"realname",title:"用户姓名",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
            '{field:"registerTime",title:"注册日期",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"firstinvestTime",title:"首投日期",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"channelName",title:"渠道名称",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"sex",title:"性别",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"age",title:"用户年龄段",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"isInterflow",title:"是否注册存管系统",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"isDepository",title:"是否开通存管账号",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"invPeriod",title:"投资时间段",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"invInterval",title:"投资间隔",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"normalPeriodPreference",title:"项目期限偏好",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"changePeriodPreference",title:"债转期限偏好",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"cumulativeInvMoney",title:"累计投资金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"cumulativeInvMoneyYear",title:"累计投资年化金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"balance",title:"账户可用余额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"lastInvMoney",title:"最近一笔投资金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"invMaxMoney",title:"单笔投资最高金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"useVoucherProportion",title:"使用优惠投资的比例",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"cumulativeUseVoucherMoney",title:"累计使用的红包及券的金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"cumulativeUseVoucherCou",title:"累计使用的红包及券的次数",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"voucherEarningsProportion",title:"红包券收益占比已收收益比例",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"cumulativeUsageVoucher",title:"累计红包使用率",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"avgUseMoney",title:"次均红包金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"lastInvTime",title:"最近一次投资时间",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"lastRechargeTime",title:"最近一次充值时间",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"avgNormalMoney",title:"平均每笔项目投资金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"avgChangeMoney",title:"平均每笔债转投资金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"avgInvMoney",title:"平均每笔综合投资金额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"normalCou",title:"投资项目次数",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"changeCou",title:"投资债转次数",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"invCou",title:"投资综合次数",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"voucherBalance",title:"账户可用优惠余额",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"marketingDay",title:"距离上次营销天数",align:"center",valign:"middle",sortable:"true"},'+
            '{field:"lastLoginTime",title:"最近一次登录时间",align:"center",valign:"middle",sortable:"true"}'+
            ']';

            //加载数据
            loadTable(b,a,'#reportTable');

            //查询总条数
            getTotal();
            }
        });


};

// 获取查询总条数
function getTotal(){
     $.ajax({
            type: "POST",
            url: "../labeltaguser/labelTaglisttotal",
            data: JSON.stringify(pageInfo),
            contentType: "application/json;charset=utf-8",
            success : function(msg) {
                console.log(msg);
                var con  = $("#container");
                var s = "一共查询到：" + msg.total + " 条数据";
                var len = s.length;
                var index = 0;
                var tid = null;

                function start(){
                    con.text('');
                    tid = setInterval(function(){
                        con.append(s.charAt(index));
                        if(index ++ === len){
                            clearInterval(tid);
                            index = 0;
                        }
                    },100);
                }
                start();
                }
            });
};

$('#btn_search').click(function(){
    loadTableAjax();
});

$('#btn_export').click(function(){
    window.open("../labeltaguser/partExport","_blank",'height=400,width=400,top=100,left=200,toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
});

$(function(){
    $("#btn_export").hide();
    $(window).resize(function () {
        refreshTable();
    });
});