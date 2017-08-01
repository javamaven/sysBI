$(function () {
	initDetailTableGrid();
	initCountTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initlicaiTableGrid();
	initTimeCond2();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		console.info(select);
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
			$("#licai_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			$("#licai_div").hide();
		}else if(select == 'licai'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#licai_div").show();
		}

	});
}

function initTimeCond(){
    $("#reg_begindate").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}

function initTimeCond2(){
    $("#reg_enddate").datetimepicker({
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
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../../dmreportdepsales/exportExcel', {'params' : JSON.stringify(params)});
		}else if(select == 'vip_count'){
			executePost('../../dmreportdepsales/exportExcel1', {'params' : JSON.stringify(params)});
		}else if(select == 'licai'){
			executePost('../../dmreportdepsales/exportExcel2', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
    	//url: '../../dmreportdepsales/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '产品', name: 'salesType', index: '$sales_type', width: 50 }, 			
			{ label: '15天', name: 'shiwutian', index: '$shiwutian', width: 50 }, 			
			{ label: '1个月', name: 'yiyue', index: '$yiyue', width: 50 }, 			
			{ label: '2个月', name: 'eryue', index: '$eryue', width: 50 },
			{ label: '3个月', name: 'sanyue', index: '$sanyue', width: 50, key: true },
			{ label: '4个月', name: 'siyue', index: '$siyue', width: 50 }, 	
			{ label: '5个月', name: 'wuyue', index: '$wuyue', width: 50 }, 	
			{ label: '6个月', name: 'liuyue', index: '$liuyue', width: 50 }, 			
			{ label: '8个月', name: 'bayue', index: '$bayue', width: 50 }, 			
			{ label: '9个月', name: 'jiuyue', index: '$jiuyue', width: 50 }, 			
			{ label: '10个月', name: 'shiyue', index: '$shiyue', width: 50 },
			{ label: '11个月', name: 'shiyiyue', index: '$shiyiyue', width: 50 },
			{ label: '12个月', name: 'shieryue', index: '$shieryue', width: 50 }, 			
			{ label: '15个月', name: 'shiwuyue', index: '$shiwuyue', width: 50 }, 			
			{ label: '18个月', name: 'shibayue', index: '$shibayue', width: 50 },
			{ label: '24个月', name: 'ershisiyue', index: '$ershisiyue', width: 50, key: true },
			{ label: '35个月', name: 'sanshiwuyue', index: '$sanshiwuyue', width: 50 }, 			
			{ label: '36个月', name: 'sanshiliuyue', index: '$sanshiliuyue', width: 50 }, 			
			{ label: '48个月', name: 'sishibayue', index: '$sishibayue', width: 50 }, 
			{ label: '60个月', name: 'liushiyue', index: '$liushiyue', width: 50 },
			{ label: '96个月', name: 'jiushiliuyue', index: '$jiushiliuyue', width: 50, key: true },
			{ label: '总计', name: 'zongji', index: '$zongji', width: 50 },
			{ label: '占比', name: 'zhanbi', index: '$zhanbi', width: 50, key: true },
			{ label: '周同比', name: 'weekTongRate', index: '$WeekTongRate', width: 50, key: true }
	
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
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '期限', name: 'zichan', index: '$zichan', width: 50 }, 			
			{ label: '15天', name: 'shiwutian', index: '$shiwutian', width: 50 }, 			
			{ label: '1个月', name: 'yiyue', index: '$yiyue', width: 50 }, 			
			{ label: '2个月', name: 'eryue', index: '$eryue', width: 50 },
			{ label: '3个月', name: 'sanyue', index: '$sanyue', width: 50, key: true },
			{ label: '4个月', name: 'siyue', index: '$siyue', width: 50 }, 	
			{ label: '5个月', name: 'wuyue', index: '$wuyue', width: 50 }, 	
			{ label: '6个月', name: 'liuyue', index: '$liuyue', width: 50 }, 			
			{ label: '8个月', name: 'bayue', index: '$bayue', width: 50 }, 			
			{ label: '9个月', name: 'jiuyue', index: '$jiuyue', width: 50 }, 			
			{ label: '10个月', name: 'shiyue', index: '$shiyue', width: 50 },
			{ label: '11个月', name: 'shiyiyue', index: '$shiyiyue', width: 50 }, 	
			{ label: '12个月', name: 'shieryue', index: '$shieryue', width: 50 }, 			
			{ label: '15个月', name: 'shiwuyue', index: '$shiwuyue', width: 50 }, 			
			{ label: '18个月', name: 'shibayue', index: '$shibayue', width: 50 },
			{ label: '24个月', name: 'ershisiyue', index: '$ershisiyue', width: 50, key: true },
			{ label: '35个月', name: 'sanshiwuyue', index: '$sanshiwuyue', width: 50 }, 			
			{ label: '36个月', name: 'sanshiliuyue', index: '$sanshiliuyue', width: 50 }, 			
			{ label: '48个月', name: 'sishibayue', index: '$sishibayue', width: 50 }, 
			{ label: '60个月', name: 'liushiyue', index: '$liushiyue', width: 50 },
			{ label: '96个月', name: 'jiushiliuyue', index: '$jiushiliuyue', width: 50, key: true },
			{ label: '总计', name: 'zongji', index: '$zongji', width: 50 }
	
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

function initlicaiTableGrid(){
    $("#jqGrid_licai").jqGrid({
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '类型', name: 'salesType', index: '$sales_type', width: 50 }, 			
			{ label: '金额', name: 'zhanbi', index: '$sumddd', width: 50 },
			{ label: '周同比', name: 'weekTongRate', index: '$WeekTongRate', width: 50, key: true }

        ],
 
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
        rownumbers: true, 
        autowidth:true,
        pager: "#jqGridPager_licai",
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
    $("#licai_div").hide();
    
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
					url: '../../dmreportdepsales/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../../dmreportdepsales/list2',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
			else if(select == 'licai'){
				$("#jqGrid_licai").jqGrid("clearGridData");
				$("#jqGrid_licai").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../../dmreportdepsales/list3',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'reg_begindate': $("#reg_begindate").val().replace(/-/g,""),
        	'reg_enddate': $("#reg_enddate").val().replace(/-/g,"")
	};
	return params;
}
