var getSelectValue = function(select) {
	//获取第一个ID
	var selectId = select.id;
	var selectSplit = selectId.split("_");
    // alert(selectId);
	var idx = select.selectedIndex,option,value; 
	var selectOptionText =""
	if (idx > -1) { 
		option = select.options[idx]; 
		selectOptionText = option.text; 
	};
	/*-----------------------------------逻辑-----------------------------------------*/	
	// 逻辑数值
	var arrLogicDim = new Array();
	var arrLogicIndex = new Array();
	// 维度
	arrLogicDim=["请选择","等于","在范围内"];
	// 指标
	arrLogicIndex=["请选择","等于","大于","大于等于","小于","小于等于"];
	// 赋值逻辑
	/*-----------------------------------内容-----------------------------------------*/
	// 内容数据
	var labelConteneDim = new Array();
    labelConteneDim = ["请选择","后台渠道名称","用户性别","用户年龄段","是否注册存管系统","是否开通存管账号","投资时间段"];
    var labelConteneIndex = new Array();
    labelConteneIndex = ["请选择","注册日期","首投日期","投资间隔","项目期限偏好","债转期限偏好","累计投资金额","累计投资年化金额","账户可用余额","最近一笔投资金额","单笔投资最高金额",
    "使用优惠投资的比例","累计使用的红包及券的金额","累计使用的红包及券的次数","红包券收益占比已收收益比例","累计红包使用率","次均红包金额","最近一次登陆日期","最近一次投资日期","最近一次充值日期","平均每笔项目投资金额",
    "平均每笔债转投资金额","平均每笔综合投资金额","投资项目次数","投资债转次数","投资综合次数","账户可用优惠余额","距离上次营销天数","当前待收金额"];

    /*-----------------------------------条件-----------------------------------------*/
    // 性别
    var dimSex = new Array();
    dimSex = ["全部","男","女","未识别"];
    // 年龄段
    var dimAgeLevel = new Array();
    dimAgeLevel = ["全部","小于18岁","18-25岁","26-30岁","31-35岁","36-40岁","41-45岁","46-50岁","51-60岁","60岁以上","未识别"];
    var dimFlag = new Array();
    dimFlag = ["是","否"];
    // 投资时间段
    var dimInvestTimeLevel = new Array();
    for(i = 0; i < 24; i ++){
        dimInvestTimeLevel.push(i);
    };
    /*-----------------------------------赋值(逻辑/内容)-----------------------------------------*/
	if(selectId == "labelType_"+selectSplit[1]){
	    // 逻辑
	    var labelLogic =document.getElementById("labelLogic_"+selectSplit[1]);
	    // 清空逻辑选项
	    labelLogic.options.length=0;
	    // 内容
	    var labelContene =document.getElementById("labelContene_"+selectSplit[1]);
        // 清空内容选项
        labelContene.options.length=0;
        // 添加选项
        if(selectOptionText =="维度"){
            // 逻辑
            for(i = 0;i < arrLogicDim.length;i++){
                labelLogic.options.add(new Option(arrLogicDim[i],"value"));
            }
            // 内容
            for(i = 0;i < labelConteneDim.length;i++){
                labelContene.options.add(new Option(labelConteneDim[i],"value"));
            }
        };
        if(selectOptionText =="指标"){
            // 逻辑
            for(i = 0;i < arrLogicIndex.length;i++){
                labelLogic.options.add(new Option(arrLogicIndex[i],"value"));
            }
            // 内容
            for(i = 0;i < labelConteneIndex.length;i++){
                labelContene.options.add(new Option(labelConteneIndex[i],"value"));
            }
        }

        // 调用
        cleanSetDataTable("#td_labelType_"+selectSplit[1],selectOptionText);
    };

    // 逻辑
    if(selectId == "labelLogic_"+selectSplit[1]){
        // 调用
        cleanSetDataTable("#td_labelLogic_"+selectSplit[1],selectOptionText);
    }

    /*-----------------------------------赋值(条件)-----------------------------------------*/
    if(selectId == "labelContene_"+selectSplit[1]){
        var labelContenediv = document.getElementById('labelCondition_'+selectSplit[1]);
        // 清空条件对应div里面的内容
        if(selectOptionText =="注册日期" || selectOptionText =="首投日期" || selectOptionText =="用户性别" || selectOptionText =="用户年龄段" || selectOptionText =="是否注册存管系统"
        || selectOptionText =="是否开通存管账号" || selectOptionText =="投资时间段" || selectOptionText =="后台渠道名称"|| selectOptionText =="最近一次登陆日期"|| selectOptionText =="最近一次投资日期"
        || selectOptionText =="最近一次充值日期"){
            // 清空dynamicTable 对应行的条件
            $('#labelCondition_'+selectSplit[1]).empty();
            // 清空 dataTable 对应行的条件
            $('#td_labelCondition_'+selectSplit[1]).empty();
        }else{
            $('#labelCondition_'+selectSplit[1]).empty();
            $('#td_labelCondition_'+selectSplit[1]).empty();
            $('#labelCondition_'+selectSplit[1]).append('<input type="text" id="input_labelCondition_'+selectSplit[1]+'" class="form-control" oninput="getInputValue(this)"/>');
        };
        // 后台渠道名称
        if(selectOptionText =="后台渠道名称"){
            labelContenediv.innerHTML ='<select class="form-control" multiple id="channelName_'+selectSplit[1]+'"></select>';
            var channelNameId =document.getElementById("channelName_"+selectSplit[1]);
            channelNameId.options.length=0;
            $.ajax({
                type: "POST",
                url: "../channel/channelAll/getChannel",
                data: JSON.stringify(),
                contentType: "application/json;charset=utf-8",
                success : function(msg) {
                    console.log(msg);
                    for(var list in msg.Channel){
                        for(var key in msg.Channel[list]){
                            if(key == "channelName")
                                channelNameId.options.add(new Option(msg.Channel[list][key],"value"));
                        }
                    };
                    $("#channelName_"+selectSplit[1]).select2({
                        width:'100%'
                    });
                    // 获取select的值
                    getSelect2Value("#channelName_",selectSplit[1]);
                }
             });
        };

        if(selectOptionText =="注册日期"){
            // 在对应div里面添加内容
            labelContenediv.innerHTML = '<input type="text" class="form-control form_datetime_2" id="reg_date_'+selectSplit[1]+'" placeholder="请选择时间"/>';
            // 添加datetime样式
            setDateTime("#reg_date_",selectSplit[1]);
        };
        if(selectOptionText =="首投日期"){
            // 在对应div里面添加内容
            labelContenediv.innerHTML = '<input type="text" class="form-control form_datetime_2" id="first_Investdate_'+selectSplit[1]+'" placeholder="请选择时间" />';
            // 添加datetime样式
            setDateTime("#first_Investdate_",selectSplit[1]);
        };
        if(selectOptionText =="最近一次登陆日期"){
            // 在对应div里面添加内容
            labelContenediv.innerHTML = '<input type="text" class="form-control form_datetime_2" id="last_login_time_'+selectSplit[1]+'" placeholder="请选择时间" />';
            // 添加datetime样式
            setDateTime("#last_login_time_",selectSplit[1]);
        };
        if(selectOptionText =="最近一次投资日期"){
            // 在对应div里面添加内容
            labelContenediv.innerHTML = '<input type="text" class="form-control form_datetime_2" id="last_inv_time_'+selectSplit[1]+'" placeholder="请选择时间" />';
            // 添加datetime样式
            setDateTime("#last_inv_time_",selectSplit[1]);
        };
        if(selectOptionText =="最近一次充值日期"){
            // 在对应div里面添加内容
            labelContenediv.innerHTML = '<input type="text" class="form-control form_datetime_2" id="last_recharge_time_'+selectSplit[1]+'" placeholder="请选择时间" />';
            // 添加datetime样式
            setDateTime("#last_recharge_time_",selectSplit[1]);
        };
        if(selectOptionText =="用户性别"){
            labelContenediv.innerHTML ='<select class="form-control" multiple id="dimSex_'+selectSplit[1]+'" ></select>';
            var dimSexId =document.getElementById("dimSex_"+selectSplit[1]);
            for(i = 0;i < dimSex.length;i++){
                dimSexId.options.add(new Option(dimSex[i],"value"));
            };
            // 添加select2 样式
            $("#dimSex_"+selectSplit[1]).select2({
                width:'100%'
            });
            // 获取select的值
            getSelect2Value("#dimSex_",selectSplit[1]);

        };
        if(selectOptionText =="用户年龄段"){
            labelContenediv.innerHTML ='<select class="form-control"  multiple id="dimAge_'+selectSplit[1]+'" ></select>';
            var dimAgeId =document.getElementById("dimAge_"+selectSplit[1]);
            for(i = 0;i < dimAgeLevel.length;i++){
                dimAgeId.options.add(new Option(dimAgeLevel[i],"value"));
            };
            // 添加select2 样式
            $("#dimAge_"+selectSplit[1]).select2({
                width:'100%'
            });
            // 获取select的值
            getSelect2Value("#dimAge_",selectSplit[1]);
        };
        if(selectOptionText =="是否注册存管系统" ){
            labelContenediv.innerHTML ='<select class="form-control"  multiple id="isRegCg_'+selectSplit[1]+'"></select>';
            var isRegCgId =document.getElementById("isRegCg_"+selectSplit[1]);
            for(i = 0;i < dimFlag.length;i++){
                isRegCgId.options.add(new Option(dimFlag[i],"value"));
            };
            // 添加select2 样式
            $("#isRegCg_"+selectSplit[1]).select2({
                width:'100%'
            });
            // 获取select的值
            getSelect2Value("#isRegCg_",selectSplit[1]);
        };
        if(selectOptionText =="是否开通存管账号"){
            labelContenediv.innerHTML ='<select class="form-control" multiple  id="isOpenCg_'+selectSplit[1]+'"></select>';
            var isOpenCgId =document.getElementById("isOpenCg_"+selectSplit[1]);
            for(i = 0;i < dimFlag.length;i++){
                isOpenCgId.options.add(new Option(dimFlag[i],"value"));
            };
            // 添加select2 样式
            $("#isOpenCg_"+selectSplit[1]).select2({
                width:'100%'
            });
            // 获取select的值
            getSelect2Value("#isOpenCg_",selectSplit[1]);
        };
        if(selectOptionText =="投资时间段"){
            labelContenediv.innerHTML ='<select class="form-control"  multiple id="dimInvestTimeLevel_'+selectSplit[1]+'"></select>';
            var dimInvestTimeLevelId =document.getElementById("dimInvestTimeLevel_"+selectSplit[1]);
            for(i = 0;i < dimInvestTimeLevel.length;i++){
                dimInvestTimeLevelId.options.add(new Option(dimInvestTimeLevel[i],"value"));
            };
             // 添加select2 样式
             $("#dimInvestTimeLevel_"+selectSplit[1]).select2({
                 width:'100%'
             });
            // 获取select的值
            getSelect2Value("#dimInvestTimeLevel_",selectSplit[1]);
        };
        // 调用
        cleanSetDataTable("#td_labelContene_"+selectSplit[1],selectOptionText);
    };

};


