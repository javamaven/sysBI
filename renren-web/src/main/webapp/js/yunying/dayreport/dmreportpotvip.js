$(function () {
  initTableGrid();
  initTimeCond();
  initExportFunction();
  initEvent();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../../dmreportpotvip/exportExcel', {'params' : JSON.stringify(params)});  
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
//        url: '../../dmreportpotvip/list',
        datatype: "json",
        colModel: [			
			{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 50, key: true },
			{ label: '用户ID', name: 'userId', index: '$USER_ID', width: 80 }, 			
			{ label: '存管ID', name: 'cgUserId', index: '$CG_USER_ID', width: 80 }, 			
			{ label: '用户名', name: 'username', index: '$USERNAME', width: 80 }, 			
			{ label: '电话', name: 'phone', index: '$PHONE', width: 80 }, 			
			{ label: '姓名', name: 'realname', index: '$REALNAME', width: 80 }, 			
			{ label: '账户资产权益额', name: 'moneyAll', index: '$MONEY_ALL', width: 80 }, 			
			{ label: '账户余额', name: 'balance', index: '$BALANCE', width: 80 }, 			
			{ label: '待收金额（≥5万元)', name: 'moneyWait', index: '$MONEY_WAIT', width: 80 }, 			
			{ label: '投资次数(前3次)', name: 'sumInvest', index: '$SUM_INVEST', width: 80 }, 			
			{ label: '平均投资期限偏好(大于100天)', name: 'avgPeriod', index: '$AVG_PERIOD', width: 80 }, 			
			{ label: '累计充值金额（≥5万元）', name: 'amount', index: '$AMOUNT', width: 80 }, 			
			{ label: '累计投资金额（≥5万元）', name: 'moneyInvest', index: '$MONEY_INVEST', width: 80 }, 			
			{ label: '注册日期', name: 'registerTime', index: '$REGISTER_TIME', width: 80 },
			{ label: '使用红包金额', name: 'tendVouche', index: '$TEND_VOUCHE', width: 80 }, 			
			{ label: '债转金额', name: 'transferCapital', index: '$TRANSFER_CAPITAL', width: 80 }, 			
			{ label: '债转次数', name: 'transferNum', index: '$TRANSFER_NUM', width: 80 }, 			
			{ label: '渠道名称', name: 'channelName', index: '$CHANNEL_NAME', width: 80 }	
        ],
		viewrecords: true,
        height: $(window).height()-130,
        rowNum: 20,
//		rowList : [20,30,50],
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

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportPotVip: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportPotVip = {};
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
			var url = vm.dmReportPotVip.statPeriod == null ? "../dmreportpotvip/save" : "../dmreportpotvip/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportPotVip),
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
				    url: "../dmreportpotvip/delete",
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
			$.get("../dmreportpotvip/info/"+statPeriod, function(r){
                vm.dmReportPotVip = r.dmReportPotVip;
            });
		},

		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../../dmreportpotvip/list',
	            postData: getParams()
            }).trigger("reloadGrid");
		}
	}
});

function getParams(){
	var params = {
			'statPeriod': $("#stat_period").val().replace(/-/g,"")
	};
	return params;
}