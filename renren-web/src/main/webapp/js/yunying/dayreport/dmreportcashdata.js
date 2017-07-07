$(function () {
  initTableGrid();
  initTimeCond();
  initExportFunction();
  initEvent();
});

function initExportFunction(){
	$('#btn_exports').click(function(){
		var params = getParams();
		executePost('../dmreportcashdata/exportExcel', {'params' : JSON.stringify(params)});  
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
//	        url: '../dmreportcashdata/list',
	        datatype: "json",
	        colModel: [			
				{ label: '日期', name: 'statPeriod', index: '$STAT_PERIOD', width: 90, key: true ,align:'right'},
				{ label: '用户ID', name: 'userId', index: '$USER_ID', width: 85,align:'right' }, 			
				{ label: '存管ID', name: 'cgUserId', index: '$CG_USER_ID', width: 85,align:'right' }, 			
				{ label: '用户名', name: 'username', index: '$USERNAME', width: 80 ,align:'right'}, 			
				{ label: '电话', name: 'phone', index: '$PHONE', width: 100 ,align:'right'}, 			
				
				{ label: '姓名', name: 'realname', index: '$PHONE', width: 100 ,align:'right'},
				
				{ label: '提现成功金额', name: 'money', index: '$CASH_MONEY', width: 100 ,align:'right'}, 			
				{ label: '账户资产权益额', name: 'total', index: '$FROST', width: 120 ,align:'right'}, 			
				{ label: '账户余额', name: 'balance', index: '$BALANCE', width: 80 ,align:'right'}, 			
				{ label: '待收金额', name: 'await', index: '$AWAIT', width: 80,align:'right' }, 	
				{ label: '是否VIP', name: 'isVip', index: '$IS_VIP', width: 80,align:'right' },
				{ label: '注册日期', name: 'regTime', index: '$REG_TIME', width: 150 ,align:'right'}, 	
				{ label: '渠道名称', name: 'channelName', index: '$CHANNEL_NAME', width: 80 ,align:'right'}, 
//				{ label: '首投时间', name: 'xmInvOneTime', index: '$XM_INV_ONE_TIME', width: 150,align:'right' }, 			
				{ label: '注册后首投间隔(分)(数据覆盖历史项目)', name: 'xmStJg', index: '$XM_ST_JG', width: 150 ,align:'right'}, 			
				
				{ label: '平均投资时间间隔(分)(数据覆盖历史项目)', name: 'avgXmTzJg', index: '$XM_ST_JG', width: 150 ,align:'right'}, 			
				
				{ label: '平均投资金额(数据覆盖历史项目)', name: 'avgXmInvMoney', index: '$XM_ST_JG', width: 150 ,align:'right'}, 			
				
				{ label: '投资次数(数据覆盖历史项目)', name: 'xmInvCou', index: '$XM_INV_COU', width: 150,align:'right' }, 			
				
				{ label: '投资次数中使用奖励次数(数据覆盖历史项目)', name: 'invPackBl', index: '$XM_INV_COU', width: 160,align:'right' }, 			
				

				{ label: '发起转让比例(数据覆盖历史项目)', name: 'zzBl', index: '$XM_INV_COU', width: 150,align:'right' }, 			
				
				
				{ label: '投资期限偏好(数据覆盖历史项目)', name: 'periodJq', index: '$PERIOD_JQ', width: 150 ,align:'right'}, 			
				
				{ label: '当前是否持有红包', name: 'rewardStatus', index: '$PERIOD_JQ', width: 100 ,align:'right'}, 			
				
				{ label: '当前持有红包金额', name: 'rewardMoney', index: '$PERIOD_JQ', width: 100 ,align:'right'}, 			
				
				{ label: '累计充值金额', name: 'czMoney', index: '$CZ_MONEY', width: 110 ,align:'right'}, 			
				{ label: '累计提现金额', name: 'txCgMoney', index: '$TX_CG_MONEY', width: 110 ,align:'right'} ,
				
				{ label: '提现金额占比充值金额', name: 'txCgMoneyBl', index: '$TX_CG_MONEY', width: 90 ,align:'right'}		
				
	        ],
			viewrecords: true,
	        height: $(window).height()-150,
	        rowNum: 20,
			rowList : [10,30,50],
	        rownumbers: true, 
	        autowidth:true,
	        shrinkToFit: false,
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
//	        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
	        }
	    });
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dmReportCashData: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dmReportCashData = {};
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
			var url = vm.dmReportCashData.statPeriod == null ? "../dmreportcashdata/save" : "../dmreportcashdata/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.dmReportCashData),
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
				    url: "../dmreportcashdata/delete",
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
			$.get("../dmreportcashdata/info/"+statPeriod, function(r){
                vm.dmReportCashData = r.dmReportCashData;
            });
		},
		reload: function (event) {
			vm.showList = true;
			$("#jqGrid").jqGrid("clearGridData");
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype:'json', 
				url: '../dmreportcashdata/list',
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