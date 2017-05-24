package io.renren.system.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import io.renren.system.listener.PrintscreenServiceListener;

public class ConfigProp {
	// public static Map<String, String> systemConfigCache = new
	// ConcurrentHashMap<String, String>();
	static Properties p = new Properties();

	public static void main(String[] args) throws IOException {
		String path = ConfigProp.class.getResource("/").getPath() + "config.properties";
		InputStream in = null;
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		p.load(in);
		p.setProperty("es.host", "test");
		FileOutputStream os = new FileOutputStream(new File(path));
		p.store(os, null);
		os.flush();
		os.close();
		System.out.println("======end=====");

	}

	static {
		try {
			InputStream in = ConfigProp.class.getResourceAsStream("/config.properties");
			BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			p.load(bf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getTempDir() {
		return p.getProperty("email.temp.dir");
	}

	public static boolean getEmailSsl() {
		return Boolean.parseBoolean(p.getProperty("email.ssl"));
	}

	public static String getEmailPhone() {
		return p.getProperty("email.phone");
	}

	public static String getEmailUserName() {
		return p.getProperty("email.userName");
	}

	public static String getEmailAddress() {
		return p.getProperty("email.address");

	}

	public static String getEmailCompany() {
		return p.getProperty("email.company");
	}

	public static String getEmailPassword() {
		return p.getProperty("email.password");
	}

	public static String getEmailName() {
		return p.getProperty("email.name");
	}

	public static String getEmailHost() {
		return p.getProperty("email.host");
	}

	public static String getEmailPort() {
		return p.getProperty("email.port");
	}

	public static String getPrintscreenServiceRequestChartUrl() {
		return p.getProperty("printscreen.service.request.chart.url");
	}

	public static String getPrintscreenServiceURL() {
		return p.getProperty("printscreen.service.url");
	}

	public static String getPrintscreenServicePort() {
		return p.getProperty("printscreen.service.port");
	}

	/**
	 * 后台截图存放目录
	 * 
	 * @return
	 */
	public static String getPrintscreenDir() {
		// return p.getProperty("printscreen.dir");
		return PrintscreenServiceListener.IMAGE_FILE_STORE_DIR;
	}
}
