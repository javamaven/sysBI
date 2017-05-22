package io.renren.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.google.gson.reflect.TypeToken;

public class HttpUtil {

	public static final Logger logger = Logger.getLogger(HttpUtil.class);

	public static final String DEF_CHATSET = "GBK";
	public static final int DEF_CONN_TIMEOUT = 3000;
	public static final int DEF_READ_TIMEOUT = 3000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	public static void main(String[] args) throws Exception {
		Type mapType = new TypeToken<Map<String, Map<String, String>>>() {
		}.getType();

		while (true) {
			String jsonStr = executeHttp("http://192.168.10.107:34545/metrics",
					null, "GET");

//			Map<String, Map<String, String>> jsonToType = GsonUtils.jsonToType(
//					jsonStr, mapType);

//			Map<String, String> map = jsonToType.get("mechine");
//			System.out.println("cpu使用率===>" + map.get("cpu.percent") + "%");
//			String mem = map.get("mem.totalVirtual");
//			System.out.println("mem使用率===>" + Long.parseLong(mem) / 1003160000l
//					+ "%");
			Thread.sleep(3000);
		}

	}

	public static String executeHttp(String strUrl, Map<String, Object> params,
			String method) throws Exception {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(
							conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	public static String executeHttps(String strUrl,
			Map<String, Object> params, String method) throws Exception {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		HttpsURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
			URL url = new URL(strUrl);
			conn = (HttpsURLConnection) url.openConnection();
			// 设置https相关属性
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(
							conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	public static String urlencode(Map<String, ?> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, ?> i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=")
						.append(URLEncoder.encode(i.getValue() + "", "UTF-8"))
						.append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static class TrustAnyTrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}

	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}