// 初始化
var public_i = 0;
function addTr(){
	// var length = $("#dynamicTable tbody tr").length;
	public_i++;
	var oTable = document.getElementById("dynamicTable");
	var tBodies = oTable.tBodies;
	var tbody = tBodies[0];
	var tr = tbody.insertRow(tbody.rows.length);
	var td_1 = tr.insertCell(0);
	td_1.innerHTML = '<input type="text" class="form-control" name="NO" size="2" value="'+public_i+'" disabled="disabled" />';
	var td_2 = tr.insertCell(1);
	td_2.innerHTML = '<select class="form-control" id="labelType_'+public_i+'" onchange="getSelectValue(this)" >'+
						'<option value="0">请选择</option>'+
						'<option value="1">指标</option>'+
						'<option value="2">维度</option>'+
					'</select>';
	var td_3 = tr.insertCell(2);
	td_3.innerHTML = '<select class="form-control" id="labelContene_'+public_i+'" onchange="getSelectValue(this)" ></select>';
	var td_4 = tr.insertCell(3);
	td_4.innerHTML = '<select class="form-control" id="labelLogic_'+public_i+'" onchange="getSelectValue(this)" ></select>';
	var td_5 = tr.insertCell(4);
	td_5.innerHTML = '<div id="labelCondition_'+public_i+'" ><input type="text" id="input_labelCondition_'+public_i+'" class="form-control" oninput="getInputValue(this)"/></div>';
	var td_6 = tr.insertCell(5);
	td_6.innerHTML = '<a class="btn btn-danger" onClick="deltr(this)"><i class="fa fa-trash-o"></i>&nbsp;删除</a>';
	//更新行号
	changeIndex();

	buildDataTable(public_i);
};

