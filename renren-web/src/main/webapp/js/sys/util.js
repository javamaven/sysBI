//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { // author: meizz
	var o = {
	   "M+": this.getMonth() + 1, // 月份
	   "d+": this.getDate(), // 日
	   "h+": this.getHours(), // 小时
	   "m+": this.getMinutes(), // 分
	   "s+": this.getSeconds(), // 秒
	   "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
	   "S": this.getMilliseconds() // 毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
	if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

/**
 * 使用post提交表单
 * 
 * @param URL
 * @param PARAMS
 * @returns
 */
function executePost(URL, PARAMS) {        
    var temp = document.createElement("form");        
    temp.action = URL;        
    temp.method = "post";        
    temp.style.display = "none";        
    for (var x in PARAMS) {        
        var opt = document.createElement("textarea");        
        opt.name = x;        
        opt.value = PARAMS[x];        
        temp.appendChild(opt);        
    }        
    document.body.appendChild(temp);        
    temp.submit();        
    return temp;        
}   


function addDate(date,days){
	var a = new Date(date);
	a = a.valueOf();
	a = a + days * 24 * 60 * 60 * 1000;
	a = new Date(a);
	return a.Format("yyyy-MM-dd");
}

function addHours(hours){
	var a = new Date();
	a = a.valueOf();
	a = a + hours * 60 * 60 * 1000;
	a = new Date(a);
	return a.Format("yyyy-MM-dd hh");
}

//当天
function getCurrDate(){
	var time1 = new Date().Format("yyyy-MM-dd");
	return time1;
}

//昨天
function getYesterday(){
	var time1 = new Date();
	return addDate(time1, -1);
}

/**
 * 将数值四舍五入后格式化.
 *
 * @param num 数值(Number或者String)
 * @param cent 要保留的小数位(Number)
 * @param isThousand 是否需要千分位 0:不需要,1:需要(数值类型);
 * @return 格式的字符串,如'1,234,567.45'
 * @type String
 */
function formatNumber(num,cent) {
	if(!num){
		return '';
	}
	var num = num.toString().replace(/\$|\,/g,'');
	 var isThousand = 1;
	 // 检查传入数值为数值类型
	  if(isNaN(num))
	   num = "0";
	
	 // 获取符号(正/负数)
	 sign = (num == (num = Math.abs(num)));
	
	 num = Math.floor(num*Math.pow(10,cent)+0.50000000001); // 把指定的小数位先转换成整数.多余的小数位四舍五入
	 cents = num%Math.pow(10,cent);       // 求出小数位数值
	 num = Math.floor(num/Math.pow(10,cent)).toString();  // 求出整数位数值
	 cents = cents.toString();        // 把小数位转换成字符串,以便求小数位长度
	
	 // 补足小数位到指定的位数
	 while(cents.length<cent)
	  cents = "0" + cents;
	
	 if(isThousand) {
	  // 对整数部分进行千分位格式化.
	  for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
	   num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));
	 }
	
	 if (cent > 0){
		 if(cents == '00'){
			 return (((sign)?'':'-') + num);
		 }
		 return (((sign)?'':'-') + num + '.' + cents);
	 }else{
		 return (((sign)?'':'-') + num);
	 }
}

/**
 * json格式化
 * @param txt
 * @param compress
 * @returns
 */
