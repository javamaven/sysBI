function addTab(title, href, icon){
    var tt = $('#tabs');  
    if (tt.tabs('exists', title)){//如果tab已经存在,则选中并刷新该tab          
        tt.tabs('select', title);  
        refreshTab({tabTitle:title,url:href});  
    } else {  
    	 var winSize = tt.tabs('tabs');  
    	if(winSize && winSize.length > 7){
    		alert('最多只能打开8个窗口');
    		return;
    	}
        if (href){  
            var content = '<iframe scrolling="yes" frameborder="0"  src="'+href+'" style="width:100%;height:96%;margin-top:10px;margin-left:0px;overflow: hidden;"></iframe>';  
        } else {  
            var content = '未实现';  
        }  
        tt.tabs('add',{  
            title:title,  
            closable:true,  
            content:content
//            ,  
//            iconCls:icon||'icon-default'  
        });  
    }  
}  
/**     
 * 刷新tab 
 * @cfg  
 *example: {tabTitle:'tabTitle',url:'refreshUrl'} 
 *如果tabTitle为空，则默认刷新当前选中的tab 
 *如果url为空，则默认以原来的url进行reload 
 */  
function refreshTab(cfg){  
    var refresh_tab = cfg.tabTitle?$('#tabs').tabs('getTab',cfg.tabTitle):$('#tabs').tabs('getSelected');  
    if(refresh_tab && refresh_tab.find('iframe').length > 0){
    	var _refresh_ifram = refresh_tab.find('iframe')[0];  
    	var refresh_url = cfg.url?cfg.url:_refresh_ifram.src;  
    	_refresh_ifram.contentWindow.location.href=refresh_url;  
    }  
}



//生成菜单
var menuItem = Vue.extend({
	name: 'menu-item',
	props:{item:{}},
	template:[
	          '<li>',
	          '<a v-if="item.type === 0" href="javascript:;">',
	          '<i v-if="item.icon != null" :class="item.icon"></i>',
	          '<span>{{item.name}}</span>',
	          '<i class="fa fa-angle-left pull-right"></i>',
	          '</a>',
	          '<ul v-if="item.type === 0" class="treeview-menu">',
	          '<menu-item :item="item" v-for="item in item.list"></menu-item>',
	          '</ul>',
	          '<a v-if="item.type === 1" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
	          '</li>'
	].join('')
});

//iframe自适应
$(window).on('resize', function() {
	var $content = $('.content');
	$content.height($(this).height() - 120);
	$content.find('iframe').each(function() {
		$(this).height($content.height());
	});
}).resize();

//注册菜单组件
Vue.component('menuItem',menuItem);

var vm = new Vue({
	el:'#rrapp',
	data:{
		user:{},
		menuList:{},
		main:"sys/main.html",
		password:'',
		newPassword:'',
        navTitle:"控制台"
	},
	methods: {
		getMenuList: function (event) {
			$.getJSON("sys/menu/user?_"+$.now(), function(r){
				vm.menuList = r.menuList;
			});
		},
		getUser: function(){
			$.getJSON("sys/user/info?_"+$.now(), function(r){
				vm.user = r.user;
			});
		},
		updatePassword: function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "修改密码",
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#passwordLayer"),
				btn: ['修改','取消'],
				btn1: function (index) {
					var data = "password="+vm.password+"&newPassword="+vm.newPassword;
					$.ajax({
						type: "POST",
					    url: "sys/user/password",
					    data: data,
					    dataType: "json",
					    success: function(result){
							if(result.code == 0){
								layer.close(index);
								layer.alert('修改成功', function(index){
									location.reload();
								});
							}else{
								layer.alert(result.msg);
							}
						}
					});
	            }
			});
		}
	},
	created: function(){
		this.getMenuList();
		this.getUser();
	},
	updated: function(){
		//路由
		var router = new Router();
		routerList(router, vm.menuList);
		router.start();
	}
});



function routerList(router, menuList){
	for(var key in menuList){
		var menu = menuList[key];
		
		if(menu.type == 0){
			routerList(router, menu.list);
		}else if(menu.type == 1){
			router.add('#'+menu.url, function() {
				var url = window.location.hash;
				var menuObj = $("a[href='"+url+"']");
				var menuName = $("a[href='"+url+"']:first").text()
				addTab(menuName, url.replace('#', ''));
				//替换iframe的url
			    vm.main = url.replace('#', '');
			    
			    //导航菜单展开
			    $(".treeview-menu li").removeClass("active");
			    $("a[href='"+url+"']").parents("li").addClass("active");
			    
			    vm.navTitle = $("a[href='"+url+"']").text();
			});
		}
	}
}
