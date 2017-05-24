/**
 *  截图服务执行脚本
 *  by liuchong 2016-04-21
 */

"use strict";
phantom.outputEncoding="gb2312";
var port, server, service,
    system = require('system');

if (system.args.length !== 2) {
    console.log('Usage: simpleserver.js <portnumber>');
    phantom.exit(1);
} else {
    //端口
    port = system.args[1];
    //web 服务
    server = require('webserver').create();

    /**
     ** Object对象转换为String
     **/
    var postBody = function(obj){
        if(!obj){
            return '';
        }
        var result = '';
        for(var key in obj){
            if(key != 'fileName' && key != 'callback'){
                result += '&'+key+'='+obj[key];
            }
        }
        result = result.substr(result.indexOf('&')+1);
        return result;
    };
    /**
     **把post请求参数转换为Object对象
     **/
    var buildParam = function(post){
        if(!post){
            return;
        }
        post = decodeURIComponent(post);
        var params = post.split("&");
        var obj = {};
        for(var i = 0; i < params.length; i++){
            if(params[i]){
                var temp = [];
                //此处勿用split(),结果中可能包含'='
                var index = params[i].indexOf('=');
                temp[0] = params[i].substring(0,index);
                temp[1] = params[i].substring(index+1,params[i].length);
                obj[temp[0]] = temp[1]
            }
        }
        return obj;
    };


    //启动监听
    service = server.listen(port, function (request, response) {
        if(request.post){
            //构造参数
            var param = buildParam(request.post);
            var settings = {
                operation: "POST",
                encoding: "utf8",
                resourceTimeout: 5000,
                data: postBody(param)
            };
            var page = require('webpage').create();
//            page.settings.resourceTimeout = 15000; // 5 seconds
            console.info('回调地址:'+param.callback);
            //打开回调的URL
            // 打开课程对应的淘宝商品详情页。
            page.onConsoleMessage = function(msg, lineNum, sourceId) {
            	  console.log('CONSOLE: ' + msg + " +++++++++lineNum=" + lineNum);
            	};
            page.open(param.callback,settings, function(status) {
                if(status !== "success") {
                    response.write('{success:false}');
                }
                // 由于页面中的资源是动态加载的，需要setTimeout 10s 等待资源加载完，再操作页面。
                setTimeout(function() {
                    page.render(param.fileName);
                    console.log("+++++++++++++++render end++++", param.fileName);
                    var params = {};
                    params.success = true;
                    params.file = param.fileName;
                    response.write( JSON.stringify(params));
                    
                    page.close();
                    response.close();
                }, 15000);
             
            });
            
            
          /*  page.open(param.callback, function (status) {
                if(status == 'success'){
                	
                	setTimeout(function() {
//                        var apply = page.evaluate(function() {
                        	page.render(param.fileName, {format: 'png', quality: '100'});
//                        });
//                         console.log("apply:", apply);
//                        phantom.exit();
                    }, 10000);
                	
//                	 window.setTimeout(function() {
//                	        page.render(param.fileName);
//                	    }, 15000); 
                    // 由于页面中的资源是动态加载的，需要setTimeout 10s 等待资源加载完，再操作页面。
//                    setTimeout(function() {
//                        page.render(param.fileName);
//                    }, 10000);
                    //截图
//                    page.render(param.fileName);
                    //发送信息给截图服务
//                    response.write('{success:true,file:"'+param.fileName+'"}');
                    var params = {};
                    params.success = true;
                    params.file = param.fileName;
                    response.write( JSON.stringify(params));
                   
                }else{
                    response.write('{success:false}');
                }
                page.close();
                response.close();
            });*/
            var index = 0;
            page.onResourceReceived = function(response) {
//            	console.info("+++++++++onResourceReceived++++++++"  + "   index=" + index++)
                /*if(mItem = response.url.match(/^http\:\/\/(?:.*)[?|&]item=(\d*)/)) {
                    itemId = mItem[1];
                    console.log(itemId);
                    phantom.exit();
                }*/
                // 获取课程对应的淘宝网商品id
//                if(mItem = response.url.match(/itemId=(\d*)/)) {
//                    itemId = parseInt(mItem[1]);
//                }
            };
            page.onCallback = function(data) {
//            	console.log('CALLBACK: ' + JSON.stringify(data));
            	// Prints 'CALLBACK: { "hello": "world" }'
            };
            page.onPageCreated = function(newPage) {
//            	  console.log('A new child page was created! Its requested URL is not yet available, though.');
//            	  // Decorate
//            	  newPage.onClosing = function(closingPage) {
//            	    console.log('A child page is closing: ' + closingPage.url);
//            	  };
            };
        	page.onResourceTimeout = function(request) {
        	    console.log('Response (#' + request.id + '): ' + JSON.stringify(request));
        	};
            page.onLoadFinished = function() {
                console.log("Page load finished");
                // page.evaluate(function() {});
//                page.render("a.png");
//                var content = page.content;
//                console.log('Content: ' + param.fileName);
            };
        }

    });

    if (service) {
        console.log('截图服务端口: ' + port);
    } else {
        console.log('创建截图服备失败，端口不可用，端口号：' + port);
        phantom.exit();
    }
}
