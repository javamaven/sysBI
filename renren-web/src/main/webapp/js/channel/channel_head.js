$(function () {
	initTableGrid();
	initExportFunction();
	
	initChannelHeadList();
});

var selectHtml = '';
function initChannelHeadList(replaceStr){
	var html = '<option value =""></option>';
	if(replaceStr){
//		selectHtml = selectHtml.replace('<option value ="'+replaceStr+'">'+replaceStr+'</option>','');
	}
	$("#parent_channel_head").html(html + selectHtml);
}
var parentChannelHead = '-1';
function selectType(selectType){
	if(selectType == 0){//是市场部总监
		$("#parent_channel_head_div").hide();
		parentChannelHead = '-1';
	}else{
		parentChannelHead = '';
		$("#parent_channel_head_div").show();
		$("#modal_body_div").css("height", "145px;");
	}
}

function initExportFunction(){
	var params = {};
	$('#btn_exports').click(function(){
		executePost('../channel/channelHead/exportExcel', {'params' : JSON.stringify(params)});  
	});
}
var currSelectRow = {};
function initTableGrid(){
    $("#jqGrid").jqGrid({
        url: '../channel/channelHead/list',
        datatype: "json",
        colModel: [			
			{ label: '渠道负责人姓名', name: 'channel_head', index: '$STAT_PERIOD', width: 150, key: true,align:'right'
					,formatter:function(cellvalue, options, rowObject){
						selectHtml += '<option value ="'+cellvalue+'">'+cellvalue+'</option>';
						return cellvalue;
					}
			},
			{ label: '', name: 'channel_head_id', index: '$CHANNEL_LABEL', width: 150 ,align:'right', hidden:true},
			{ label: '', name: 'parent_channel_id', index: '$CHANNEL_LABEL', width: 150 ,align:'right', hidden:true}, 	
			{ label: '经分系统帐号', name: 'sys_account', index: '$CHANNEL_LABEL', width: 150 ,align:'right'}, 			
			{ label: '上级负责人', name: 'parent_channel_head', index: '$RECHARGE', width: 150 ,align:'right'
				,formatter:function(cellvalue, options, rowObject){
					return cellvalue;
				}
			},
			
			{ label: '所负责渠道', name: 'channelName', index: '$COST', width: 500,align:'right', hidden:true
				,formatter:function(cellvalue, options, rowObject){
					return cellvalue;
					if(cellvalue){
						return '<a href="" onclick="alert("'+cellvalue+'")">'+cellvalue+'</a>';
					}
				}
			}
        ],
		viewrecords: true,
        height: $(window).height()-130,
        viewrecords: true,
        rowNum: 20,
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        shrinkToFit: true,
        autoScroll: false,
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
        loadComplete: function(){
        	initChannelHeadList();
        }
    });
}


var operType;//操作类型，add,update,delete
function add(){
	operType = 'add';
	$("#channel_head").val('');
	$("#sys_account").val('')
	$('#table_win').modal('show');
}
function update(){
	if(!currSelectRow.channel_head){
		alert('请先选择要修改的行记录');
		return ;
	}
	operType = 'update';
	$('#table_win').modal('show');
	$("#channel_head").val(currSelectRow.channel_head)
	$("#sys_account").val(currSelectRow.sys_account)
	console.info(currSelectRow)
	if(currSelectRow.parent_channel_head == '-1'){
		$("input[name='TimeSelect']").get(0).checked=true; 
	}else{
		$("input[name='TimeSelect']").get(1).checked=true; 
		selectType('1');
		$("#parent_channel_head").val(currSelectRow.parent_channel_head);
//		initChannelHeadList(currSelectRow.parent_channel_head);//替换掉自己的上级负责人
	}
}
function del(){
	if(!currSelectRow.channel_head){
		alert('请先选择要删除的行记录');
		return ;
	}
	confirm('确定要删除选中的记录？', function(){
		$.ajax({
			type: "POST",
		    url: "../channel/channelHead/delete",
		    data: JSON.stringify(currSelectRow),
		    success: function(r){
				if(r.code == 0){
					alert('操作成功', function(index){
						reload();
					});
				}else{
					alert(r.msg);
				}
			}
		});
	});
}

function saveOrUpdate(){
	var url = '';
	if(operType == 'add'){
		url = '../channel/channelHead/save';
	}else if(operType == 'update'){
		url = '../channel/channelHead/update';
	}
	$.ajax({
		type: "POST",
	    url: url,
	    data: JSON.stringify(getParams()),
	    success: function(r){
	    	if(r.code === 0){
	    		$('#table_win').modal('hide');
				alert('操作成功', function(index){
					reload();
				});
			}else{
				alert(r.msg);
			}
		}
	});
}

function reload(){
	selectHtml = '';
	var page = $("#jqGrid").jqGrid('getGridParam','page');
	$("#jqGrid").jqGrid('setGridParam',{ 
        page:page
    }).trigger("reloadGrid");
}

function getParams(){
	var params = {
			'channelHead': $("#channel_head").val(),
			'sysAccount': $("#sys_account").val(),
			'parentChannelHead': parentChannelHead == '-1' ? '-1':$("#parent_channel_head").val()
			
	};
	if(currSelectRow){
		params.channel_head_id = currSelectRow.channel_head_id;
	}
	return params;
}