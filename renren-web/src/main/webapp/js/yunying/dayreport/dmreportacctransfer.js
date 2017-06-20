$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
	initEvent();
});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportacctransfer/exportExcel', {'params' : JSON.stringify(params)});  
	});

}


function initTimeCond(){
    $("#stat_period").datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true
    }).on("click",function(){
    });
}
function initTableGrid(){
    $("#jqGrid").jqGrid({
//        url: '../dmreportacctransfer/list',
        datatype: "json",
        colModel: [			
			{ label: '发起日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 120, key: true,align:'right'  },
			{ label: '待审核金额', name: 'awaitAuditM', index: '$AWAIT_AUDIT_M', width: 120,align:'right'  }, 			
			{ label: '处理中金额', name: 'processingM', index: '$PROCESSING_M', width: 120,align:'right'  }, 			
			{ label: '成功金额', name: 'successfulM', index: '$SUCCESSFUL_M', width: 120 ,align:'right' }, 			
			{ label: '失败金额', name: 'failureM', index: '$FAILURE_M', width: 120 ,align:'right' }, 			
			{ label: '审核不通过金额', name: 'nthroughM', index: '$NTHROUGH_M', width: 120,align:'right'  }, 			
			{ label: '发起人数', name: 'pCou', index: '$P_COU', width: 120 ,align:'right' }, 			
			{ label: '回款金额', name: 'recoverM', index: '$RECOVER_M', width: 120,align:'right'  }, 			
			{ label: '回款人数', name: 'recoverCou', index: '$RECOVER_COU', width: 120,align:'right'  }			
        ],
        viewrecords: true,
		height: $(window).height()-130,
        rowNum: 20,
		rowList : [10,30,50],
        rownumbers: true, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
        multiselect: false,
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
//        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportAccTransfer: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportAccTransfer = {};
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
			var url = vm.dmReportAccTransfer.statPeriod == null ? "../dmreportacctransfer/save" : "../dmreportacctransfer/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportAccTransfer),
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
				    url: "../dmreportacctransfer/delete",
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
			$.get("../dmreportacctransfer/info/"+statPeriod, function(r){
                vm.dmReportAccTransfer = r.dmReportAccTransfer;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../dmreportacctransfer/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
        	'statPeriod': $("#stat_period").val()
	};
	return params;
}