//function jsonFormat(txt,compress/*是否为压缩模式*/){/* 格式化JSON源码(对象转换为JSON文本) */  
function jsonFormat(txt){
	var compress = false;
    var indentChar = '    ';   
    if(/^\s*$/.test(txt)){   
        alert('数据为空,无法格式化! ');   
        return;   
    }   
    try{var data=eval('('+txt+')');}   
    catch(e){   
        alert('数据源语法错误,格式化失败! 错误信息: '+e.description,'err');   
        return;   
    };   
    var draw=[],last=false,This=this,line=compress?'':'\n',nodeCount=0,maxDepth=0;   
       
    var notify=function(name,value,isLast,indent/*缩进*/,formObj){   
        nodeCount++;/*节点计数*/  
        for (var i=0,tab='';i<indent;i++ )tab+=indentChar;/* 缩进HTML */  
        tab=compress?'':tab;/*压缩模式忽略缩进*/  
        maxDepth=++indent;/*缩进递增并记录*/  
        if(value&&value.constructor==Array){/*处理数组*/  
            draw.push(tab+(formObj?('"'+name+'":'):'')+'['+line);/*缩进'[' 然后换行*/  
            for (var i=0;i<value.length;i++)   
                notify(i,value[i],i==value.length-1,indent,false);   
            draw.push(tab+']'+(isLast?line:(','+line)));/*缩进']'换行,若非尾元素则添加逗号*/  
        }else   if(value&&typeof value=='object'){/*处理对象*/  
                draw.push(tab+(formObj?('"'+name+'":'):'')+'{'+line);/*缩进'{' 然后换行*/  
                var len=0,i=0;   
                for(var key in value)len++;   
                for(var key in value)notify(key,value[key],++i==len,indent,true);   
                draw.push(tab+'}'+(isLast?line:(','+line)));/*缩进'}'换行,若非尾元素则添加逗号*/  
            }else{   
                    if(typeof value=='string')value='"'+value+'"';   
                    draw.push(tab+(formObj?('"'+name+'":'):'')+value+(isLast?'':',')+line);   
            };   
    };   
    var isLast=true,indent=0;   
    notify('',data,isLast,indent,false);   
    return draw.join('');   
}  

/**
 * 是否是邮箱
 * @param str
 * @returns
 */
function isEmail(str){
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
	return reg.test(str);
} 



/*
 * 显示loading遮罩层
 */
function loading() {
	var dddd = document.createElement("div");
	dddd.id = "mask_div";
	dddd.style.width = "100%";
	dddd.style.height = "100%";
    var mask_bg = document.createElement("div");
    mask_bg.id = "mask_bg";
    mask_bg.style.position = "absolute";
    mask_bg.style.top = "0px";
    mask_bg.style.left = "0px";
    mask_bg.style.width = "100%";
    mask_bg.style.height = "100%";
    mask_bg.style.backgroundColor = "black";
    mask_bg.style.opacity = 0.03;
    mask_bg.style.zIndex = 8888;
 
    document.body.appendChild(dddd);
    dddd.appendChild(mask_bg);
    
    
    var mask_msg = document.createElement("img")
//    <img alt="" style="width: 10%;height: 10%" src="${rc.contextPath}/statics/picture/xd-3.gif">
     mask_msg.src = ctx + "/statics/picture/xd-3.gif";
//    var mask_msg = document.createElement("div");
    mask_msg.style.position = "absolute";
    mask_msg.style.width = "15%";
    mask_msg.style.height = "25%";
    mask_msg.style.top = "25%";
    mask_msg.style.left = "40%";
    mask_msg.style.filter = "alpha(opacity=60)";
//    mask_msg.style.backgroundColor = "white";
//    mask_msg.style.border = "#777 1px solid";
//    mask_msg.style.textAlign = "center";
//    mask_msg.style.fontSize = "1.1em";
//    mask_msg.style.fontWeight = "bold";
//    mask_msg.style.padding = "0.5em 3em 0.5em 3em";
    mask_msg.style.zIndex = 9999;
//    mask_msg.innerText = "正在执行,请稍后...";
    
    dddd.appendChild(mask_msg);
}
/*
 * 关闭遮罩层
 */
function loaded() {
    var mask_bg = document.getElementById("mask_div");
    if (mask_bg != null){
        mask_bg.parentNode.removeChild(mask_bg);
    }
}
/**
 * 是否flat值
 * @returns
 */
function isIntOrDoubleValue(value){
	var arr = [0,1,2,3,4,5,6,7,8,9];
	for (var i = 0; i < value.length; i++) {
		var cc = value.charAt(i);
		if(cc == '.'){
			continue;
		}
		var isNumber = false;
		for (var j = 0; j < arr.length; j++) {
			if(cc == arr[j]){
				isNumber = true;
			}
		}
		if(!isNumber){
			return false;
		}
	}
	return true;
}

