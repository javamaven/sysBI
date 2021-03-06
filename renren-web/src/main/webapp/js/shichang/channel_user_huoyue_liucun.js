$(function () {
	initDataGrid();
	initExportFunction();
	initTime();
});

function initTime(){
    $("#month").datetimepicker({
        format: 'yyyy-mm',
        weekStart: 1,  
        autoclose: true,  
        startView: 3,  
        minView: 3,  
        forceParse: false,  
        language: 'zh-CN'  
    }).on("click",function(){
    }); 
}

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		if(!params.month){
			alert('日期不能为空');
			return;
		}
		executePost('../channel/st/exportUserHuoyueLiucunExcel', {'params' : JSON.stringify(params)});
	});

}

function initDataGrid(){
    $("#jqGrid").jqGrid({
        datatype: "json",
        colModel: [			
		    { label: '渠道名称', name: '渠道名称', width: 130, key: true,align: 'right' },
		    { label: '渠道标记', name: '渠道标记', width: 130, key: true,align: 'right' },
		    { label: '新增首投用户', name: '新增首投用户', width: 130, key: true,align: 'right' },
		    { label: '首投用户30天内人均投资次数', name: '首投用户30天内人均投资次数', width: 130, key: true,align: 'right'},
		    { label: '30天后', name: '30天后', width: 80, key: true,align: 'right'},
		    { label: '60天后', name: '60天后', width: 80, key: true,align: 'right'},
		    { label: '90天后', name: '90天后', width: 80, key: true,align: 'right'},
        	{ label: '120天后', name: '120天后', width: 80, key: true,align: 'right'},
        	
        	{ label: '150天后', name: '150天后', width: 80, key: true,align: 'right'},
        	{ label: '180天后', name: '180天后', width: 80, key: true,align: 'right'},
        	{ label: '270天后', name: '270天后', width: 80, key: true,align: 'right'},
        	{ label: '360天后', name: '360天后', width: 80, key: true,align: 'right'}
        ],
        height:  $(window).height()-150,
        rowNum: 1000,
//		rowList : [10,30,50],
//        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
		viewrecords: true,
		rowList : [10,30,50],
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
        
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDalilyMarketing: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		reload: function (event) {
			vm.showList = true;
			var params = getParams();
			if(!params.month){
				alert('日期不能为空');
				return;
			}
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../channel/st/channelUserHuoyueLiuCunList',
	            postData: params
            }).trigger("reloadGrid");
		}
	}
});

//获取渠道信息
function getChannelName(){
    var arrStr = new Array();
    $(".select2-selection__choice").each(function(){
        arrStr.push($(this).attr("title"))
        });
    return  arrStr;
};

function getParams(){
	var params = {};
	params.month = $("#month").val();
	return params;
}