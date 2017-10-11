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
    $("#period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
    $("#period").val(addDate(getCurrDate(), -1));
}



function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		
			executePost('../yunying/source/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}


function initDataGrid(){
    $('#tt').treegrid({
//        url:'../yunyingtool/list2?user_type=all_user',
//        url: '../yunying/source/list?period=' + getParams().period ,
        idField:'id',
        loadMsg : '数据正在加载,请耐心的等待...',
        treeField:'HUIZONG',
        columns:[[
//          {field:'id',title:'id',width:180},
        {field:'HUIZONG',title:'用戶來源',width:80,align:'left'},
        {field:'ALL_REG',title:'注册人数',width:70,align:'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        {field:'M_REG',title:'当月注册',width:60,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'D_REG',title:'当日注册',width:60,align:'left'},
        {field:'ALL_FIRST',title:'首投人数',width:60,align:'left',
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },	

        {field:'M_FIRST',title:'当月首投人数',width:80,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'D_FIRST',title:'当日首投人数',width:80 , align:'left'},
        {field:'M_FIRST_INV',title:'当月首投金额',width:90,
        	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'D_FIRST_INV',title:'当日首投金额',width:90,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },

        {field:'WEIZHI4',title:'当月充值',width:90,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'WEIZHI3',title:'当月净充值',width:100,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'WEIZHI2',title:'当日充值',width:100,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'WEIZHI1',title:'当日净充值',width:100,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'M_INV',title:'当月投资',width:100,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        {field:'D_INV',title:'当日投资',width:100,align:'left',
	    	formatter:function(cellvalue, options, rowObject){
	    		if(cellvalue){
	    			return formatNumber(cellvalue,2);
	    		}else{
	    			return '';
	    		}
	    	} 
	    },
        
        
        {field:'AWIAT',title:'待收本金',width:110,align:'left',
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
		    var options = $('#tt').treegrid('options');
		    options.url = '../yunying/source/list?period=' + getParams().period ;
		    $('#tt').treegrid('reload');
	   }
		
	}
});

function getParams(){
	var params = {
			'period': $("#period").val()
	};
	return params;
}
