var stataDay;
$(function () {
	loadChannel();
	 initExportFunction();
	stataDay = getDate(1);
	document.getElementById("stat_day").value=stataDay;
	initTimeCond();
	initTable();
	queryTotalInfo(stataDay);
});

/**
 * 将数值四舍五入后格式化.
 *
 * @param num 数值(Number或者String)
 * @param cent 要保留的小数位(Number)
 * @param isThousand 是否需要千分位 0:不需要,1:需要(数值类型);
 * @return 格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatNumber(num,cent) {
	num = num.toString().replace(/\$|\,/g,'');
	 var isThousand = 1;
	 // 检查传入数值为数值类型
	  if(isNaN(num))
	   num = "0";
	
	 // 获取符号(正/负数)
	 sign = (num == (num = Math.abs(num)));
	
	 num = Math.floor(num*Math.pow(10,cent)+0.50000000001); // 把指定的小数位先转换成整数.多余的小数位四舍五入
	 cents = num%Math.pow(10,cent);       // 求出小数位数值
	 num = Math.floor(num/Math.pow(10,cent)).toString();  // 求出整数位数值
	 cents = cents.toString();        // 把小数位转换成字符串,以便求小数位长度
	
	 // 补足小数位到指定的位数
	 while(cents.length<cent)
	  cents = "0" + cents;
	
	 if(isThousand) {
	  // 对整数部分进行千分位格式化.
	  for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	   num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));
	 }
	
	 if (cent > 0)
	  return (((sign)?'':'-') + num + '.' + cents);
	 else
	  return (((sign)?'':'-') + num);
}
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = {
        	'statPeriod': $("#stat_day").val(), 
        	'afterInvestBalance_start': $("#start_multi_invest_money").val(), 
        	'afterInvestBalance_end': $("#end_multi_invest_money").val(),
        	'startFirstInvestTime': $("#start_first_invest_time").val() ,
        	'endFirstInvestTime': $("#end_first_invest_time").val(),
        	'startTotalMoney': $("#start_total_money").val(),
        	'endTotalMoney': $("#end_total_money").val(),
        	'startTotalInvestAmount': $("#start_total_invest_amount").val(),
        	'endTotalInvestAmount': $("#end_total_invest_amount").val(),
        	'startFirstInvestAmount': $("#start_first_invest_amount").val(),
        	'endFirstInvestAmount': $("#end_first_invest_amount").val(),
        	'startRegisterTime': $("#start_register_time").val(),
        	'endRegisterTime': $("#end_register_time").val(),
        	'bangCard': $("#if_bang_card").val(),
        	'realName': $("#if_real_name").val(),
        	'channelName': getChannelName().toString().length == "0" ? null : getChannelName()
        }
//		encodeURI(JSON.stringify(params))
		executePost('../channel/manager/exportExcel', {'params' : JSON.stringify(params)});  
//		executePost('../channel/manager/exportExcel', {'list': currDataList } );  
//		 $.ajax({
//		        type: "POST",
//		        url: "../channel/manager/exportExcel",
//		        async: false,
//		        data: JSON.stringify(params),
//		        contentType: "application/json;charset=utf-8",
//		        success : function(retData) {
//		        	
//		        }
//		     });
	});

}
function executePost(URL, PARAMS) {        
    var temp = document.createElement("form");        
    temp.action = URL;        
    temp.method = "post";        
    temp.style.display = "none";        
    for (var x in PARAMS) {        
        var opt = document.createElement("textarea");        
        opt.name = x;        
        opt.value = PARAMS[x];        
        temp.appendChild(opt);        
    }        
    document.body.appendChild(temp);        
    temp.submit();        
    return temp;        
} 
function resetTotalInfo(){
	var char = '-';
	$("#register_user_num").html(char);
	$("#invest_user_num").html(char);
	$("#first_invest_rate").html(char);
	$("#total_invest_amount").html(char);
	$("#first_invest_amount").html(char);
	$("#cre_invest_amount").html(char);
	$("#multi_invest_user_num").html(char);
	$("#multi_invest_amount").html(char);
	$("#multi_invest_rate").html(char);
}

function getParams(){
	var params = {
			'statPeriod': $("#stat_day").val(), 
        	'afterInvestBalance_start': $("#start_multi_invest_money").val(), 
        	'afterInvestBalance_end': $("#end_multi_invest_money").val(),
        	'startFirstInvestTime': $("#start_first_invest_time").val() ,
        	'endFirstInvestTime': $("#end_first_invest_time").val(),
        	'startTotalMoney': $("#start_total_money").val(),
        	'endTotalMoney': $("#end_total_money").val(),
        	'startTotalInvestAmount': $("#start_total_invest_amount").val(),
        	'endTotalInvestAmount': $("#end_total_invest_amount").val(),
        	'startFirstInvestAmount': $("#start_first_invest_amount").val(),
        	'endFirstInvestAmount': $("#end_first_invest_amount").val(),
        	'startRegisterTime': $("#start_register_time").val(),
        	'endRegisterTime': $("#end_register_time").val(),
        	
        	'bangCard': $("#if_bang_card").val(),
        	'realName': $("#if_real_name").val(),
        	'channelName': getChannelName().toString().length == "0" ? null : getChannelName()
	};
	return params;
}

function queryTotalInfo(stataDay){
	var params = getParams();
	 $.ajax({
	        type: "POST",
	        url: "../channel/manager/totalList",
	        async: false,
	        data: JSON.stringify(params),
	        contentType: "application/json;charset=utf-8",
	        success : function(retData) {
	        	var data = retData.data;
	        	$("#register_user_num").html(data.registerUserNum);
	        	$("#invest_user_num").html(data.investUserNum);
	        	$("#first_invest_rate").html((data.firstInvestRate*100).toFixed(2) + "%");
	        	$("#total_invest_amount").html(formatNumber(data.totalInvestAmount, 2));
	        	$("#first_invest_amount").html(formatNumber(data.firstInvestAmount, 2));
	        	$("#cre_invest_amount").html(formatNumber(data.changeInvestAmount, 2));
	        	$("#multi_invest_user_num").html(data.multiInvestUserNum);
	        	$("#multi_invest_amount").html(formatNumber(data.multiInvestAmount, 2));
	        	$("#multi_invest_rate").html((data.multiInvestRate*100).toFixed(2) + "%");
	        }
	     });
	 
}

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

function initTimeCond(){

    $("#stat_day").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true,
        endDate:getDate(1)
    }).on("click",function(){
//        $("#stat_day").datetimepicker("setEndDate",$("#stat_day").val())
    });
    
    $("#start_first_invest_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
//        endDate:getDate(1)
    }).on("click",function(){
    });
    $("#end_first_invest_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
//        endDate:getDate(1)
    }).on("click",function(){
    });
    $("#start_register_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#end_register_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    
    
}

var params = {};

function initTable(){
	 $("#jqGrid").jqGrid({
	        url: '../channel/manager/list',
	        datatype: "json",
	        postData: {'statPeriod': getDate(1)},
	        colModel: [			
				{ label: '统计日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 85, key: true },
				{ label: '用户ID', name: 'userId', index: '$USER_ID', width: 80 }, 			
				{ label: '用户名', name: 'username', index: '$USERNAME', width: 80 }, 			
//				{ label: '渠道ID', name: 'channelId', index: '$CHANNEL_ID', width: 80 }, 			
				{ label: '渠道名称', name: 'channelName', index: '$CHANNEL_NAME', width: 80 }, 			
				{ label: '渠道标记', name: 'channelMark', index: '$CHANNEL_MARK', width: 80 }, 			
				{ label: '注册时间', name: 'registerTime', index: '$REGISTER_TIME', width: 150 }, 			
				{ label: '实名', name: 'isRealname', index: '$IS_REALNAME', width: 50 }, 			
				{ label: '绑卡', name: 'isBinding', index: '$IS_BINDING', width: 50 }, 			
				{ label: '激活投资时间', name: 'activateInvestTime', index: '$ACTIVATE_INVEST_TIME', width: 100 }, 			
				{ label: '首投时间', name: 'firstInvestTime', index: '$FIRST_INVEST_TIME', width: 80 }, 			
				{ label: '首投金额', name: 'firstInvestBalance', index: '$FIRST_INVEST_BALANCE', width: 80 }, 			
				{ label: '首投期限', name: 'firstInvestPeriod', index: '$FIRST_INVEST_PERIOD', width: 80 }, 			
				{ label: '复投金额', name: 'afterInvestBalance', index: '$AFTER_INVEST_BALANCE', width: 80 }, 			
				{ label: '复投次数', name: 'afterInvestNumber', index: '$AFTER_INVEST_NUMBER', width: 80 }, 			
				{ label: '累计投资金额', name: 'totalInvestBalance', index: '$TOTAL_INVEST_BALANCE', width: 80 }, 			
				{ label: '累计投资次数', name: 'totalInvestNumber', index: '$TOTAL_INVEST_NUMBER', width: 80 }, 			
				{ label: '债转投资金额', name: 'changeInvestBalance', index: '$CHANGE_INVEST_BALANCE', width: 80 }, 			
				{ label: '帐户总资产', name: 'totalCapital', index: '$TOTAL_CAPITAL', width: 80 }		
	        ],
			viewrecords: true,
	        height: 385,
	        rowNum: 10,
			rowList : [10,30,50],
	        rownumbers: true, 
	        rownumWidth: 25, 
//	        fitColumns: true,
	        autowidth:true,
//	        shrinkToFit:false,
	        multiselect: true,
	        pager: "#jqGridPager",
	        jsonReader : {
	            root: "page.list",
	            page: "page.currPage",
	            total: "page.totalPage",
	            records: "page.totalCount",
	            
	        },
	        prmNames : {
	            page:"page", 
	            rows:"limit", 
	            statPeriod: "statPeriod",
	            order: "order"
	        },
	        gridComplete:function(){
	        	//隐藏grid底部滚动条
	        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	        	$("#query_cond").removeAttr("disabled");
	        }
	    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportUserActivateDaily: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportUserActivateDaily = {};
		},
		update: function (event) {
			var statPeriod = getSelectedRow();
			if(statPeriod == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(statPeriod)
		},
		saveOrUpdate: function (event) {
			var url = vm.dmReportUserActivateDaily.statPeriod == null ? "../dmreportuseractivatedaily/save" : "../dmreportuseractivatedaily/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportUserActivateDaily),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var statPeriods = getSelectedRows();
			if(statPeriods == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../dmreportuseractivatedaily/delete",
				    data: JSON.stringify(statPeriods),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(statPeriod){
			$.get("../dmreportuseractivatedaily/info/"+statPeriod, function(r){
                vm.dmReportUserActivateDaily = r.dmReportUserActivateDaily;
            });
		},
		reload: function (event) {
			vm.showList = true;
			resetTotalInfo();
			$("#query_cond").attr({"disabled":"disabled"});
			$("#jqGrid").jqGrid("clearGridData");
			queryTotalInfo($("#stat_day").val());
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			 $("#jqGrid").jqGrid('setGridParam',{  
	            datatype:'json', 
	            postData:{
	            	'statPeriod': $("#stat_day").val(), 
	            	'afterInvestBalance_start': $("#start_multi_invest_money").val(), 
	            	'afterInvestBalance_end': $("#end_multi_invest_money").val(),
	            	'startFirstInvestTime': $("#start_first_invest_time").val() ,
	            	'endFirstInvestTime': $("#end_first_invest_time").val(),
	            	'startTotalMoney': $("#start_total_money").val(),
	            	'endTotalMoney': $("#end_total_money").val(),
	            	'startTotalInvestAmount': $("#start_total_invest_amount").val(),
	            	'endTotalInvestAmount': $("#end_total_invest_amount").val(),
	            	'startFirstInvestAmount': $("#start_first_invest_amount").val(),
	            	'endFirstInvestAmount': $("#end_first_invest_amount").val(),
	            	'startRegisterTime': $("#start_register_time").val(),
	            	'endRegisterTime': $("#end_register_time").val(),
	            	
	            	'bangCard': $("#if_bang_card").val(),
	            	'realName': $("#if_real_name").val(),
	            	'channelName': getChannelName().toString().length == "0" ? null : getChannelName()
	            	
	            }, //发送数据  
	            page:page 
	        }).trigger("reloadGrid"); //重新载入  
			
//			$("#jqGrid").jqGrid('setGridParam',{ 
//                page:page
//            }).trigger("reloadGrid");
		}
	}
});
//获取渠道信息
function getChannelName(){
    var arrStr = '';
    $(".select2-selection__choice").each(function(){
        arrStr += $(this).attr("title") + "^";
    });
    return arrStr;
};

//加载渠道数据
function loadChannel(){
	   var str = '';
	    var i = 0;
	    $.ajax({
	        type: "POST",
	        url: "../channel/queryChannelName",
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
	                width:'200px'
	            });
	            $("#id_select").append(str);
	        }
	     });

};
