$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initCountTableGrid();
	initSelectEvent();
	initTimeCond1();
	initHuikuanTableGrid();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
			$("#huikuan_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			$("#huikuan_div").hide();
		}
		else if(select == 'huikuan'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#huikuan_div").show();
		}
	});
}

function initTimeCond(){
    $("#invest_end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}
function initTimeCond1(){
    $("#invest_month_time").datetimepicker({
        format: 'yyyy-mm',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../yunying/yyp2p/exportExcel', {'params' : JSON.stringify(params)});
		}
		
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '日期', name: 'DAYS', index: '$DAYS', width: 90,align:'right' },
			{ label: '待收金额（万元）', name: 'WAIT', index: '$WAIT', width: 90 ,align:'right'}		
		
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
//        shrinkToFit: false,
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
        }
    });
}
function initCountTableGrid(){
    $("#jqGrid_count").jqGrid({
        datatype: "json",
        colModel: [			
			{ label: '月份', name: 'MONTH', index: '$MONTH', width: 80 ,align:'right'}, 			
			{ label: '项目年华投资金额（万元）', name: 'TENDER', index: '$TENDER', width: 100,align:'right' }			
 			
						
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
        pager: "#jqGridPager_count",
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
        }
    });
    $("#vip_count_div").hide();
}

function initHuikuanTableGrid(){
    $("#jqGrid_huikuan").jqGrid({
        datatype: "json",
        colModel: [			
			{ label: '月份', name: 'MONTH', index: '$MONTH', width: 80 ,align:'right'}, 			
			{ label: '回款金额（万元）', name: 'MONEY', index: '$MONEY', width: 100,align:'right' }			
 			
						
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
        pager: "#jqGridPager_huikuan",
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
        }
    });
    $("#huikuan_div").hide();
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
			var select = $("#list_select").children('option:selected').val();
			if(select == 'vip_detail'){
				$("#jqGrid").jqGrid("clearGridData");
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/yyp2p/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/yyp2p/ddylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'huikuan'){
				$("#jqGrid_huikuan").jqGrid("clearGridData");
				$("#jqGrid_huikuan").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/yyp2p/huikuanlist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'invest_end_time': $("#invest_end_time").val(),
        	'invest_month_time': $("#invest_month_time").val()
	};
	return params;
}
