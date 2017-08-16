$(function () {
	initTableGrid();
	initUpload();
	initExportFunction();
});

function initExportFunction(){
	var params = {};
	$('#btn_exports').click(function(){
		executePost('../dimchannelcostnew/exportExcel', {'params' : JSON.stringify(params)});  
	});

}

function initUpload(){
    new AjaxUpload('#upload', {
        action: '../dimchannelcostnew/upload',
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
        url: '../dimchannelcostnew/list',
        datatype: "json",
        colModel: [			
			{ label: '渠道名称', name: 'CHANNEL_NAME', index: '$STAT_PERIOD', width: 100, key: true,align:'right' },
			{ label: '渠道标记', name: 'CHANNEL_LABEL', index: '$CHANNEL_LABEL', width: 80 ,align:'right'}, 			
			{ label: '累计充值', name: 'RECHARGE', index: '$RECHARGE', width: 80 ,align:'right'
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '累计费用', name: 'COST', index: '$COST', width: 80 ,align:'right'
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '渠道账户余额', name: 'BALANCE', index: '$COST', width: 80 ,align:'right'
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return formatNumber(cellvalue,2);
					}else{
						return '';
					}
				}
			},
			{ label: '最近充值记录', name: 'LAST_RECHARGE_TIME', index: '$COST', width: 80,align:'right' }, 	
			{ label: '最近费用记录', name: 'LAST_COST_TIME', index: '$COST', width: 80,align:'right' }, 	
			{ label: '最近注册天数', name: 'LAST_REG_DAY', index: '$COST', width: 80,align:'right'
				,formatter:function(cellvalue, options, rowObject){
					if(cellvalue){
						return cellvalue + "天";
					}else{
						return '';
					}
				}
			}
			
			
//				
//			last_recharge_time	
//			last_cost_time	
//			last_reg_time	
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
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});