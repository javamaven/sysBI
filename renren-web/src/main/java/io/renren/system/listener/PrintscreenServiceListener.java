package io.renren.system.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import io.renren.system.common.ConfigProp;

/**
 * 启动、关闭截图服务的监听器
 * 
 * @author liuchong
 * @data 2016/4/11 11:24
 */
public class PrintscreenServiceListener implements ServletContextListener {
	private static final Logger LOG = Logger.getLogger(PrintscreenServiceListener.class);
	private static Process process;
	/**
	 * 生成图表的请求URL
	 */
	public static String REQUEST_PRINT_SCREEN_CHART_URL = "";
	/**
	 * 请求截图的URL
	 */
	public static String REQUEST_PRINT_SCREEN_URL = "";

	/**
	 * 后台截图服务执行文件绝对路径
	 */
	public static String PRINTSCREEN_SERVICE_FILE_PATH = "";

	/**
	 * 后台截图服务执行文件执行的脚本文件绝对路径
	 */
	public static String PRINTSCREEN_SERVICE_SCRIPT_FILE_PATH = "";

	/**
	 * 图片存放目录
	 */
	public static String IMAGE_FILE_STORE_DIR;

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		initParam(servletContextEvent.getServletContext());
		Thread thread = new Thread(new MyServiceRunnable());
		thread.start();

