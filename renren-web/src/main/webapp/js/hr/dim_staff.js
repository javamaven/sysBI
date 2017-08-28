$(function () {
	initTableGrid();
	initUpload();
	initExportFunction();
});

function initExportFunction(){
	var params = {};
	$('#btn_exports').click(function(){
		executePost('../hr/exportExcel', {'params' : JSON.stringify(params)});  
	});

}

function initUpload(){
    new AjaxUpload('#upload', {
        action: '../hr/upload',
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
        url: '../hr/list',
        datatype: "json",
        colModel: [			
			{ label: '员工姓名', name: 'realname', index: '$STAT_PERIOD', width: 100, key: true,align:'right' },
			{ label: '身份证号', name: 'cardId', index: '$CHANNEL_LABEL', width: 80 ,align:'right',hidden: true}, 		
			{ label: '手机', name: 'phone', index: '$CHANNEL_LABEL', width: 80 ,align:'right'}, 			
			{ label: '部门', name: 'department', index: '$RECHARGE', width: 80 ,align:'right'},
			{ label: '部门序列', name: 'part', index: '$COST', width: 80 ,align:'right'},
			{ label: '职位', name: 'post', index: '$COST', width: 80 ,align:'right'},
			{ label: '是否副总监及以上职位', name: 'ifBoss', index: '$COST', width: 100,align:'right' }, 	
			{ label: '入职时间', name: 'workTime', index: '$COST', width: 80,align:'right' }, 	
			{ label: '离职时间', name: 'leaveTime', index: '$COST', width: 80,align:'right'}
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