// 临时表的行列创建
function buildDataTable(length){
	var oTable = document.getElementById("dataTable");
	var tBodies = oTable.tBodies;
	var tbody = tBodies[0];
	var tr = tbody.insertRow(tbody.rows.length);
	var td_1 = tr.insertCell(0);
	td_1.id = "td_ID_"+length;
	td_1.innerHTML = '<div id="div_'+length+'">'+length+'</div>';
	var td_2 = tr.insertCell(1);
	td_2.id = "td_labelType_"+length;
	var td_3 = tr.insertCell(2);
	td_3.id = "td_labelContene_"+length;
	var td_4 = tr.insertCell(3);
	td_4.id = "td_labelLogic_"+length;
	var td_5 = tr.insertCell(4);
	td_5.id = "td_labelCondition_"+length;
};
// 删除对应行
function delDataTable(length){
    $("#div_"+length).parent().parent().remove();
};

// 清除并且赋值 表格 datatable 对应 表格 dynamicTable 的 行和列的值
function cleanSetDataTable(strIndex,strValue){
        //清空 表格 dataTable 对应位置的td的值
        $(strIndex).empty();
        //赋值 表格 dataTable 对应位置的td的值
        $(strIndex).append(strValue);
};

function changeIndex() {
	var i = 1;
	$("#dynamicTable tbody tr").each(function () { //循环tab tbody下的tr
		$(this).find("input[name='NO']").val(i++);//更新行号
	});
}
// 删除行
function deltr(opp) {
	var length = $("#dynamicTable tbody tr").length;
	// alert(length);
	if (length <= 1) {
		alert("至少保留一行");
	} else {
	    //移除正式表格当前行
		$(opp).parent().parent().remove();
	    // 删除临时表格对应的列
	    delDataTable($(opp).parent().parent().find("input").attr("value"));
		changeIndex();
	}
};

