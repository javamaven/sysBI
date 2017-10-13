$(function () {
//	initDetailTableGrid();
	initExportFunction();
	initEvent();
	initTimeCond();
//	initSelectEvent();
	initDataGrid();
});



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
		
			executePost('../yunying/licaijihuadaily/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}


function initDataGrid(){
    $('#tt').datagrid({
//        idField:'id',
        loadMsg : '数据正在加载,请耐心的等待...',
//        treeField:'HUIZONG',
        frozenColumns:[[
        	{field:'STAT_PERIOD',title:'日期',width:80,align:'right',halign :'left'}
    	]],
        columns:[[
//          {field:'id',title:'id',width:180},
//        {field:'STAT_PERIOD',title:'日期',width:80,align:'right',halign :'left'},
        {field:'FIN_AWAIT',title:'理财计划待收',width:80,align:'right',halign :'left',
        	
	    },
        {field:'FIN_AWAIT_NUM',title:'有待收人数',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'FIN_HUOQI_AWAIT',title:'活期待收总量',width:90,align:'right',halign :'left'},
        {field:'AWAIT_RATE',title:'活期待收占比',width:90,align:'right',halign :'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },	

        {field:'FIN_HUOQI_AWAIT_NUM',title:'有活期待收人数',width:100,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },
        
        {field:'FIN_INV',title:'理财计划投资',width:90 , align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	}
        	},
        
        {field:'UNLOCKS',title:'解锁金额',width:60,align:'right',halign :'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },
        
        {field:'APPLY_EXIT',title:'申请退出金额',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '0';
	    		}
	    	} 
	    },

        {field:'APPLY_EXIT_NUM',title:'申请退出人数',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'EXITS',title:'成功退出本金',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'EXITS_ALL',title:'成功退出本息',width:80,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'EXIT_RATE',title:'当日退出率',width:80,align:'right',halign :'left',
	 
	    },
	    {field:'FIN_REPAY',title:'理财计划底层回款本息',width:110,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'NO_MATCH_CAPITIL_WAIT',title:'理财计划预约资金',width:100,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
	    {field:'FIN_APR',title:'理财计划加权利率',width:100,align:'right',halign :'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
  
	    {field:'AVG_APR',title:'理财计划底层资产加权利率',width:150,align:'right',halign :'left',
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
            $(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
            $(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed"); 
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
		    options.url = '../yunying/licaijihuadaily/list?beginTime=' + getParams().beginTime +'&&'+'endTime='+getParams().endTime;
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
