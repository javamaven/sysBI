$(function () {
	loadMainChart();//渠道分次投资图表
});


//var channelNameList = ['百度PC品专', 'SC-车主无忧-移动' , '券妈妈H5'];//百度PC品专","SC-车主无忧-移动","券妈妈H5
var channelNameList = [];
function getChannelName(){
	return channelNameList;
}

var pageInfo;
//main图表加载
function loadMainChart(){
    pageInfo = {
        page : 1,
        limit : 10000000,
        paramsJson : paramsJson
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


    $.ajax({
        type: "POST",
//        url: "../queryChannelAllChart",
//        url : "../../../channel/channelAll/mainChart",
        url: '/bi_sys/common/create-chart-image/queryChannelAllChart',
        data: JSON.stringify(pageInfo),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
        	msg = eval("("+msg+")");
        	
        	if(msg.channelNameList){
        		channelNameList = msg.channelNameList;
        	}
        	
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
            var worldMapContainer = document.getElementById('chartDiv');

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

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option,true);

         }
    });

}



//冒泡算法
function bubbleSort(Value) {
	var temp = 0;
	for (var i = 0; i < Value.length; i++) {
		for (var j = 0; j < Value.length - i; j++) {
			if (Value[j] > Value[j + 1]) {
				temp = Value[j];
				Value[j] = Value[j + 1];
				Value[j + 1] = temp;
			}
		}
	}
	return Value;
}
