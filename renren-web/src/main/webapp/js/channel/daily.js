$(function () {
  initDetailTableGrid();
  initTimeCond();
  initExportFunction();
  initEvent();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../channel/daily/partExport', {'params' : JSON.stringify(params)});  
	});

}


function initTimeCond(){
    $("#begin_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#begin_time").val(addDate(getCurrDate(), -1));
    
    
    
    $("#end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#end_time").val(addDate(getCurrDate(), -1));
    
}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$INDICATORS_NAME', width: 50, key: true },
			{ label: '指标名字', name: 'indicatorsName', index: '$INDICATORS_VALUE', width: 80 }, 			
			{ label: '指标值', name: 'indicatorsValue', index: '$SEQUENTIAL', width: 80 }, 			
			{ label: '环比', name: 'sequential', index: '$COMPARED', width: 80 }, 			
			{ label: '同比', name: 'compared', index: '$MONTH_MEAN_VALUE', width: 80 }, 			
			{ label: '30天均值', name: 'monthMeanValue', index: '$MONTH_MEAN_VALUE_THAN', width: 80 }, 			
			{ label: '30天均值比', name: 'monthMeanValueThan', index: '$STAT_PERIOD', width: 80 }			
        ],
		viewrecords: true,
        height: $(window).height()-130,
        rowNum: 500,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: true,
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportPotVip: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		

		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../channel/daily/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
			'begin_time': $("#begin_time").val().replace(/-/g,""),
			'end_time': $("#end_time").val().replace(/-/g,"")
	};
	return params;
}