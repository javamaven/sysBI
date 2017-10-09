$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
	initCountTableGrid();
	initCunGuanTableGrid();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
			$("#cunguan_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
			$("#cunguan_div").hide();
		}
		else if(select == 'cunguan'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").hide();
			$("#cunguan_div").show();
		}
	});
}

function initTimeCond(){
    $("#statPeriod").datetimepicker({
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
			executePost('../yunying/hongbao/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/hongbao/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'cunguan'){
			executePost('../yunying/hongbao/exportExcel', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '红包名称', name: 'NAME', index: '$TYPE', width: 90,align:'right' },
			{ label: '券号NID/红包模板ID', name: 'ID', index: '$ID', width: 90 ,align:'right'}, 			
			{ label: '发放人数', name: 'FAFANG', index: '$FAFANG', width: 90 ,align:'right'}, 
			{ label: '使用人数', name: 'SHIYONG', index: '$SHIYONG', width: 90 ,align:'right'}, 			
			{ label: '使用总金额(元)', name: 'ZMONEY', index: '$ZMONEY', width: 90 ,align:'right'}, 	
			{ label: '用户首投时使用金额(元)', name: 'FIRSTMONEY', index: '$FIRSTMONEY', width: 90,align:'right' },
			{ label: '红包所属系统', name: 'RED', index: '$RED', width: 90 ,align:'right'},		
			{ label: '数据统计周期', name: 'TIME', index: '$TIME', width: 90 ,align:'right'},
			{ label: '用途', name: 'YONGTU', index: '$YONGTU', width: 90,align:'right' },
			{ label: '所属于部门', name: 'BUMEN', index: '$BUMEN', width: 90 ,align:'right'},		
			{ label: '成本分摊方式', name: 'CHENGBEN', index: '$CHENGBEN', width: 90 ,align:'right'},
			{ label: '市场部费用(元)', name: 'SHICHANGFEIYONG', index: '$SHICHANGFEIYONG', width: 90 ,align:'right'},
			{ label: '运营部费用(元)', name: 'YUNYINGFEIYONG', index: '$YUNYINGFEIYONG', width: 90 ,align:'right'}
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 1000,
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
			{ label: '一级科目', name: 'HONGBAO', index: '$HONGBAO', width: 100,align:'right' },
			{ label: '二级', name: 'YUNYING', index: '$YUNYING', width: 80 ,align:'right'}, 			
			{ label: '归属系统', name: 'CUNGUAN', index: '$CUNGUAN', width: 100,align:'right' }, 			
			{ label: '统计周期', name: 'TIME', index: '$TIME', width: 100,align:'right' }, 			
			{ label: '成本', name: 'YUNYINGFEIYONG', index: '$YUNYINGFEIYONG', width: 100 ,align:'right'}
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
function initCunGuanTableGrid(){
    $("#jqGrid_cunguan").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '渠道名称', name: 'NAME', index: '$TYPE', width: 90,align:'right' },
			{ label: '券号NID/红包模板ID', name: 'ID', index: '$ID', width: 90 ,align:'right'}, 			
			{ label: '发放人数', name: 'FAFANG', index: '$FAFANG', width: 90 ,align:'right'}, 
			{ label: '使用人数', name: 'SHIYONG', index: '$SHIYONG', width: 90 ,align:'right'}, 			
			{ label: '使用总金额(元)', name: 'ZMONEY', index: '$ZMONEY', width: 90 ,align:'right'}, 	
			{ label: '用户前3次投资使用金额(元)', name: 'FIRSTMONEY', index: '$FIRSTMONEY', width: 90,align:'right' },
			{ label: '红包所属系统', name: 'RED', index: '$RED', width: 90 ,align:'right'},		
			{ label: '数据统计周期', name: 'TIME', index: '$TIME', width: 90 ,align:'right'},
			{ label: '用途', name: 'YONGTU', index: '$YONGTU', width: 90,align:'right' },
			{ label: '所属于部门', name: 'BUMEN', index: '$BUMEN', width: 90 ,align:'right'},		
			{ label: '成本分摊方式', name: 'CHENGBEN', index: '$CHENGBEN', width: 90 ,align:'right'},
			{ label: '市场部费用(元)', name: 'SHICHANGFEIYONG', index: '$SHICHANGFEIYONG', width: 90 ,align:'right'},
			{ label: '运营部费用(元)', name: 'YUNYINGFEIYONG', index: '$YUNYINGFEIYONG', width: 90 ,align:'right'}
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 1000,
        rownumbers: true, 
        autowidth:true,
//        shrinkToFit: false,
//        autoScroll: false,
//        multiselect: false,
        pager: "#jqGridPager_cunguan",
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
    $("#cunguan_div").hide();
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
					url: '../yunying/hongbao/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/hongbao/ddylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'cunguan'){
				$("#jqGrid_cunguan").jqGrid("clearGridData");
				$("#jqGrid_cunguan").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/hongbao/ddylist2',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#statPeriod").val()
	};
	return params;
}
