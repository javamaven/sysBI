
function getDate(datatype){
    var today = new Date(new Date()-24*60*60*1000);
    var halfYearAgo = new Date(new Date()-24*60*60*1000*91);
    var startdate;
    var enddate;
    startdate = (today.getFullYear())  +'-'+
        (today.getMonth() + 1 >9  ? (today.getMonth() + 1 ) : "0"+(today.getMonth() + 1 ))  + '-'+
        (today.getDate() > 10 ? today.getDate() : "0" + today.getDate());
    enddate = (halfYearAgo.getFullYear())  +'-'+
        (halfYearAgo.getMonth() + 1 >9  ? (halfYearAgo.getMonth() + 1 ) : "0"+(halfYearAgo.getMonth() + 1 ))+
        "-01";
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
document.getElementById("reg_enddate").value=getDate(1);
document.getElementById("invest_enddate").value=getDate(1);

// 自适应高度
function tableHeight() {
	return $(window).height();
}


// 查询条件
var pageInfo = {
        page : 1,
        limit : 20,
        reg_begindate  : document.getElementById("reg_begindate").value.replace(/-/g,""),
        reg_enddate : document.getElementById("reg_enddate").value.replace(/-/g,""),
        invest_enddate : document.getElementById("invest_enddate").value.replace(/-/g,""),
        investTimes : document.getElementById("invest_times").value
    };

// 冒泡算法
function bubbleSort(Value){
    var temp = 0;
    for (var i = 0; i < Value.length; i++)
    {
        for (var j = 0; j < Value.length - i; j++)
        {
            if (Value[j] > Value[j + 1])
            {
                temp = Value[j];
                Value[j] = Value[j+1];
                Value[j+ 1] = temp;
            }
        }
    };
    return Value;
}

// 表格加载
function loadTable(columnsData,tableData){
    $('#reportTable').bootstrapTable({
        method: 'get',
        cache: false,
        height: tableHeight(),
        // striped: true,
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
            }

            $("#id_select").select2({
                maximumSelectionLength: 3,
                width:'100%'
            });
            $("#id_select").append(str);
        }
     });
};

