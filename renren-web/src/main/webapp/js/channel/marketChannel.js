$(function(){
    loadChannel();
    initExportFunction();
    $(".spinners li").removeClass("active");
	initEvent();
	initTableGridNew();
});
function queryParams(params) {  //配置参数
    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
      limit: params.pageSize,   //页面大小
      page: params.pageNumber,  //页码
      minSize: $("#leftLabel").val(),
      maxSize: $("#rightLabel").val(),
      minPrice: $("#priceleftLabel").val(),
      maxPrice: $("#pricerightLabel").val(),
      channelName : JSON.stringify(getChannelName("id_select")),
      channelName_a : JSON.stringify(getChannelName("id_select")),
      channelHead :document.getElementById("channelHead").value,
      reg_begindate: document.getElementById("reg_begindate").value.replace(/-/g,""),
      reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
    };
    return temp;
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
document.getElementById("reg_begindate").value=getDate(1);
document.getElementById("reg_enddate").value = getYesterday(1);


// 自适应高度
function tableHeight() {
	return $(window).height();
}


function getParams(){
	var params = {
        	      channelName :getChannelName("id_select"),
                  channelName_a : getChannelName("id_select"),
                  channelHead :document.getElementById("channelHead").value,
                  reg_begindate: document.getElementById("reg_begindate").value.replace(/-/g,""),
                  reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
	};
	return params;
}


