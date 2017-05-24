package io.renren.service.schedule.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import io.renren.system.listener.PrintscreenServiceListener;

/**
 * 生成图片图片,单例对象，请使用spring ioc注入，勿手动创建此例对象
 * 
 * @data 2016/4/9 14:47
 */
@Component()
@Scope("singleton")
public class CreateChartImage {
	private static final Logger LOG = Logger.getLogger(CreateChartImage.class);
	private static HttpClient httpClient = new HttpClient();
	static {
		httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, 300000); // 超时设置,5分钟超时
		httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 300000);// 连接超时
	}

	/**
	 * 生成一个图片文件名称，格式为.png
	 * 
	 * @return
	 */
	public static String getFileName() {
		return System.currentTimeMillis() + ".png";//jpeg
	}

	/**
	 * 渠道分次投资-截图
	 * 
	 * @param reportId
	 * @param exportFile
	 * @return
	 */
	public boolean createChannelAllPicture(String fileName, String paramsJson) {
		return create(getChannelAllPostMethod(fileName, paramsJson));
	}

	private HttpMethod getChannelAllPostMethod(String fileName, String paramsJson) {
		PostMethod method = new PostMethod(PrintscreenServiceListener.REQUEST_PRINT_SCREEN_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair[] parameters = new NameValuePair[] { 
				new NameValuePair("fileName", fileName),
				new NameValuePair("params", paramsJson),
				new NameValuePair("callback",
						PrintscreenServiceListener.REQUEST_PRINT_SCREEN_CHART_URL + "chart-report-id"),
				new NameValuePair("type", "channel_all") 
				};
		method.setRequestBody(parameters);
		method.releaseConnection();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.addRequestHeader("Content-Type", "text/html;charset=UTF-8");
		method.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		return method;
	}

	/**
	 * 根据报表id生成图片
	 * 
	 * @param reportId
	 *            报表id
	 * @param exportFile
	 *            生成的图片路径
	 * @return true:成功生成图片,false:生成一张空白图片
	 */
	public boolean createByReportId(Long reportId, String exportFile) {
		return create(getPostMethodByReportId(reportId, exportFile));
	}

	/**
	 * 根据仪表盘fieldid生成图片
	 * 
	 * @param fieldId
	 *            仪表盘fieldid
	 * @param exportFile
	 *            生成的图片路径
	 * @return true:成功生成图片,false:生成一张空白图片
	 */
	public boolean createByDashboardFieldsId(Long fieldId, String execTimeRange, String exportFile) {
		return create(getPostMethodByDashboardFieldId(fieldId, execTimeRange, exportFile));
	}

	/**
	 * 根据报表配置生成图片
	 * 
	 * @param reportText
	 * @param reportConfig
	 * @param reportType
	 * @param reportFilter
	 * @param execTime
	 * @param logTag
	 * @param fileName
	 * @return
	 */
	public boolean createByConfig(String reportName, String reportText, String reportConfig, String reportType,
			String reportFilter, String execTime, String logTag, String fileName) {
		// LOG.info(String.format("导出图表参数----------------------------------------------\n搜索条件：%s,\n配置：%s,\n图表类型：%s,\n,过滤设置:%s,\n,时间:%s,\n,索引：%s,\n文件路径：%s",reportText,reportConfig,reportType,reportFilter,execTime,logTag,fileName));
		return create(getPostMethodByConfig(reportName, reportText, reportConfig, reportType, reportFilter, execTime,
				logTag, fileName));
	}

	public boolean createByConfig(int x, int y, String reportName, String reportText, String reportConfig,
			String reportType, String reportFilter, String execTime, String logTag, String fileName) {
		// LOG.info(String.format("导出图表参数----------------------------------------------\n搜索条件：%s,\n配置：%s,\n图表类型：%s,\n,过滤设置:%s,\n,时间:%s,\n,索引：%s,\n文件路径：%s",reportText,reportConfig,reportType,reportFilter,execTime,logTag,fileName));
		return create(getPostMethodByConfig(x, y, reportName, reportText, reportConfig, reportType, reportFilter,
				execTime, logTag, fileName));
	}

	/**
	 * 获取post method对象
	 * 
	 * @param reportId
	 * @param fileName
	 * @return
	 */
	private PostMethod getPostMethodByReportId(Long reportId, String fileName) {
		// LogUser currentLoginUser = null;
		// try {
		// Subject subject = SecurityUtils.getSubject();
		// currentLoginUser = (LogUser)
		// subject.getSession().getAttribute("user");
		// currentLoginUser.getLogRole().setLogHtmlRoleList(null);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		PostMethod method = new PostMethod(PrintscreenServiceListener.REQUEST_PRINT_SCREEN_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("fileName", fileName),
				new NameValuePair("callback",
						PrintscreenServiceListener.REQUEST_PRINT_SCREEN_CHART_URL + "chart-report-id.html"),
				new NameValuePair("reportId", reportId + "") };
		method.setRequestBody(parameters);
		method.releaseConnection();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.addRequestHeader("Content-Type", "text/html;charset=UTF-8");
		method.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		return method;
	}

	private PostMethod getPostMethodByDashboardFieldId(Long fieldId, String execTimeRange, String fileName) {

		PostMethod method = new PostMethod(PrintscreenServiceListener.REQUEST_PRINT_SCREEN_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("fileName", fileName),
				new NameValuePair("callback",
						// "http://localhost:8080/bi_sys/index.html#channel/channelAll.html"),
						PrintscreenServiceListener.REQUEST_PRINT_SCREEN_CHART_URL + "chart-report-id"),
				new NameValuePair("fieldId", fieldId + ""), new NameValuePair("execTimeRange", execTimeRange) };
		method.setRequestBody(parameters);
		method.releaseConnection();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.addRequestHeader("Content-Type", "text/html;charset=UTF-8");
		method.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		return method;
	}

	/**
	 * 获取post method对象
	 * 
	 * @param reportText
	 * @param reportConfig
	 * @param reportType
	 * @param reportFilter
	 * @param execTime
	 * @param logTag
	 * @return
	 */
	private PostMethod getPostMethodByConfig(String reportName, String reportText, String reportConfig,
			String reportType, String reportFilter, String execTime, String logTag, String fileName) {
		PostMethod method = new PostMethod(PrintscreenServiceListener.REQUEST_PRINT_SCREEN_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("fileName", fileName),
				new NameValuePair("callback",
						PrintscreenServiceListener.REQUEST_PRINT_SCREEN_CHART_URL + "chart-report-config.html"),
				new NameValuePair("reportName", reportName), new NameValuePair("reportText", reportText),
				new NameValuePair("reportConfig", reportConfig.replaceAll("&", "@@@")),
				new NameValuePair("reportType", reportType), new NameValuePair("reportFilter", reportFilter),
				new NameValuePair("execTime", execTime), new NameValuePair("logTag", logTag) };
		method.setRequestBody(parameters);
		method.releaseConnection();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.addRequestHeader("Content-Type", "text/html;charset=UTF-8");
		method.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		return method;
	}

	private PostMethod getPostMethodByConfig(int x, int y, String reportName, String reportText, String reportConfig,
			String reportType, String reportFilter, String execTime, String logTag, String fileName) {
		PostMethod method = new PostMethod(PrintscreenServiceListener.REQUEST_PRINT_SCREEN_URL);
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("fileName", fileName),
				new NameValuePair("callback",
						PrintscreenServiceListener.REQUEST_PRINT_SCREEN_CHART_URL + "chart-report-config.html"),
				new NameValuePair("reportText", reportText),
				new NameValuePair("reportConfig", reportConfig.replaceAll("&", "@@@")),
				new NameValuePair("reportType", reportType), new NameValuePair("reportFilter", reportFilter),
				new NameValuePair("execTime", execTime), new NameValuePair("logTag", logTag),
				new NameValuePair("reportName", reportName), new NameValuePair("width", x + ""),
				new NameValuePair("height", y + "") };
		method.setRequestBody(parameters);
		method.releaseConnection();
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		method.addRequestHeader("Content-Type", "text/html;charset=UTF-8");
		method.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		return method;
	}

	/**
	 * 生成图片，同步方法
	 * 
	 * @param httpMethod
	 * @return true:成功生成图片,false:生成一张空白图片
	 */
	private boolean create(HttpMethod httpMethod) {
		if (httpMethod == null) {
			throw new IllegalArgumentException();
		}
		synchronized (httpClient) {
			boolean flag = false;
			try {
				int code = httpClient.executeMethod(httpMethod);
				if (code == HttpStatus.SC_OK) {
					String str = httpMethod.getResponseBodyAsString();
					LOG.info("截图返回：" + str);
					Gson gson = new Gson();
					Flag result = gson.fromJson(str, Flag.class);
					// Flag result = JSON.parseObject(str, Flag.class);
					flag = result.isSuccess();
					if (result.isSuccess()) {
						LOG.info("截图成功：" + str + ",文件：" + result.getFile());
					} else {
						LOG.info("截图失败……");
					}
				} else {
					LOG.info("截图失败……");
				}
			} catch (IOException e) {
				LOG.error("截图异常", e);
			}
			return flag;
		}

	}

	/**
	 * 截图返回结果
	 */
	class Flag {
		// 状态
		private boolean success;
		// 截图文件保存路径
		private String file;

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public String getFile() {
			return file;
		}

		public void setFile(String file) {
			this.file = file;
		}

		@Override
		public String toString() {
			return "Flag{" + "success=" + success + ", file='" + file + '\'' + '}';
		}
	}

}
