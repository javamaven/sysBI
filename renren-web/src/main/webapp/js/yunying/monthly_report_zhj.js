$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initCountTableGrid();
	initEvent();
	initSelectEvent();
	initTimeCond1();
});



function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'vip_detail'){
			$("#vip_detail_div").show();
			$("#vip_count_div").hide();
		}else if(select == 'vip_count'){
			$("#vip_detail_div").hide();
			$("#vip_count_div").show();
		}
	});
}

function initTimeCond(){
    $("#statPeriod").datetimepicker({
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
		
		var select = $("#list_select").children('option:selected').val();
		if(select == 'vip_detail'){
			executePost('../yunying/zhjp2p/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/zhjp2p/exportExcel', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '指标编码', name: 'BIANMA', index: '$ITEMSCODE', width: 90,align:'right' },
			{ label: '指标名称', name: 'ZHIBIAO', index: '$TYPENAME', width: 90 ,align:'right'}, 			
			{ label: '频度', name: 'PERIOD', index: '$MONTH', width: 90 ,align:'right'}, 
			{ label: '金额(数量)', name: 'DATA', index: '$MONEY', width: 90 ,align:'right'}	

				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 100,
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
			{ label: '指标编码', name: 'BIANMA', index: '$ITEMSCODE', width: 90,align:'right' },
			{ label: '指标名称', name: 'ZHIBIAO', index: '$TYPENAME', width: 90 ,align:'right'}, 			
			{ label: '频度', name: 'PERIOD', index: '$MONTH', width: 90 ,align:'right'}, 
			{ label: '金额(占比)', name: 'DATA', index: '$MONEY', width: 90 ,align:'right'}	

				
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
					url: '../yunying/zhjp2p/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/zhjp2p/ddylist',
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
