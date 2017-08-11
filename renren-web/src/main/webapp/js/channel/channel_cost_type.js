$(function () {
	initTableGrid();
});

function initTableGrid(){
    $("#jqGrid").jqGrid({
        url: '../dimchanneltype/list',
        datatype: "json",
        colModel: [			
			{ label: '渠道中心', name: 'CHANNEL_CENTER', index: '$CHANNEL_LABEL', width: 80 ,align: 'right'},
			{ label: '渠道负责人', name: 'CHANNEL_HEAD', index: '$CHANNEL_TYPE', width: 80 ,align: 'right'},	
			{ label: '渠道名称', name: 'CHANNEL_NAME', index: '$CHANNEL_TYPE', width: 80 ,align: 'right',
				cellattr: function (rowId, val, rawObject, cm, rdata){
					 if(rawObject.IS_REPEAT == 1 ){
			                return "style='color:red'";
			         }
				},
				formatter:function(cellvalue, options, rowObject){
					if(rowObject.IS_REPEAT == 1 ){
						return cellvalue + '(重复渠道)';
					}else{
						return cellvalue;
					}
				}
			},
			{ label: '渠道标签', name: 'CHANNEL_LABEL', index: '$CHANNEL_TYPE', width: 80 ,align: 'right'},
			{ label: '渠道状态', name: 'CHANNEL_STATUS', index: '$CHANNEL_TYPE', width: 80 ,align: 'right'},
//			{ label: '渠道开始时间', name: 'CHANNEL_STARTTIME', index: '$CHANNEL_TYPE', width: 80 ,align: 'right'},
			{ label: '渠道类型', name: 'PAYMENT_WAY', index: '$CHANNEL_TYPE', width: 80,align: 'right' }, 
			{ label: '是否收费', name: 'CHANNEL_TYPE', index: '$CHANNEL_TYPE', width: 80,align: 'right'
//				,formatter:function(cellvalue, options, rowObject){
//					if(cellvalue){
//						return '';
//					}else{
//						return '';
//					}
//				}
			}
			
			
        ],
		viewrecords: true,
		height: $(window).height()-130,
	    rowNum: 20,
//        height: 385,
//        rowNum: 10,
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
        onSelectRow: function(rowId){
        	currSelectRow = $("#jqGrid").jqGrid('getRowData',rowId);
    	},
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        },
        loadComplete: function (){
        	currSelectRow = null;
        }
    });
}

var currSelectRow ; 

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		dimChannelType: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.dimChannelType = {};
		},
		update: function (event) {
//			var channelLabel = getSelectedRow();
//			var channelLabel = currSelectRow.CHANNEL_LABEL;
//			if(channelLabel == null){
//				return ;
//			}
			vm.showList = false;
            vm.title = "修改";
            $("#channel_type").val(currSelectRow.CHANNEL_TYPE);
//            vm.getInfo(channelLabel);
		},
		saveOrUpdate: function (event) {
			var channelType = $("#channel_type").val();
			var label = currSelectRow.CHANNEL_LABEL;
			var url = label == null ? "../dimchanneltype/save" : "../dimchanneltype/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify({'channelType': channelType, 'channelLabel': label}),
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
			if(!currSelectRow){
				alert('请选择要删除的记录');
				return ;
			}
			var channelLabel = currSelectRow.CHANNEL_LABEL;
			var channelLabels = [];
			channelLabels.push(channelLabel);
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../dimchanneltype/delete",
				    data: JSON.stringify(channelLabels),
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
		getInfo: function(channelLabel){
			$.get("../dimchanneltype/info/"+channelLabel, function(r){
                vm.dimChannelType = r.dimChannelType;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			
			 $("#jqGrid").jqGrid('setGridParam',{  
	            datatype:'json', 
	            url: '../dimchanneltype/list',
	            postData: getParams(), //发送数据  
	            page:page 
	        }).trigger("reloadGrid"); //重新载入  
		}
	}
});
function getParams(){
	var params = {
			'channelName': $("#channel_name").val(),
			'channelLabel': $("#channel_label").val(),
			'channelType': $("#channel_type_select").val()
	};
	return params;
}