// main图表加载
function loadMainChart(){

    pageInfo = {
        page : 1,
        limit : 20,
        reg_begindate  : document.getElementById("reg_begindate").value.replace(/-/g,""),
        reg_enddate : document.getElementById("reg_enddate").value.replace(/-/g,""),
        invest_enddate : document.getElementById("invest_enddate").value.replace(/-/g,""),
        channelName : getChannelName().toString().length == "0" ? null : getChannelName(),
        investTimes : document.getElementById("invest_times").value
    };

    // 投资人数
    var strInvestUsers = '';
    // 留存率
    var strStayPer = '';
    // 投资人数2
    var strInvestUsers2 = '';
    // 留存率2
    var strStayPer2 = '';
    // 投资人数3
    var strInvestUsers3 = '';
    // 留存率3
    var strStayPer3 = '';

    var arrayValue = new Array();

    var scn4='';
    var scn5='';
    var scn6='';
    var legend_data= new Array();

    if (getChannelName().length != 0 ) {
        for (i=0;i<getChannelName().length ;i++ )
            {
                switch(i%3){
                case 0:
                    scn4 = getChannelName()[i];
                    break;
                case 1:
                    scn5 = getChannelName()[i];
                    break;
                case 2:
                    scn6 = getChannelName()[i];
                    break;
                }

                legend_data.push(getChannelName()[i]+"投资人数");
                legend_data.push(getChannelName()[i]+"留存率");
            }
    }
    else{
        legend_data.push("百度投资人数");
        legend_data.push("百度留存率");
        legend_data.push("小米商店投资人数");
        legend_data.push("小米商店留存率");
    };


    $.ajax({
        type: "POST",
        url: "../channel/channelAll/mainChart",
        data: JSON.stringify(pageInfo),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            for(var list in msg.mainChart){
                var i = 0;
                for(var key in msg.mainChart[list]){

                    if(getChannelName().length == 0){
                        if(msg.mainChart[list]["channelName"]=='百度' ){
                            if(key == "investUsers")
                                strInvestUsers += msg.mainChart[list]["investUsers"]+',';
                            if(key == "stayPer")
                                strStayPer += msg.mainChart[list]["stayPer"]+',';
                        }
                        if(msg.mainChart[list]["channelName"]=='小米商店'){
                            if(key == "investUsers")
                                strInvestUsers2 +=msg.mainChart[list]["investUsers"]+',';
                            if(key == "stayPer")
                                strStayPer2 += msg.mainChart[list]["stayPer"]+',';
                        }
                    }
                    else{

                        if(msg.mainChart[list]["channelName"]==scn4 ){
                            if(key == "investUsers")
                                strInvestUsers += msg.mainChart[list]["investUsers"]+',';
                            if(key == "stayPer")
                                strStayPer += msg.mainChart[list]["stayPer"]+',';
                        }
                        if(msg.mainChart[list]["channelName"]==scn5 ){
                            if(key == "investUsers")
                                strInvestUsers2 += msg.mainChart[list]["investUsers"]+',';
                            if(key == "stayPer")
                                strStayPer2 += msg.mainChart[list]["stayPer"]+',';
                        }
                        if(msg.mainChart[list]["channelName"]==scn6 ){
                            if(key == "investUsers")
                                strInvestUsers3 += msg.mainChart[list]["investUsers"]+',';
                            if(key == "stayPer")
                                strStayPer3 += msg.mainChart[list]["stayPer"]+',';
                            ;
                        }

                    }
                    arrayValue.push(msg.mainChart[list]["investUsers"]);
                }
             };
            strInvestUsers = '['+(strInvestUsers.substring(0,strInvestUsers.length-1))+']';
            strStayPer = '['+(strStayPer.substring(0,strStayPer.length-1))+']';

            strInvestUsers2 = '['+(strInvestUsers2.substring(0,strInvestUsers2.length-1))+']';
            strStayPer2 = '['+(strStayPer2.substring(0,strStayPer2.length-1))+']';

            strInvestUsers3 = '['+(strInvestUsers3.substring(0,strInvestUsers3.length-1))+']';
            strStayPer3 = '['+(strStayPer3.substring(0,strStayPer3.length-1))+']';

            //百位取整
            strValue = bubbleSort(arrayValue);
            var maxValue = strValue[strValue.length-1];
            maxValue = ((parseInt(maxValue/100))+1)*100;
            var BarInterval = parseInt(maxValue/5);


            //折线图
            var worldMapContainer = document.getElementById('main');
            //用于使chart自适应高度和宽度,通过窗体高宽计算容器高宽
            var resizeWorldMapContainer = function () {
              worldMapContainer.style.width = document.getElementById("showDiv").offsetWidth+'px';
              worldMapContainer.style.height = '400px';
            };

            var investTimeArr = new Array();
            var invest_times = $("#invest_times").val();
            if(invest_times > 24){
                for(var i=1;i <= invest_times;i++){
                    investTimeArr.push(i);
                }
            }else{
                for(var i=1;i <= 24;i++){
                    investTimeArr.push(i);
                }
            };
			if (getChannelName().length == 0){
					item1 = {
				            name:legend_data[0],
				            type:'bar',
				            data:eval("("+strInvestUsers+")")
					};
					item2 = {
				            name:legend_data[1],
				            type:'line',
				            yAxisIndex: 1,
				            data:eval("("+strStayPer+")")
					};
					item3 = {
				            name:legend_data[2],
				            type:'bar',
				            data:eval("("+strInvestUsers2+")")
					};
					item4 = {
				            name:legend_data[3],
				            type:'line',
				            yAxisIndex: 1,
				            data:eval("("+strStayPer2+")")
					};
				}
				else{
					item5 = {
				            name:legend_data[0],
				            type:'bar',
				            data:eval("("+strInvestUsers+")")
					};
					item6 = {
				            name:legend_data[1],
				            type:'line',
				            yAxisIndex: 1,
				            data:eval("("+strStayPer+")")
					};
					item7 = {
				            name:legend_data[2],
				            type:'bar',
				            data:eval("("+strInvestUsers2+")")
					};
					item8 = {
				            name:legend_data[3],
				            type:'line',
				            yAxisIndex: 1,
				            data:eval("("+strStayPer2+")")
					};
					item9 = {
				            name:legend_data[4],
				            type:'bar',
				            data:eval("("+strInvestUsers3+")")
					};
					item10 = {
				            name:legend_data[5],
				            type:'line',
				            yAxisIndex: 1,
				            data:eval("("+strStayPer3+")")
					};
				}

            //设置容器高宽
            resizeWorldMapContainer();
            //基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(worldMapContainer,'shine');

            // 指定图表的配置项和数据
            var option = {
                    tooltip: {
                        trigger: 'axis'
                    },
                    toolbox: {
                        feature: {
                            dataView: {show: true, readOnly: false},
                            magicType: {show: true, type: ['line', 'bar']},
                            restore: {show: true},
                            saveAsImage: {show: true}
                        }
                    },
                    legend: {
                        data:[legend_data[0],legend_data[1],legend_data[2],legend_data[3],legend_data[4],legend_data[5]]
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data:investTimeArr
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: '投资人数',
                            min: 0,
                            max: maxValue,
                            interval: BarInterval,
                            axisLabel: {
                                formatter: '{value}'
                            }
                        },
                        {
                            type: 'value',
                            name: '留存量',
                            min: 0,
                            max: 100,
                            interval: 20,
                            axisLabel: {
                                formatter: '{value} %'
                            }
                        }
                    ],
                    series: []
            };

				if (getChannelName().length == 0){
					option.series.push(item1);
					option.series.push(item2);
					option.series.push(item3);
					option.series.push(item4);
				}
				else{

					option.series.push(item5);
					option.series.push(item6);
					option.series.push(item7);
					option.series.push(item8);
					option.series.push(item9);
					option.series.push(item10);
				}

            //用于使chart自适应高度和宽度
            window.onresize = function () {
                //重置容器高宽
                resizeWorldMapContainer();
                myChart.resize();
            };
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option,true);

         }
    });

}

