$(function () {
	initDetailTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
	initSelectEvent();
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
    $("#invest_end_time").datetimepicker({
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
			executePost('../yunying/yxp2p/exportExcel', {'params' : JSON.stringify(params)});
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
			{ label: '分类', name: 'TYPE', index: '$TYPE', width: 90,align:'right' },
			{ label: '人数(穿透)', name: 'NUM', index: '$NUM', width: 90 ,align:'right'}, 			
			{ label: '借款余额(穿透)', name: 'SUM', index: '$SUM', width: 90 ,align:'right'}, 
			{ label: '人数(非穿透)', name: 'BORROW_USER', index: '$BORROW_USER', width: 90 ,align:'right'}, 			
			{ label: '借款余额(非穿透)', name: 'BORROW_CAPITAL', index: '$BORROW_CAPITAL', width: 90 ,align:'right'}, 	
			{ label: '人数(穿+非)', name: 'NUMM', index: '$NUMM', width: 90,align:'right' },
			{ label: '借款余额(总)', name: 'SUMM', index: '$SUMM', width: 90 ,align:'right'},		
			{ label: '人均借款余额(万)', name: 'AVGG', index: '$AVGG', width: 90 ,align:'right'},
			{ label: '出借人数(总)', name: 'NUMS', index: '$NUMS', width: 90,align:'right' },
			{ label: '平均借款期限(天)', name: 'AVGS', index: '$AVGS', width: 90 ,align:'right'},		
			{ label: '平均借款利率(万)', name: 'AVGLI', index: '$AVGLI', width: 90 ,align:'right'},
			{ label: '逾期', name: 'YUQI', index: '$YUQI', width: 90 ,align:'right'}
				
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
					url: '../yunying/yxp2p/list',
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
        	'invest_end_time': $("#invest_end_time").val()
	};
	return params;
}
