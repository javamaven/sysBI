$(function () {
	initTableGrid();
	initUpload();
	initTimeCond();
	initExportFunction();
});


function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../phonesalemonth/phoneSaleMonthExport', {'params' : JSON.stringify(params)});  
	});

}

function initUpload(){
    new AjaxUpload('#upload', {
        action: '../phonesalemonth/importPhoneSaleMonthData',
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
        },
        onComplete : function(file, r){
            if(r.code == 0){
                alert('数据导入成功');
            }else{
                alert(r.msg);
            }
        }
    });
}
function initTimeCond(){
    $("#stat_month").datetimepicker({
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

function initTableGrid(){
    $("#jqGrid").jqGrid({
        url: '../phonesalemonth/list',
        datatype: "json",
        colModel: [			
        	{ label: '用户ID', name: 'userId', index: '$USER_NAME', width: 80, key: true },
			{ label: '用户名', name: 'userName', index: '$USER_NAME', width: 80, key: true },
			{ label: '是否外包', name: 'mark', index: '$MARK', width: 80 },
			{ label: '电销类型', name: 'phoneType', index: '$MARK', width: 80 },
			{ label: '电销日期', name: 'callDate', index: '$CALL_DATE', width: 80 }, 			
			{ label: '接通结果', name: 'callResult', index: '$CALL_RESULT', width: 80 }, 			
			{ label: '电销人员', name: 'callPerson', index: '$CALL_PERSON', width: 80 }, 			
			{ label: '电销月份', name: 'statMonth', index: '$STAT_MONTH', width: 80 },	
			{ label: '导入时间', name: 'importTime', index: '$STAT_MONTH', width: 100 },
			{ label: '导入用户', name: 'importUserName', index: '$STAT_MONTH', width: 80 }
        ],
		viewrecords: true,
//        height: 385,
//        rowNum: 10,
        height: $(window).height()-125,
        rowNum: 20,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
//        multiselect: true,
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
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
//        	$("#jqGrid").closest(".jqgfirstrow").css({ "width" : "120px" }); 
        	
        	$(".jqgfirstrow").css({ "width" : "120px" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		phoneSaleMonth: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.phoneSaleMonth = {};
		},
		update: function (event) {
			var userName = getSelectedRow();
			if(userName == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(userName)
		},
		saveOrUpdate: function (event) {
			var url = vm.phoneSaleMonth.userName == null ? "../phonesalemonth/save" : "../phonesalemonth/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.phoneSaleMonth),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var userNames = getSelectedRows();
			if(userNames == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../phonesalemonth/delete",
				    data: JSON.stringify(userNames),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(userName){
			$.get("../phonesalemonth/info/"+userName, function(r){
                vm.phoneSaleMonth = r.phoneSaleMonth;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                datatype:'json', 
				url: '../phonesalemonth/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statMonth': $("#stat_month").val()
	};
	return params;
}