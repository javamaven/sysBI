package io.renren.system.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import io.renren.service.schedule.JobService;

//创建的类名根据需要定义，但一定要实现ServletContextListener接口 
public class WebContextListener implements ServletContextListener {
	private ServletContext context;

	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

	@Override
	public void contextDestroyed(ServletContextEvent ctx) {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextInitialized(ServletContextEvent ctx) {
		// TODO Auto-generated method stub
		System.err.println("++++++++监听器+++++++++++");
		context = ctx.getServletContext();
		try {
			ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(context);
			JobService jobService = app.getBean(JobService.class);
			jobService.initJobs();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// JobService jobTaskService = SpringBeanFactory.getBean("jobService",
		// JobService.class);

	}

}
