$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
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
    $("#exit_end_time").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}
function initTimeCond1(){
    $("#exit_stat_time").datetimepicker({
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
			executePost('../yunying/exit/exportExcel', {'params' : JSON.stringify(params)});
		}
		else if(select == 'vip_count'){
			executePost('../yunying/p2p/exportExcel2', {'params' : JSON.stringify(params)});
		}
	});

}

function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
			{ label: '退出时间', name: 'TIME', index: '$TYPE', width: 90,align:'right' },
			{ label: '退出', name: 'EXIT', index: '$NUM', width: 90 ,align:'right'}, 			
			{ label: '提现', name: 'TIXIAN', index: '$SUM', width: 90 ,align:'right'}, 
			{ label: '充值', name: 'CHONGZHI', index: '$BORROW_USER', width: 90 ,align:'right'}, 			
			{ label: '迁移', name: 'QIANYI', index: '$BORROW_CAPITAL', width: 90 ,align:'right'}, 	
			{ label: '复投', name: 'FUTOU', index: '$NUMM', width: 90,align:'right' },
			{ label: '复投使用红包', name: 'HONGBAO', index: '$SUMM', width: 90 ,align:'right'},		
			{ label: '复投率', name: 'FUTOULV', index: '$AVGG', width: 90 ,align:'right'},
			{ label: '复投理财计划', name: 'LICAI', index: '$NUMS', width: 90,align:'right' }
				
        ],
		viewrecords: true,
        height: $(window).height()-170,
        rowNum: 200,
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
					url: '../yunying/exit/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(select == 'vip_count'){
				$("#jqGrid_count").jqGrid("clearGridData");
				$("#jqGrid_count").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/p2p/ddylist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		}
	}
});

function getParams(){
	var params = {
        	'exit_end_time': $("#exit_end_time").val(),
        	'exit_stat_time': $("#exit_stat_time").val()
	};
	return params;
}
