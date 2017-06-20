$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
});

function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../yunying/dmreportactivechannelcost/exportExcel', {'params' : JSON.stringify(params)});  
	});

}

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportactivechannelcost/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 50,align:'right' },
			{ label: '渠道标签', name: 'code', index: '$CODE', width: 80,align:'right' }, 			
			{ label: '渠道名称', name: 'name', index: '$NAME', width: 80 ,align:'right'}, 			
			{ label: '推广成本', name: 'cost', index: '$COST', width: 80 ,align:'right'}, 			
			{ label: '成本归属部门', name: 'costSource', index: '$COST_SOURCE', width: 80 ,align:'right'}			
        ],
		viewrecords: true,
        height: $(window).height()-130,
        rowNum: 20,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
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
		dmReportDdzRemain: {}
	},
	methods: {
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../yunying/dmreportactivechannelcost/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}