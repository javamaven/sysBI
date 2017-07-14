$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initTimeCond1();
	initCountTableGrid();
	initDaiShouTableGrid();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
			$("#vip_daishou_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			$("#vip_daishou_div").hide();
		}else if(select == 'daishou'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#vip_daishou_div").show();
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
    $("#invest_stat_time").datetimepicker({
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
			executePost('../yunying/zbp2p/exportExcel', {'params' : JSON.stringify(params)});
		}else if(select == 'vip_count'){
			executePost('../yunying/zbp2p/exportExcel', {'params' : JSON.stringify(params)});
		}else if(select == 'daishou'){
			executePost('../yunying/zbp2p/exportExcel', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '日期', name: 'NUM1', index: '$NUM1', width: 90,align:'right' },
			{ label: '指标名称', name: 'NUM2', index: '$NUM2', width: 90 ,align:'right'}, 			
			{ label: '指标名称2', name: 'XIXI', index: '$XIXI', width: 90 ,align:'right'}
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 100,
//        rownumbers: true, 
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
			{ label: '日期', name: 'STATPERIOD', index: '$STAT_PERIOD', width: 100,align:'right' },
			{ label: '本周注册人数', name: 'BZZHUCE', index: '$OWNER', width: 80 ,align:'right'}, 			
			{ label: '本周认证人数', name: 'BZRENZHENG', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '本周首投人数', name: 'BZSHOUTOU', index: '$MONTH_TENDER', width: 100,align:'right' }, 			
			{ label: '上周注册人数', name: 'SZZHUCE', index: '$MONTH_TENDER_Y', width: 100 ,align:'right'}, 			
			{ label: '上周认证人数', name: 'SZRENZHENG', index: '$MONTH_TENDER_COU', width: 100 ,align:'right'}, 
			{ label: '上周首投人数', name: 'SZSHOUTOU', index: '$AVG_PERIOD', width: 100,align:'right' }, 			
			{ label: '总注册人数', name: 'ZZHUCE', index: '$MONTH_TENDER', width: 100,align:'right' }, 			
			{ label: '总首投人数', name: 'ZSHOUTOU', index: '$MONTH_TENDER_Y', width: 100 ,align:'right'}, 
			{ label: '本月注册人数', name: 'BYZHUCE', index: '$MONTH_TENDER_Y', width: 100 ,align:'right'},
			{ label: '本月首投人数', name: 'BYSHOUTOU', index: '$MONTH_TENDER_COU', width: 100 ,align:'right'}
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
//        rownumbers: true, 
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

function initDaiShouTableGrid(){
    $("#jqGrid_daishou").jqGrid({
        datatype: "json",
        colModel: [			
        	{ label: '日期', name: 'STATPERIOD', index: '$STAT_PERIOD', width: 100,align:'right' },
			{ label: '当前待收金额', name: 'DAISHOU', index: '$MONTH_TENDER_COU', width: 100 ,align:'right'}
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 20,
//        rownumbers: true, 
        autowidth:true,
        pager: "#jqGridPager_daishou",
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
    $("#vip_daishou_div").hide();
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
					url: '../yunying/zbp2p/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/zbp2p/ddylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'daishou'){
				$("#jqGrid_daishou").jqGrid("clearGridData");
				$("#jqGrid_daishou").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/zbp2p/daishoulist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'invest_end_time': $("#invest_end_time").val(),
        	'invest_stat_time': $("#invest_stat_time").val()
	};
	return params;
}