		// //定时删除截图保存的文件
		// JobService jobTaskService = SpringBeanFactory.getBean("jobService",
		// JobService.class);
		// JobVo jobVo = new JobVo();
		// jobVo.setJobId(UUID.randomUUID().toString());
		// jobVo.setJobStatus(JobVo.STATUS_RUNNING);
		// jobVo.setJobName("删除保存的截图文件定时任务-name");
		// jobVo.setJobGroup("删除保存的截图文件定时任务-group");
		// //任务运行时间表达式,每天凌晨1点执行
		// jobVo.setCronExpression("0 0 1 * * ?");
		// //jobVo.setCronExpression("0 */1 * * * ?");
		// try {
		// jobTaskService.addJob(jobVo,TimerDeleteImgJob.class);
		// } catch (SchedulerException e) {
		// LOG.error("添加定时删除截图保存的文件失败！！！！！",e);
		// //e.printStackTrace();
		// }
	}

	/**
	 * 初始化参数
	 **/
	private void initParam(ServletContext servletContext) {
		String webPath = servletContext.getRealPath(File.separator);
		// File webPathFile = new File(webPath);
		// web项目目录
		// File webFile = classPathFile.getParentFile().getParentFile();
		// 后台截图服务执行文件绝对路径
		// /statics/plugins/printscreen/window_phantomjs.exe
		PRINTSCREEN_SERVICE_FILE_PATH = webPath + "statics" + File.separator + "plugins" + File.separator
				+ "printscreen" + File.separator + getExecuteFileName();
		// 后台截图服务执行文件执行的脚本文件绝对路径
		PRINTSCREEN_SERVICE_SCRIPT_FILE_PATH = webPath + "statics" + File.separator + "plugins" + File.separator
				+ "printscreen" + File.separator + "simpleserver.js";

		IMAGE_FILE_STORE_DIR = webPath + "temp";
		// 存放图片的临时目录是否存在
		File dirFile = new File(IMAGE_FILE_STORE_DIR);
		if (!io.renren.util.FileUtil.dirExists(dirFile)) {
			LOG.info(String.format("创建截图文件保存的临时目录：%s", IMAGE_FILE_STORE_DIR));
			dirFile.mkdir();
		}
		REQUEST_PRINT_SCREEN_CHART_URL = ConfigProp.getPrintscreenServiceRequestChartUrl();
		REQUEST_PRINT_SCREEN_URL = ConfigProp.getPrintscreenServiceURL();
	}

	/**
	 * 获取执行phantomjs执行文件名称
	 * 
	 * @return
	 */
	private String getExecuteFileName() {
		if (isWindow()) {
			return "window_phantomjs.exe";
		} else {
			if (is_32()) {
				return "linux_32_phantomjs";
			}
		}
		return "linux_64_phantomjs";
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		if (process != null) {
			LOG.info("关闭截图服务……");
			process.destroy();
		}
	}

	// /**
	// * 定时删除截图文件类
	// ***/
	// public static class TimerDeleteImgJob implements Job{
	//
	// @Override
	// public void execute(JobExecutionContext jobExecutionContext) throws
	// JobExecutionException {
	// //String dir = ConfigProp.getPrintscreenDir();
	// File file = new File(IMAGE_FILE_STORE_DIR);
	// if(!file.exists() || !file.isDirectory()){
	// return;
	// }
	// File[] childs = file.listFiles(new FileFilter(){
	// @Override
	// public boolean accept(File pathname) {
	// if(pathname.isFile()){
	// //文件后缀
	// String s = FileUtil.getFileType(pathname);
	// if("png".equalsIgnoreCase(s)){
	// return true;
	// }
	// }
	// return false;
	// }
	// });
	// for (File child : childs) {
	// LOG.info(String.format("定时任务：删除截图文件,文件名称：%s",child.getName()));
	// child.delete();
	// }
	// }
	// }

	/**
	 * 是否为window操作系统
	 * 
	 * @return
	 */
	private boolean isWindow() {
		return SystemUtils.IS_OS_WINDOWS;
	}

	/**
	 * 是否为linux操作系统
	 * 
	 * @return
	 */
	private boolean isLinux() {
		return SystemUtils.IS_OS_LINUX;
	}

	/**
	 * 是否为linux 32位操作系统
	 * 
	 * @return
	 */
	private boolean isLinux_32() {
		if (isLinux()) {
			return is_32();
		}
		return false;
	}

	/**
	 * 是否为linux 64位操作系统
	 * 
	 * @return
	 */
	private boolean isLinux_64() {
		if (isLinux()) {
			return is_64();
		}
		return false;
	}

	/**
	 * 操作系统的指令版本
	 * 
	 * @return
	 */
	private String getOSArch() {
		return SystemUtils.OS_ARCH;
	}

	/**
	 * 是否为 64位版本
	 * 
	 * @return
	 */
	private boolean is_64() {
		String version = getOSArch();
		if (version.indexOf("64") != -1) {
			return true;
		}
		return false;
	}

	/**
	 * 是否为 32位版本
	 * 
	 * @return
	 */
	private boolean is_32() {
		if (!is_64()) {
			return true;
		}
		return false;
	}

	/**
	 * 启动截图服务线程
	 **/
	class MyServiceRunnable implements Runnable {

		@Override
		public void run() {
			LOG.info("开始启动截图服务……");
			Runtime runtime = Runtime.getRuntime();
			LOG.info("截图服务启动完成……");
			InputStream stderr = null;
			InputStreamReader isr = null;
			BufferedReader br = null;
			Process chmodProcess = null;
			try {
				if (isLinux()) {
					chmodProcess = runtime.exec("chmod 777 " + PRINTSCREEN_SERVICE_FILE_PATH);
					chmodProcess.waitFor();
				}

				// LOG.info(ConfigProp.getPrintscreenServiceFile() + " " +
				// ConfigProp.getPrintscreenServiceScriptFile() + " " +
				// ConfigProp.getPrintscreenServicePort() );
				// process = runtime.exec(ConfigProp.getPrintscreenServiceFile()
				// + " " + ConfigProp.getPrintscreenServiceScriptFile() + " " +
				// ConfigProp.getPrintscreenServicePort() );
				LOG.info(String.format("截图服务执行文件路径：%s,截图服务执行脚本文件路径：%s,截图服务监听端口：%s", PRINTSCREEN_SERVICE_FILE_PATH,
						PRINTSCREEN_SERVICE_SCRIPT_FILE_PATH, ConfigProp.getPrintscreenServicePort()));
				String cmd = PRINTSCREEN_SERVICE_FILE_PATH + " " + PRINTSCREEN_SERVICE_SCRIPT_FILE_PATH + " "
						+ ConfigProp.getPrintscreenServicePort();
				process = runtime.exec(cmd);
				LOG.info("======启动命令====>" + cmd);
				stderr = process.getInputStream();
				isr = new InputStreamReader(stderr);
				br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) {
					LOG.info(String.format("截图服务输出信息：%s", line));
				}
				int exitVal = process.waitFor();
				LOG.info("截图服务启动完成……，返回码:" + exitVal);
				// LOG.info("截图服务启动完成……");
			} catch (IOException e) {
				LOG.error("启动截图服务失败！！！！", e);
			} catch (InterruptedException e) {
				LOG.error("启动截图服务失败！！！！", e);
			} finally {
				if (chmodProcess != null) {
					chmodProcess.destroy();
				}
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						LOG.error("关闭I/O流异常", e);
					}
				}
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						LOG.error("关闭I/O流异常", e);
					}
				}
				if (isr != null) {
					try {
						isr.close();
					} catch (IOException e) {
						LOG.error("关闭I/O流异常", e);
					}
				}
			}
		}
	}
}
