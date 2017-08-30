$(function () {
	initTableGrid();
	initTableGrid_partment();
	initUpload();
	initExportFunction();
	initSelectEvent();
	initTimeCond();
	
	
	
	init();
});

function init(){
	var html = "";
	$("#test").html(html);
	
}
function initTimeCond(){
    $("#month").datetimepicker({
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
function initSelectEvent(){
	//日报，月报切换
	$("#list_select").change(function(){
		var select = $(this).children('option:selected').val();
		if(select == 'staff'){
			$("#staff_div").show();
			$("#partment_div").hide();
		}else if(select == 'partment'){
			$("#staff_div").hide();
			$("#partment_div").show();
		}
	});
	//月报列表，月报汇总切换
	$("#report_type_select").change(function(){
		var select = $(this).children('option:selected').val();
		monthSelect(select);
	});
	
}

function initExportFunction(){
	var params = {};
	$('#btn_exports').click(function(){
		executePost('../hr/exportExcel', {'params' : JSON.stringify(params)});  
	});

}

function initUpload(){
    new AjaxUpload('#upload', {
        action: '../hr/worktime/upload',
        name: 'file',
        autoSubmit:true,
        responseType:"json",
        onSubmit:function(file, extension){
        },
        onComplete : function(file, r){
        	console.info(r);
            if(r.code == 0){
                alert('导入成功'+r.sucess_record+'条');
            }else{
                alert(r.msg);
            }
        }
    });
}

function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../hr/worktime/list',
        datatype: "json",
        colModel: [			
			{ label: '员工姓名', name: 'REALNAME', index: '$REALNAME', width: 50, key: true },
			{ label: '部门', name: 'DEPARTMENT', index: '$POST', width: 80 }, 			
			{ label: '职位', name: 'POST', index: '$POST', width: 80 },
			{ label: '是否副总级及以上职位', name: 'IF_BOSS', index: '$POST', width: 100 }, 
			{ label: '月度总加班工时', name: 'OVERTIME', index: '$POST', width: 80 }, 
			{ label: '日均加班工时', name: 'DAY_OVERTIME', index: '$POST', width: 80 }, 
			{ label: '月度总迟到工时', name: 'LATETIME', index: '$POST', width: 80 }, 
			{ label: '月度迟到次数', name: 'LATE_TIMES', index: '$POST', width: 80 }, 
			{ label: '加班工时排名', name: 'RANK', index: '$POST', width: 80 }
        ],
		viewrecords: true,
//        height: 385,
//        rowNum: 10,
        height: $(window).height()-130,
        rowNum: 1000,
//		rowList : [10,30,50],
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
        }
    });
}

function initTableGrid_partment(){
    $("#jqGrid_partment").jqGrid({
//        url: '../hr/worktime/partmentlist',
        datatype: "json",
        colModel: [			
			{ label: '部门', name: 'DEPARTMENT', index: '$POST', width: 80 }, 			
			{ label: '考勤人数', name: 'CLOCK_NUM', index: '$POST', width: 80 },
			{ label: '月度总加班工时', name: 'M_OVERTIME', index: '$POST', width: 80 }, 
			{ label: '月度人均加班工时', name: 'M_PER_OVERTIME', index: '$POST', width: 80 }, 
			{ label: '日人均加班工时', name: 'D_PE_OVERTIME', index: '$POST', width: 80 }, 
			{ label: '月度人均迟到工时', name: 'M_PER_LATETIME', index: '$POST', width: 80 }, 
			{ label: '月度人均迟到次数', name: 'M_PER_LATE_TIMES', index: '$POST', width: 80 }, 
			{ label: '加班最长员工', name: 'MOST_OVERTIME_STAFF', index: '$POST', width: 80 },
			{ label: '人均加班工时排名', name: 'RANK', index: '$POST', width: 80 }
        ],
		viewrecords: true,
//        height: 385,
//        rowNum: 10,
        height: $(window).height()-130,
        rowNum: 1000,
//		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
//        multiselect: true,
        pager: "#jqGridPager_partment",
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
        	$("#jqGrid_partment").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
    $("#partment_div").hide();
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dimChannelCostNew: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dimChannelCostNew = {};
		},
		update: function (event) {
			var statPeriod = getSelectedRow();
			if(statPeriod == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(statPeriod)
		},
		saveOrUpdate: function (event) {
			var url = vm.dimChannelCostNew.statPeriod == null ? "../dimchannelcostnew/save" : "../dimchannelcostnew/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dimChannelCostNew),
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
			var statPeriods = getSelectedRows();
			if(statPeriods == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../dimchannelcostnew/delete",
				    data: JSON.stringify(statPeriods),
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
		getInfo: function(statPeriod){
			$.get("../dimchannelcostnew/info/"+statPeriod, function(r){
                vm.dimChannelCostNew = r.dimChannelCostNew;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var type = $("#list_select").children('option:selected').val();
			var month = $("#month").val();
			if(!month){
				alert('请先选择月份');
				return;
			}
			if(type == 'staff'){
				var page = $("#jqGrid").jqGrid('getGridParam','page');
				$("#jqGrid").jqGrid('setGridParam',{ 
					datatype:'json',
					url: '../hr/worktime/list',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}else if(type == 'partment'){
				var page = $("#jqGrid_partment").jqGrid('getGridParam','page');
				$("#jqGrid_partment").jqGrid('setGridParam',{ 
                	datatype:'json',
					url: '../hr/worktime/partmentlist',
		            postData: getParams()
	            }).trigger("reloadGrid");
			}
			
		}
	}
});

function getParams(){
	var params = {};
	var month =  $("#month").val();
	if(month){
		month = month.replace("-", "");
		params.month = month;
	}
	return params;
}