//加载渠道数据
function loadChannel(){
    var str = '';
    var i = 0;
    $.ajax({
        type: "POST",
//        url: "../channel/channelAll/getChannel",
        url: "../channel/queryChannelNameByAuth",
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

function initTableGridNew(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '日期', name: 'statPeriod', index: '$CHANNELHEAD', width: 90,align:'right',sortable:false },
			{ label: '负责人', name: 'channelHead', index: '$CHANNELHEAD', width: 90,align:'right',sortable:false },
			{ label: '渠道类型', name: 'type', index: '$CHANNELNAME', width: 90 ,align:'right',sortable:false }, 		
			{ label: '渠道名称', name: 'channelName', index: '$CHANNELNAME', width: 120 ,align:'right',sortable:false }, 			
			{ label: '渠道标记', name: 'channelLabel', index: '$CHANNELLABEL', width: 120 ,align:'right',sortable:false }, 
			
			
//			{ label: '实际消费', name: 'actualCost', index: '$CHANNELLABEL', width: 70 ,align:'right',sortable:false }, 
			{ label: '注册人数', name: 'regCou', index: '$REGISTERED', width: 90 ,align:'right',sortable:false }, 		
			{ label: '首投人数', name: 'firstinvestCou', index: '$REGISTERED', width: 90 ,align:'right',sortable:false },
			{ label: '首投金额', name: 'firstinvestMoney', index: '$CGNUM', width: 90 ,align:'right',sortable:false 
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '年化首投金额', name: 'firstinvestyearamount', index: '$CZNUM', width: 100,align:'right' ,sortable:false 
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '投资人数', name: 'countUser', index: '$CZMONEY', width: 120 ,align:'right',sortable:false },		
			{ label: '投资金额', name: 'invMoney', index: '$TXMONEY', width: 120 ,align:'right',sortable:false 
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '年化投资金额', name: 'yearamount', index: '$CTMONEY', width: 120,align:'right' ,sortable:false 
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '渠道费用', name: 'channelCost', index: '$CHANNELNAME', width: 90 ,align:'right',sortable:false
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '渠道充值', name: 'channelRecharge', index: '$CHANNELNAME', width: 90 ,align:'right',sortable:false
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			}
			
			
//			{ label: '点点赚购买金额', name: 'ddzMoney', index: '$INVESTNUM', width: 90 ,align:'right',sortable:false },	
			
//			{ label: '注册成本', name: 'regCost', index: '$INVESTNUM', width: 110 ,align:'right',sortable:false },
//			{ label: '首投成本', name: 'firstinvestCost', index: '$INVESTNUM', width: 90 ,align:'right',sortable:false },
//			{ label: '人均首投', name: 'avgFirstinvestMoney', index: '$INVESTNUM', width: 90 ,align:'right',sortable:false },
			
			
//			{ label: '注册人投资转化率', name: 'regInvConversion', index: '$INVESTNUM', width: 90 ,align:'right',sortable:false },
//			{ label: '首投ROI', name: 'firstinvestRot', index: '$INVESTMONEY', width: 90 ,align:'right',sortable:false },
//			{ label: '累计ROI', name: 'cumulativeRot', index: '$INVESTMONEY', width: 110 ,align:'right',sortable:false }
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 500,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
        
//        autoScroll: false,
//        multiselect: false,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        },
        loadComplete:function(){
        	 loaded();//去掉遮罩
        }
    });
   
}
function initTableGrid(){
	//初始化Table
    $('#reportTable').bootstrapTable({
        url: "../market/list", //请求后台的URL（*）
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
//        clickToSelect: true,                //是否启用点击选中行
        height: tableHeight()+10,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
//        showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
//        cardView: false,                    //是否显示详细视图
        detailView: false ,                  //是否显示父子表
        formatNoMatches: function () {  //没有匹配的结果
            return '无符合条件的记录';
          },

        columns:  [
               {field:"statPeriod",title:"日期",align:"center",valign:"middle",sortable:"true"},//居中对齐
		       {field:"channelHead",title:"主负责人",align:"center",valign:"middle",sortable:"true"},//居中对齐
		       {field:"type",title:"渠道类型",align:"center",valign:"middle",sortable:"true"},//居中对齐
		       {field:"channelName",title:"　　渠道名称　　",align:"center",valign:"middle",sortable:"true"},//居中对齐
		       {field:"channelLabel",title:"渠道标签",align:"center",valign:"middle",sortable:"true"},//居中对齐
		       {field:"actualCost",title:"实际消费",align:"center",valign:"middle",sortable:"true"},
		       {field:"regCou",title:"新增注册人",align:"center",valign:"middle",sortable:"true"},
		       {field:"firstinvestCou",title:"新增首投人数",align:"center",valign:"middle",sortable:"true"},
		       {field:"firstinvestMoney",title:"首投金额",align:"center",valign:"middle",sortable:"true"},
		       {field:"firstinvestYMoney",title:"首投年化金额",align:"center",valign:"middle",sortable:"true"},
		       {field:"invCou",title:"投资总人数",align:"center",valign:"middle",sortable:"true"},
		       {field:"invMoney",title:"投资总金额",align:"center",valign:"middle",sortable:"true"},
		       {field:"invYMoney",title:"年化投资总金额",align:"center",valign:"middle",sortable:"true"},
		       {field:"ddzMoney",title:"点点赚购买金额",align:"center",valign:"middle",sortable:"true"},
		       {field:"regCost",title:"注册成本",align:"center",valign:"middle",sortable:"true"},
		       {field:"firstinvestCost",title:"首投成本",align:"center",valign:"middle",sortable:"true"},
		       {field:"avgFirstinvestMoney",title:"人均首投",align:"center",valign:"middle",sortable:"true"},
		       {field:"regInvConversion",title:"注册人投资转化率",align:"center",valign:"middle",sortable:"true"},
		       {field:"firstinvestRot",title:"首投ROI",align:"center",valign:"middle",sortable:"true"},
		       {field:"cumulativeRot",title:"累计ROI",align:"center",valign:"middle",sortable:"true"}
        ]

    });
}

function getQueryParams(){

	return {
	        page  :1,
	        limit :20,
	         channelName : JSON.stringify(getChannelName("id_select")),
             channelName_a : JSON.stringify(getChannelName("id_select")),
             channelHead :document.getElementById("channelHead").value,
             reg_begindate: document.getElementById("reg_begindate").value.replace(/-/g,""),
             reg_enddate:document.getElementById("reg_enddate").value.replace(/-/g,"")
	    };
}

function print(obj){
	for(var key in obj){
		alert(key + " = " + obj[key])
	}
}



$("#searchButton").click(function(){
	console.info("+++++++++++++++++++++++++")
	console.info(JSON.stringify(getQueryParams()))
	$("#jqGrid").jqGrid("clearGridData");
	$("#jqGrid").jqGrid('setGridParam',{ 
		datatype:'json', 
		url: '../market/list',
        postData: getQueryParams()
    }).trigger("reloadGrid");
    // 显示之前，先把当前表格销毁
//      $('#reportTable').bootstrapTable('destroy');
    //加载数据
//    initTableGrid();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../market/partExport', {'params' : JSON.stringify(params)});
	});

}