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