function loadTableAjax(){
 $.ajax({
    type: "POST",
    url: "../channel/channelAll/list",
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
        '{field:"channelName",title:"渠道",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"investTimes",title:"投资次数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"stayPer",title:"留存率",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"investUsers",title:"投资用户数",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"moneyVoucherPer",title:"次均红包金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"moneyInvestPer",title:"次均投资金额",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"borrowPeriodPer",title:"平均投资期限",align:"center",valign:"middle",sortable:"true"},'+
        '{field:"intervalsPer",title:"平均投资间隔",align:"center",valign:"middle",sortable:"true"}'+
        ']';

        //加载数据
        loadTable(b,a);

        $(window).resize(function () {
            $('#reportTable').bootstrapTable('resetView');
        });

        loadMainChart();

        }
    });
}

$("#searchButton").click(function() {
    // 隐藏图表
    document.getElementById('main').style.height = '0px';
    echarts.init(document.getElementById('main')).hideLoading();
    // 显示之前，先把当前表格销毁
    $('#reportTable').bootstrapTable('destroy');
    //添加样式
    $(".spinners li").addClass("active");
    pageInfo = {
        page : 1,
        limit : 20,
        reg_begindate  : document.getElementById("reg_begindate").value.replace(/-/g,""),
        reg_enddate : document.getElementById("reg_enddate").value.replace(/-/g,""),
        invest_enddate : document.getElementById("invest_enddate").value.replace(/-/g,""),
        channelName  : getChannelName().toString().length == "0" ? null : getChannelName(),
        investTimes : document.getElementById("invest_times").value
    };
    //加载数据
    loadTableAjax();

});

// 获取渠道信息
function getChannelName(){
    var arrStr = new Array();
    $(".select2-selection__choice").each(function(){
        arrStr.push($(this).attr("title"))
        });
    return  arrStr;
}

$(function(){

    loadChannel();
    loadTableAjax();


});
