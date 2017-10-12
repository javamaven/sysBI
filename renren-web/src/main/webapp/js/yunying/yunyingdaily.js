$(function () {
//	initDetailTableGrid();
	initExportFunction();
	initEvent();
	initTimeCond();
//	initSelectEvent();
	initDataGrid();
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
function initTimeCond(){
    $("#beginTime").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#beginTime").val(addDate(getCurrDate(), -31));
    $("#endTime").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#endTime").val(addDate(getCurrDate(), -1));
}



function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
			executePost('../yunying/yunyingdaily/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}


function initDataGrid(){
    $('#tt').datagrid({
//        url:'../yunyingtool/list2?user_type=all_user',
//        url: '../yunying/source/list?period=' + getParams().period ,
//        idField:'id',
        loadMsg : '数据正在加载,请耐心的等待...',
//        treeField:'HUIZONG',
        frozenColumns:[[
        	{field:'STAT_PERIOD',title:'日期',width:80,align:'right',halign :'left'}
    	]],
        columns:[[
//          {field:'id',title:'id',width:180},
//        {field:'STAT_PERIOD',title:'日期',width:80,align:'right',halign :'left'},
        {field:'ADD_INV',title:'净增投资',width:80,align:'right',halign :'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        {field:'INV',title:'投资金额',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'SAN_INV',title:'散标投资',width:70,align:'right',halign :'left'},
        {field:'TRAN_INV',title:'债转投资',width:70,align:'right',halign :'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },	

        {field:'FIN_INV',title:'理财计划投资',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },
        
        {field:'PROJECT_SCALE',title:'点点赚余额',width:80 , align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	}
        	},
        
        {field:'NUM',title:'投资人数',width:60,align:'right',halign :'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },
        
        {field:'INV_NUM',title:'投资次数',width:60,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },

        {field:'RE_AMOUNT',title:'充值金额',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'WI_AMOUNT',title:'提现金额',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'CT',title:'净充值',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'SAN_REPAY_CAPITAL',title:'散标回款本金',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'SAN_REPAY',title:'散标回款',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'EXITS',title:'理财计划退出本金',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'VOUCHE',title:'红包成本',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'INTEREST_EXTRA',title:'加息成本',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'REBATE_MONEY',title:'返利成本',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'AWAIT_NUM',title:'有待收人数',width:70,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'OLD_AWAIT',title:'旧版待收本金',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'AWAIT',title:'待收本金',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'REPAY_ACCOUNT_WAIT',title:'待收金额',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'BORROW_ACCOUNT',title:'底层资产成交',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'YEAR_BORROW_ACCOUNT',title:'底层资产年化',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'M_REPAY_ACCOUNT_WAIT',title:'本月新增年后还款待收',width:100,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'Y_REPAY_ACCOUNT_WAIT',title:'年后还款待收',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'SCHEDULE_MONEY',title:'可排期项目',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'NO_MATCH_CAPITIL_WAIT',title:'理财计划预约资金',width:90,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    }
        
        ]],
        onLoadSuccess: function(row, data){
//            $(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
//            $(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed"); 
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
			console.info("aaaaa")
			vm.showList = true;
		    var options = $('#tt').datagrid('options');
		    options.url = '../yunying/yunyingdaily/list?beginTime=' + getParams().beginTime +'&&'+'endTime='+getParams().endTime;
		    $('#tt').datagrid('reload');
	   }
		
	}
});

function getParams(){
	var params = {
			'beginTime': $("#beginTime").val(),
			'endTime': $("#endTime").val()
	};
	return params;
}
