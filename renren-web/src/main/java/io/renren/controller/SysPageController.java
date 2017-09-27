package io.renren.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统页面视图
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月24日 下午11:05:27
 */
@Controller
public class SysPageController {
	
	@RequestMapping("sys/{url}.html")
	public String page(@PathVariable("url") String url){
		return "sys/" + url + ".html";
	}

	@RequestMapping("generator/{url}.html")
	public String generator(@PathVariable("url") String url){
		return "generator/" + url + ".html";
	}

	@RequestMapping("channel/{url}.html")
	public String channel(@PathVariable("url") String url){
		return "channel/" + url + ".html";
	}
	
	@RequestMapping("schedule/{url}.html")
	public String schedule(@PathVariable("url") String url){
		return "schedule/" + url + ".html";
	}

	@RequestMapping("labelTag/{url}.html")
	public String labelTag(@PathVariable("url") String url){
		return "labelTag/" + url + ".html";
	}
	
	@RequestMapping("yunying/dayreport/{url}.html")
	public String yunyingDayReport(@PathVariable("url") String url){
		return "yunying/dayreport/" + url + ".html";
	}
	
	@RequestMapping("yunying/basicreport/{url}.html")
	public String yunyingBasicReport(@PathVariable("url") String url){
		return "yunying/basicreport/" + url + ".html";
	}
	
	@RequestMapping("yunying/zixiao/{url}.html")
	public String yunyingZixiao(@PathVariable("url") String url){
		return "yunying/zixiao/" + url + ".html";
	}
	
	@RequestMapping("yunying/analyse/{url}.html")
	public String yunyingAnalyse(@PathVariable("url") String url){
		return "yunying/analyse/" + url + ".html";
	}
	
	@RequestMapping("yunying/yunyingtool/{url}.html")
	public String yunyingTool(@PathVariable("url") String url){
		return "yunying/yunyingtool/" + url + ".html";
	}
	
	@RequestMapping("/yunying/licaijihua/{url}.html")
	public String yunyingLicaijihua(@PathVariable("url") String url){
		return "yunying/" + url + ".html";
	}
	
	@RequestMapping("yunying/{url}.html")
	public String yunying(@PathVariable("url") String url){
		return "yunying/" + url + ".html";
	}
	
	@RequestMapping("hr/{url}.html")
	public String hr(@PathVariable("url") String url){
		return "hr/" + url + ".html";
	}


	@RequestMapping("property/{url}.html")
	public String property(@PathVariable("url") String url){
		return "property/" + url + ".html";
	}
	
	@RequestMapping("shichang/{url}.html")
	public String shichang(@PathVariable("url") String url){
		return "shichang/" + url + ".html";
	}

}
