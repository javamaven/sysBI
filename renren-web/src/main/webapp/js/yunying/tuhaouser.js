$(function () {
	initDetailTableGrid();
	initExportFunction();
	initEvent();
//	initSelectEvent();
});


//function initSelectEvent(){
//	//日报，月报切换
//	$("#list_select").change(function(){
//		var select = $(this).children('option:selected').val();
//		if(select == 'vip_detail'){
//			$("#vip_detail_div").show();
//			$("#vip_count_div").hide();
//		}else if(select == 'nianlin'){
//			$("#vip_detail_div").hide();
//			$("#vip_count_div").show();
//		}
//	});
//}


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
		var select = $("#list_select").children('option:selected').val();
			executePost('../yunying/tuhao/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}
function initDetailTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../yunying/dmreportvipuser/list',
        datatype: "json",
        colModel: [
        	{ label: '用户id', name: 'USER_ID', index: '$TYPE', width: 80,align:'left' },
			{ label: '旧版用户id', name: 'OLD_USER_ID', index: '$NUM', width: 80 ,align:'right'}, 			
			{ label: '存管id', name: 'CG_USER_ID', index: '$SUM', width: 80 ,align:'right'}, 
			
			{ label: '用户手机号', name: 'PHONE', index: '$BORROW_USER', width: 100 ,align:'right'}, 			
			{ label: '用户名', name: 'USERNAME', index: '$BORROW_CAPITAL', width: 80 ,align:'right'}, 
			
			{ label: '用户真实姓名', name: 'REALNAME', index: '$BORROW_USER', width: 100,align:'right'}, 
			{ label: '是否员工', name: 'IS_STALL', index: '$BORROW_CAPITAL', width: 80 ,align:'right'} ,
			
			{ label: '性别', name: 'SEX', index: '$TYPE', width: 50,align:'left' },
			{ label: '注册时间', name: 'REGISTER_TIME', index: '$NUM', width: 150 ,align:'right'}, 			
			{ label: '投资总次数', name: 'TEND_TIMES', index: '$SUM', width: 100 ,align:'right'}, 
			
			{ label: '最近一次投资离现在天数', name: 'DIF_DATE', index: '$BORROW_USER', width: 180 ,align:'right'}, 			
			{ label: '总投资金额', name: 'SUM_CAPITAL', index: '$BORROW_CAPITAL', width: 120 ,align:'right'}, 
			
			{ label: '总使用红包金额', name: 'SUM_VOUCHE', index: '$BORROW_USER', width: 120,align:'right'}, 			
			{ label: '是否高净值', name: 'IS_VIP', index: '$BORROW_CAPITAL', width: 100 ,align:'right'} ,
			
			{ label: '用户年龄', name: 'AGE', index: '$TYPE', width: 80,align:'left' },
			{ label: '近180天内到期待收比例', name: 'ID_R_180', index: '$NUM', width: 180 ,align:'right'}, 			
			{ label: '近30天内到期待收比例', name: 'ID_R_30', index: '$SUM', width: 150 ,align:'right'}, 
			
			{ label: '近30天内到期待收金额', name: 'R_30', index: '$BORROW_USER', width: 180 ,align:'right'}, 			
			{ label: '是否容易流失', name: 'IS_YLS', index: '$BORROW_CAPITAL', width: 100 ,align:'right'}, 
			
			{ label: '是否为新数据', name: 'IS_NEW', index: '$BORROW_USER', width: 100,align:'right'}, 			
			{ label: '提数日期最新待收', name: 'AW', index: '$BORROW_CAPITAL', width: 150 ,align:'right'} ,
			
			
			{ label: '提数日期前一天申请提现金额（成功申请）', name: 'AMOUNT1', index: '$TYPE', width: 280,align:'left' },
			{ label: '平均申请提现金额/天（不含提取数据前1天）', name: 'AMOUNT2', index: '$NUM', width: 300 ,align:'right'}, 			
			{ label: '待收种类', name: 'PPC', index: '$SUM', width: 100 ,align:'right'}, 
			
			{ label: '当前待收', name: 'DS', index: '$BORROW_USER', width: 150 ,align:'right'} 			
			
		
	
        ],
		viewrecords: true,
        height: $(window).height()-150,
        rowNum: 100,
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: false,
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




//var vm = new Vue({
//	el:'#rrapp',
//	data:{
//		showList: true,
//		title: null,
//		dmReportDdzRemain: {}
//	},
//	methods: {
//		reload: function (event) {
//			vm.showList = true;
//			var select = $("#list_select").children('option:selected').val();
//			if(select == 'vip_detail'){
//				$("#jqGrid").jqGrid("clearGridData");
//				$("#jqGrid").jqGrid('setGridParam',{ 
//					datatype:'json', 
//					url: '../yunying/daishouqujian/list',
//		            postData: getParams()
//	            }).trigger("reloadGrid");
//			}else if(select == 'nianlin'){
//				$("#jqGrid_count").jqGrid("clearGridData");
//				$("#jqGrid_count").jqGrid('setGridParam',{ 
//					datatype:'json', 
//					url: '../yunying/daishouqujian/ddylist',
//		            postData: getParams()
//	            }).trigger("reloadGrid");
//			}
//		}
//	}
//});


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
				$("#jqGrid").jqGrid("clearGridData");
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json', 
					url: '../yunying/tuhao/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
		
	}
});

function getParams(){
	var params = {
        	
	};
	return params;
}
