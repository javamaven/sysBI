$(function () {
	initExportFunction();
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
    $("#beginTime").val(addDate(getCurrDate(), -8));
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
		
			executePost('../yunying/yemiantongji/exportExcel', {'params' : JSON.stringify(params)});
		
	});

}


function initDataGrid(){
    $('#tt').datagrid({
//        idField:'id',
    	pageNumber :1,
    	pageSize: 100,
        loadMsg : '数据正在加载,请耐心的等待...',
//        queryParams:{'pageUrls':111},
//        treeField:'HUIZONG',
        method: 'POST',
//        frozenColumns:[[
//        	{field:'STAT_PERIOD',title:'日期',width:80,align:'right',halign :'left'}
//    	]],
        columns:[[
//          {field:'id',title:'id',width:180},
        {field:'TIME_DAY',title:'日期',width:80,align:'center',halign :'center'},
        {field:'PV',title:'PV',width:80,align:'center',halign :'center',
        	
	    },
        {field:'UV',title:'UV',width:80,align:'center',halign :'center'}
        ]],
//        pagination:true,
        onLoadSuccess: function(row, data){
            $(".tree-icon,.tree-file").removeClass("tree-icon tree-file");
            $(".tree-icon,.tree-folder").removeClass("tree-icon tree-folder tree-folder-open tree-folder-closed"); 
            loaded();
            
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
	    	loading();
			vm.showList = true;	 
		    ajaxQuery();

	   }
		
	}
});

function ajaxQuery(){
	var queryParams = {};
//	beginTime=' + getParams().beginTime +'&&'+'endTime='+getParams().endTime;
	queryParams.beginTime = getParams().beginTime;
	queryParams.endTime = getParams().endTime;
	queryParams.table_num = getParams().table_num; 
	queryParams.table_sum = getParams().table_sum; 
	queryParams.bianhao = getParams().bianhao; 
//	queryParams.table_first_2 = getParams().table_first_2; 
	queryParams.select = getParams().select; 
	$.ajax({
	    type: "POST",
	    url: ctx + "/yunying/yemiantongji/list",
	    data: JSON.stringify(queryParams),
	    contentType: "application/json;charset=utf-8",
	    success : function(data) {
	    	console.info('++++++data+++++')
	    	console.info(data)
	    	$('#tt').datagrid('loadData', data);
	    }
    });
}



function Obj(id,inputVal,selectVal){
	this.Id=id;
	this.Input=inputVal;
	this.Select=selectVal;
}
function getInput(table){
	
	var arr=new Array();
	table.each(function(){
    arr.push(new Obj($(this).attr("id"),$(this).val(),$(this).parent().parent().find("select").find("option:selected").text()))
//	arr.push(new Obj(table.index(this)+1,$(this).val(),$(this).parent().parent().find("select").find("option:selected").text()))
	})

	return arr
}


function getParams(){
	var select = $("#list_select").children('option:selected').val();
	var table_sce=document.getElementsByTagName("table_sce");
	var params = {
			'beginTime': $("#beginTime").val(),
			'endTime': $("#endTime").val(),
			'bianhao': $("#bianhao").val(),
			table_num:getInput($(".table_first input")),
			table_sum:getInput($(".table_sce input"))
			 
	}
	return params;
}