// 获取select2选择的值
function getSelect2Value(strDim,strId) {
   $(strDim+strId).on("change", function() {
        var $this = $(this);
        var strCondition = "";
        if($(strDim+strId).select2('data').length>0){
            for(i = 0;i < $(strDim+strId).select2('data').length;i++){
                strCondition += $(strDim+strId).select2('data')[i].text + ",";
            };
        };
        cleanSetDataTable("#td_labelCondition_"+strId,strCondition.substring(0,strCondition.length-1));
    });
};

// 获取input的值
function getInputValue(select){
	//获取第一个ID
	var selectId = select.id;
	var selectSplit = selectId.split("_");
	var strCondition = document.getElementById("input_labelCondition_"+selectSplit[2]).value;
    cleanSetDataTable("#td_labelCondition_"+selectSplit[2],strCondition);
};

// 设置时间空间的值
function setDateTime(str,strId){
    // 添加datetime样式
    $(str+strId).
        datetimepicker({
        format: 'yyyy-mm-dd',
        minView:'month',
        language: 'zh-CN',
        autoclose:true,
        todayBtn : true,
        setStartDate:new Date()
    }).on('changeDate',function(ev){
        var  time=$(str+strId).val();
        // 调用
        cleanSetDataTable("#td_labelCondition_"+strId,time);
    });
};


// 遍历表
$('#btn_save').click(function() {

    var tableInfo = "";
    var tableObj = document.getElementById('dataTable');
    var list1= new Object();
    var alterStr = '';
    var str = '';
    list1["tag"] = {
    "Type":$('#lagTagType option:selected').text(),
    "MainLabel_Name":document.getElementById("mainLabelName").value
    };

    if($('#lagTagType option:selected').text() == "" || $('#lagTagType option:selected').text() == "请选择标签类型"){
        alterStr = "请选择标签类型";
    };
    if(document.getElementById("mainLabelName").value == ""){
        alterStr = "请输入标签名称";
    }

    for (var i = 0; i < tableObj.rows.length; i++) {    //遍历Table的所有Row
        var list2="";
        for (var j = 0; j < tableObj.rows[i].cells.length; j++) {   //遍历Row中的每一列
            str = "";
            if(tableObj.rows[i].cells[j].innerText == ''){
                switch(j){
                    case 1:
                        str = '维度/指标';
                        break;
                    case 2:
                        str = '内容';
                        break;
                    case 3:
                        str = '逻辑';
                        break;
                    case 4:
                        str = '条件';
                        break;
                }
                alterStr = "第  "+(i+1)+"  行的  "+str+"  不能为空！";
            }
            else{
                list2={
                    "labelID":i+1,
                    "labelType":tableObj.rows[i].cells[1].innerText,
                    "labelContene":tableObj.rows[i].cells[2].innerText,
                    "labelLogic":tableObj.rows[i].cells[3].innerText,
                    "labelCondition":tableObj.rows[i].cells[4].innerText
                };
            }
        }

        list1["tagDetail"+i] = list2;
    };


    pageInfo = {
        mainlabelName:document.getElementById("mainLabelName").value.replace(/(^\s*)|(\s*$)/g, ""),
        type:$('#lagTagType option:selected').text()
    };

    if(alterStr == ""){
        $.ajax({
            type: "POST",
            url: "../labelTagManager/exists",
            data: JSON.stringify(pageInfo),
            contentType: "application/json;charset=utf-8",
            success : function(msg) {
                    if(msg.queryExists == 0){
                        $.ajax({
                            type: "POST",
                            url: "../labelTagManager/save",
                            data: JSON.stringify(list1),
                            contentType: "application/json;charset=utf-8",
                            success : function(msg) {
                                    console.log(msg);
                                    alert('保存标签成功', function(index){
                                        window.location.reload();
                                    });
                                }
                            });
                        }
                        else{
                            alert($('#lagTagType option:selected').text() + "：" + document.getElementById("mainLabelName").value + " 已存在，请重新输入标签名字...");
                        }
                    }
            });


    }else{
        alert(alterStr);
    }

});

