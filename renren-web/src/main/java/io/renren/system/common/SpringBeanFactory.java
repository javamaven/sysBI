package io.renren.system.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * 通过ApplicationContextFactory创建ApplicationContext，然后创建SpringBeanFactory并设置为单例模式
 * 
 */
@Component
public class SpringBeanFactory implements ApplicationContextAware {

	private static SpringBeanFactory singleton = new SpringBeanFactory();

	private static Logger logger = LoggerFactory.getLogger(SpringBeanFactory.class);

	public static SpringBeanFactory getSingleton() {
		return singleton;
	}

	private static ApplicationContext applicationContext = null;

	/**
	 * 提供给spring通过注解的方式自动创建
	 * 
	 * @Deprecated 该方法仅供spring调用
	 */
	public SpringBeanFactory() {

	}

	public ApplicationContext getApplicationContext() {
		return SpringBeanFactory.applicationContext;
	}

	/**
	 * ApplicationContextAware自动注入
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringBeanFactory.applicationContext = applicationContext;

		singleton = this;
	}

	/**
	 * 提供通过手动设置
	 * 
	 * @param applicationContext
	 */
	public SpringBeanFactory(ApplicationContext applicationContext) {
		setApplicationContext(applicationContext);
	}

	public static Object getBean(String name) {
		if (name == null || "".equals(name)) {
			return null;
		}
		Object bean = null;
		try {
			bean = applicationContext.getBean(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	public static <T> T getBean(Class<T> requiredType) {
		if (requiredType == null)
			return null;

		T bean = null;
		try {
			bean = applicationContext.getBean(requiredType);
		} catch (Exception e) {
			logger.warn("无法根据beanClass从springBeanFactory中获取bean：" + requiredType.getName());
		}
		return bean;
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		return applicationContext.getBean(beanName, clazz);
	}

	// @Override
	// public boolean containsBean(String name) {
	// return applicationContext.containsBean(name);
	// }
	//
	// @Override
	// public <T> T getBean(String name, Class<T> requiredType) {
	// if (StringUtil.isEmpty(name) || requiredType == null)
	// return null;
	//
	// return applicationContext.getBean(name, requiredType);
	// }

}
