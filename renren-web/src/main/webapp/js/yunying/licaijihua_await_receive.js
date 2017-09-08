$(function () {
	initTableGrid();
	initTimeCond();
	initExportFunction();
});
function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		var statPeriod = $("#stat_period").val();
		if(!statPeriod){
			alert('请先选择日期');
			return;
		}
		executePost('../yunying/licaijihua/export', {'params' : JSON.stringify(params)});  
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
//        url: '../yunying/licaijihua/list',
//        postData: getParams(),
        datatype: "json",
        colModel: [			
			{ label: '计划类型', name: 'PLAN_TYPE', index: '$STAT_PERIOD', width: 80 },
			{ label: '计划期限', name: 'BORROW_PERIOD', index: '$REG_NUM', width: 80 }, 			
			{ label: '待收本金总量', name: 'CAPITAL', index: '$AUTH_NUM', width: 100 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}}, 			
			{ label: '总量占比', name: 'TOTAL_RATE', index: '$FIRST_INV_NUM', width: 80 }, 			
			{ label: '活期待收总量', name: 'HUOQI_CAPITAL', index: '$RE_NUM', width: 90 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}}, 			
			{ label: '活期待收占比', name: 'HUOQI_CAPITAL_RATE', index: '$INV_NUM', width: 90 }, 			
			{ label: '当月成交总量', name: 'M_J_CAPITAL', index: '$INV_FIRST_MONEY', width: 100 , formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}    }, 			
			{ label: '当日成交总量', name: 'D_J_CAPITAL', index: '$Y_INV_FIRST_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '当月退出总量', name: 'M_TENDER_CAPITAL', index: '$INV_MONEY', width: 90, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '当日退出总量', name: 'D_TENDER_CAPITAL', index: '$Y_INV_MONEY', width: 100, formatter:function(cellvalue, options, rowObject){if(cellvalue){return formatNumber(cellvalue,2);}else{return '';}}  }, 			
			{ label: '当日退出率', name: 'TUICHU_RATE', index: '$RE_AMOUNT', width: 100  } 			
        ],
        height:  $(window).height()-130,
        rowNum: 20,
        viewrecords: true,
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
		
		rowList : [10,30,50],
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportDalilyMarketing: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportDalilyMarketing = {};
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
			var url = vm.dmReportDalilyMarketing.statPeriod == null ? "../dmreportdalilymarketing/save" : "../dmreportdalilymarketing/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportDalilyMarketing),
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
				    url: "../dmreportdalilymarketing/delete",
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
			$.get("../dmreportdalilymarketing/info/"+statPeriod, function(r){
                vm.dmReportDalilyMarketing = r.dmReportDalilyMarketing;
            });
		},
		reload: function (event) {
			vm.showList = true;
			
			var statPeriod = $("#stat_period").val();
			if(!statPeriod){
				alert('请先选择日期');
				return;
			}
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../yunying/licaijihua/list',
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