// 删除当前选项
$("#btn_del").click(function(){
    if( $('#id_type option:selected').text() == "请选择标签类型" ){
        alert('请选择"标签查询"的"标签类型"');
    }else if( $('#id_select option:selected').text() == "请选择标签"){
        alert('请选择"标签查询"的"标签"');
    }else{
            pageInfo = {
                mainlabelName:$('#id_select option:selected').text(),
                type:$('#id_type option:selected').text()
            };

			confirm($('#id_type option:selected').text() +'：'+$('#id_select option:selected').text()+'，确定要删除吗?', function(){
                $.ajax({
                    type: "POST",
                    url: "../labelTagManager/delete",
                    data: JSON.stringify(pageInfo),
                    contentType: "application/json;charset=utf-8",
                    success : function(msg) {
                            console.log(msg);
                            alert('删除标签成功', function(index){
                                window.location.reload();
                            });
                        }
                    });
			});
    }
});

// 获取 标签统计条件
$("#btn_search").click(function (){
    // 显示之前，先把当前表格销毁
    $('#labelTable').bootstrapTable('destroy');

    pageInfo = {
        mainLabelName:$('#id_select option:selected').text()
    };
     $.ajax({
            type: "POST",
            url: "../labeltaguser/labelTaglistdetail",
            data: JSON.stringify(pageInfo),
            contentType: "application/json;charset=utf-8",
            success : function(msg) {
                console.log(msg);
                 var a = '';
                for(var list in msg.labelList){
                    var d = '{'
                    for(var key in msg.labelList[list]){
                        d += '"' + key + '":"' + msg.labelList[list][key] + '",';
                    }
                    d = d.substring(0,d.length-1) + '},';
                    a += d;
                };
                a = '['+a.substring(0,a.length-1)+']';

                var b = '['+
                '{field:"labelID",title:"ID",align:"center",valign:"middle",sortable:"true"},'+//居中对齐
                '{field:"labelType",title:"维度/指标",align:"center",valign:"middle",sortable:"true"},'+
                '{field:"labelContene",title:"内容",align:"center",valign:"middle",sortable:"true"},'+
                '{field:"labelLogic",title:"逻辑",align:"center",valign:"middle",sortable:"true"},'+
                '{field:"labelCondition",title:"条件",align:"center",valign:"middle",sortable:"true"},'+
                ']';

                //加载数据
                loadTable(b,a,'#labelTable');
                }
            });
});

function getLabelTagType(){
    // 显示之前，先把当前表格销毁
    $('#labelTable').bootstrapTable('destroy');
    document.getElementById("id_select").options.length=0;
    document.getElementById("id_select").options.add(new Option("请选择标签"));
    pageInfo = {
        type:$('#id_type option:selected').text()
     };
     $.ajax({
        type: "POST",
        url: "../labeltaguser/labelTaglist",
        data: JSON.stringify(pageInfo),
        contentType: "application/json;charset=utf-8",
        success : function(msg) {
            console.log(msg);
            var obj=document.getElementById('id_select');
            for(var list in msg.labelTaglist){
                for(var key in msg.labelTaglist[list]){
                    if(key == "mainlabelName"){
                        obj.options.add(new Option(msg.labelTaglist[list][key]));
                    }
                }
            };
            }
        });
};

// 表格加载
function loadTable(columnsData,tableData,tableName){
    $(tableName).bootstrapTable({
        method: 'get',
        cache: false,
        height: tableName == "#labelTable" ? 400 : tableHeight(),
        pagination: true,
        pageSize: 20,
        pageNumber:1,
        pageList: [10, 20, 50, 100, 200, 500],
        // search: true,
        // showColumns: true,
        // showExport: true,
        clickToSelect: true,
        columns: eval("("+columnsData+")"),
        data :eval("("+tableData+")") ,
        formatNoMatches: function(){
            return '无符合条件的记录';
        }
    });
};

// 查询条件
var pageInfo = {
        page : 1,
        limit : 20
    };
$(function () {
	// 默认添加一行
	addTr();

});






