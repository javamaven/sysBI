package io.renren.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import io.renren.system.common.ConfigProp;

/**
 * 邮件发送 工具类 依赖 javax.mail-1.5.x.jar commons-email-1.4.jar
 * 
 * @author luocheng
 * @date 2015-9-27
 */
public class MailUtil {
	static Properties props = System.getProperties();
	static {
		props.put("mail.smtp.host", ConfigProp.getEmailHost());
		props.put("mail.smtp.auth", "true");
		props.put("mail.transpost.protocol", "smtp");
	}

	protected final Logger logger = Logger.getLogger(getClass());

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 发送操作
	 * 
	 * @param mail
	 *            实体bean
	 * @return true:false
	 */
	public synchronized boolean send(Mail mail) {
		HtmlEmail email = new HtmlEmail();
		// 发送email
		String receiver = mail.getReceiver();
		if (receiver != null && !receiver.equals("")) {
			mail.getReceivers().add(receiver);
		}
		try {
			synchronized (email) {
				// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
				email.setHostName(mail.getHost());
				// 字符编码集的设置
				email.setCharset(Mail.ENCODEING);
				EmailAttachment attachment = null;
				String attachPath = mail.getAttachPath();
				if (attachPath != null) {
					attachment = new EmailAttachment();
					try {
						attachment.setPath(attachPath);
						attachment.setName(MimeUtility.encodeText(mail.getAttachName()));
						attachment.setDisposition(EmailAttachment.ATTACHMENT);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if (attachment != null) {
					email.attach(attachment);
				}

				// 收件人的邮箱
				// email.addTo(mail.getReceiver());
				List<InternetAddress> inter = new ArrayList<InternetAddress>();
				for (int i = 0; i < mail.getReceivers().size(); i++) {
					try {
						String addr = mail.getReceivers().get(i);
						if (addr == null || "".equals(addr.trim())) {
							continue;
						}
						InternetAddress internetAddress = new InternetAddress(addr);
						inter.add(internetAddress);
					} catch (AddressException e) {
						e.printStackTrace();
					}

				}
				email.setTo(inter);
				for (int i = 0; i < mail.getChaosong().size(); i++) {
					String ccMail = mail.getChaosong().get(i);
					email.addCc(ccMail);
				}
				// 发送人的邮箱
				email.setFrom(mail.getUsername(), mail.getName());
				// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
				email.setAuthentication(mail.getUsername(), mail.getPassword());
				// 要发送的邮件主题
				email.setSubject(mail.getSubject());
				// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
				// email.setMsg(mail.getMessage());
				// 是否开启SSL 在端口非25是 需要改为true 默认为false
				email.setSSL(mail.isSsl());
				// 设置发送端口 默认为 25
				email.setSmtpPort(mail.getPort());
				email.setHtmlMsg(genMsg(mail));
				// 是否开启debug
				email.setDebug(mail.isDebug());
				// 发送
				email.send();
				if (logger.isDebugEnabled()) {
					logger.debug(mail.getUsername() + " 发送邮件到 " + receiver);
				}
			}
			return true;
		} catch (EmailException e) {
			e.printStackTrace();
			logger.info(mail.getUsername() + " 发送邮件到 " + receiver + " 失败");
			return false;
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param mail
	 * @return
	 */
	public synchronized Map<String, Object> sendEmail(Mail mail) {
		Map<String, Object> ret = new HashMap<String, Object>();
		HtmlEmail email = new HtmlEmail();
		// 发送email
		String receiver = mail.getReceiver();
		if (receiver != null && !receiver.equals("")) {
			mail.getReceivers().add(receiver);
		}
		try {
			synchronized (email) {
				// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
				email.setHostName(mail.getHost());
				// 字符编码集的设置
				email.setCharset(Mail.ENCODEING);
				// 收件人的邮箱
				// email.addTo(mail.getReceiver());
				List<InternetAddress> inter = new ArrayList<InternetAddress>();
				for (int i = 0; i < mail.getReceivers().size(); i++) {
					try {
						String addr = mail.getReceivers().get(i);
						if (addr == null || "".equals(addr.trim())) {
							continue;
						}
						InternetAddress internetAddress = new InternetAddress(addr);
						inter.add(internetAddress);
					} catch (AddressException e) {
						e.printStackTrace();
					}

				}
				email.setTo(inter);
				for (int i = 0; i < mail.getChaosong().size(); i++) {
					String ccMail = mail.getChaosong().get(i);
					email.addCc(ccMail);
				}
				// 发送人的邮箱
				email.setFrom(mail.getUsername(), mail.getName());
				// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
				email.setAuthentication(mail.getUsername(), mail.getPassword());
				// 要发送的邮件主题
				email.setSubject(mail.getSubject());
				// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
				// email.setMsg(mail.getMessage());
				// 是否开启SSL 在端口非25是 需要改为true 默认为false
				email.setSSL(mail.isSsl());
				// 设置发送端口 默认为 25
				email.setSmtpPort(mail.getPort());
				// 是否开启debug
				email.setDebug(mail.isDebug());
				email.setHtmlMsg(genMsg(mail));
				// 发送
				email.send();
				if (logger.isDebugEnabled()) {
					logger.debug(mail.getUsername() + " 发送邮件到 " + receiver);
				}
			}
			ret.put("code", "success");
		} catch (EmailException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public synchronized boolean send(String title, String content, String receivers, File file, String attachName) {
		boolean flag = false;
		try {
			if (StringUtils.isEmpty(receivers)) {
				return false;
			}
			Mail mail = new Mail();
			if (file.length() != 0) {
				mail.setAttachPath(file.getAbsolutePath());
				// mail.setAttachName(attachName + ".txt");
				mail.setAttachName(attachName + ".zip");
			}
			mail.setHost(ConfigProp.getEmailHost()); // 设置邮件服务器,如果不用163的,自己找找看相关的
			mail.setSsl(ConfigProp.getEmailSsl());
			mail.setPort(Integer.valueOf(ConfigProp.getEmailPort()));
			mail.setName(ConfigProp.getEmailName());
			// 接收人
			mail.setUsername(ConfigProp.getEmailUserName()); // 登录账号,一般都是和邮箱名一样吧
			mail.setPassword(ConfigProp.getEmailPassword()); // 发件人邮箱的登录密码
			mail.setSubject(title);
			mail.setMessage(content);
			// List<String> receiver = this.getSplitString(receivers, "\\|");
			String[] split = receivers.split("\\|");
			List<String> chaosong = new ArrayList<String>();
			if (split != null) {
				String[] receiver__ = split[0].split(",");
				mail.setReceivers(Arrays.asList(receiver__));
				if (split.length > 1) {
					String[] chaosong__ = split[1].split(",");
					chaosong.addAll(Arrays.asList(chaosong__));
				}
			} else {
				return false;
			}
			mail.setChaosong(chaosong);
			flag = send(mail);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (file != null && file.exists()) {
				file.delete();
			}
		}
		return flag;
	}

	/**
	 * 发送邮件
	 * 
	 * @param title
	 *            主题
	 * @param content
	 *            邮件内容
	 * @param receiver
	 *            收件人
	 * @param chaosong
	 *            抄送人
	 * @return
	 */
	public boolean send(String title, String content, List<String> receiver, List<String> chaosong) {

		try {
			Mail mail = new Mail();
			mail.setHost(ConfigProp.getEmailHost()); // 设置邮件服务器,如果不用163的,自己找找看相关的
			mail.setSsl(ConfigProp.getEmailSsl());
			mail.setPort(Integer.parseInt(ConfigProp.getEmailPort()));
			mail.setName(ConfigProp.getEmailName());
			// 接收人
			mail.setUsername(ConfigProp.getEmailUserName()); // 登录账号,一般都是和邮箱名一样吧
			mail.setPassword(ConfigProp.getEmailPassword()); // 发件人邮箱的登录密码
			mail.setSubject(title);
			mail.setMessage(content);

			mail.setReceivers(receiver);
			if (chaosong != null && !chaosong.isEmpty()) {
				mail.setChaosong(chaosong);
			}
			return send(mail);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	/**
	 * 发送带多个附件邮件
	 * @param title
	 * @param content
	 * @param receiverList
	 * @param chaosongList
	 * @param attachFilePath
	 * @throws EmailException
	 */
	public void sendWithAttachs(String title, String content, List<String> receiverList, List<String> chaosongList,
			List<String> attachFilePathList) throws EmailException {
		if (!ConfigProp.getIsSendEmail()) {
			return;
		}
		HtmlEmail email = new HtmlEmail();
		// 发送email
		// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
		email.setHostName(ConfigProp.getEmailHost());
		// 字符编码集的设置
		email.setCharset(Mail.ENCODEING);
		
		for (int i = 0; i < attachFilePathList.size(); i++) {
			EmailAttachment attach = new EmailAttachment();
			attach.setPath(attachFilePathList.get(i));
			if (attach != null) {
				email.attach(attach);
			}
		}
		// 收件人的邮箱
		// email.addTo(mail.getReceiver());
		List<InternetAddress> inter = new ArrayList<InternetAddress>();
		for (int i = 0; i < receiverList.size(); i++) {
			try {
				String addr = receiverList.get(i);
				if (addr == null || "".equals(addr.trim())) {
					continue;
				}
				InternetAddress internetAddress = new InternetAddress(addr);
				inter.add(internetAddress);
			} catch (AddressException e) {
				e.printStackTrace();
			}

		}
		email.setTo(inter);
		for (int i = 0; i < chaosongList.size(); i++) {
			String ccMail = chaosongList.get(i);
			if (StringUtils.isNotEmpty(ccMail)) {
				email.addCc(ccMail);
			}
		}
		// 发送人的邮箱
		email.setFrom(ConfigProp.getEmailUserName(), ConfigProp.getEmailName());
		// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
		email.setAuthentication(ConfigProp.getEmailUserName(), ConfigProp.getEmailPassword());
		// 要发送的邮件主题
		email.setSubject(title);
		// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
		// email.setMsg(mail.getMessage());
		// 是否开启SSL 在端口非25是 需要改为true 默认为false
		email.setSSL(ConfigProp.getEmailSsl());
		// 设置发送端口 默认为 25
		email.setSmtpPort(Integer.valueOf(ConfigProp.getEmailPort()));
		Mail mailInfo = new Mail();
		mailInfo.setMessage(content);
		email.setHtmlMsg(genMsg(mailInfo));
		// 是否开启debug
		email.setDebug(false);
		// 发送
		email.send();

	}

	/**
	 * 发送带附件邮件
	 * 
	 * @param title
	 *            邮件主题
	 * @param content
	 *            邮件内容
	 * @param receiverList
	 *            收件人
	 * @param chaosongList
	 *            抄送人
	 * @param attachFilePath
	 *            附件路径
	 * @return
	 * @throws EmailException
	 */
	public void sendWithAttach(String title, String content, List<String> receiverList, List<String> chaosongList,
			String attachFilePath) throws EmailException {
		if (!ConfigProp.getIsSendEmail()) {
			return;
		}
		HtmlEmail email = new HtmlEmail();
		// 发送email
		// 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"
		email.setHostName(ConfigProp.getEmailHost());
		// 字符编码集的设置
		email.setCharset(Mail.ENCODEING);
		EmailAttachment attach = new EmailAttachment();
		attach.setPath(attachFilePath);
		if (attach != null) {
			email.attach(attach);
		}

		// 收件人的邮箱
		// email.addTo(mail.getReceiver());
		List<InternetAddress> inter = new ArrayList<InternetAddress>();
		for (int i = 0; i < receiverList.size(); i++) {
			try {
				String addr = receiverList.get(i);
				if (addr == null || "".equals(addr.trim())) {
					continue;
				}
				InternetAddress internetAddress = new InternetAddress(addr);
				inter.add(internetAddress);
			} catch (AddressException e) {
				e.printStackTrace();
			}

		}
		email.setTo(inter);
		for (int i = 0; i < chaosongList.size(); i++) {
			String ccMail = chaosongList.get(i);
			if (StringUtils.isNotEmpty(ccMail)) {
				email.addCc(ccMail);
			}
		}
		// 发送人的邮箱
		email.setFrom(ConfigProp.getEmailUserName(), ConfigProp.getEmailName());
		// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码
		email.setAuthentication(ConfigProp.getEmailUserName(), ConfigProp.getEmailPassword());
		// 要发送的邮件主题
		email.setSubject(title);
		// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
		// email.setMsg(mail.getMessage());
		// 是否开启SSL 在端口非25是 需要改为true 默认为false
		email.setSSL(ConfigProp.getEmailSsl());
		// 设置发送端口 默认为 25
		email.setSmtpPort(Integer.valueOf(ConfigProp.getEmailPort()));
		Mail mailInfo = new Mail();
		mailInfo.setMessage(content);
		email.setHtmlMsg(genMsg(mailInfo));
		// 是否开启debug
		email.setDebug(false);
		// 发送
		email.send();

	}

	/**
	 * 分割字符串
	 * 
	 * @param line
	 *            类型
	 * @param delim
	 *            分割符
	 * @return
	 */
	public List<String> getSplitString(String line, String delim) {

		List<String> wordSet = new ArrayList<String>();
		StringTokenizer stk;
		if (StringUtils.isNotEmpty(delim)) {
			stk = new StringTokenizer(line, delim);
		} else {
			stk = new StringTokenizer(line);
		}
		while (stk.hasMoreElements()) {
			wordSet.add(stk.nextToken());
		}
		return wordSet;
	}

	/**
	 * 设置邮件签名内容
	 * 
	 * @param mail
	 * @return
	 */
	private String genMsg(Mail mail) {
		// Map<String, String> systemConfigCache =
		// ConfigProp.getSystemConfigCache();
		/*
		 * String logUserInfo = systemConfigCache.get("log_user"); Map logUser =
		 * new HashMap(); if(StringUtil.isNotEmpty(logUserInfo)){ logUser =
		 * GsonUtils.jsonToClass(logUserInfo, Map.class); } Map<String,String>
		 * logUserMap = (Map<String,String>) logUser.get(mail.getUsername());
		 */
		String br = "<br/>";
		String msg = mail.getMessage();
		String currDate = br + br + sdf.format(new Date());
		String hr = "<hr>";
		// String name = "姓名：" + logUser.get("userName");
		String company = "公司：" + ConfigProp.getEmailCompany();
		String phone = "联系电话：" + ConfigProp.getEmailPhone();
		String address = "地址：" + ConfigProp.getEmailAddress();
		msg += currDate + br + hr + company + br + phone + br + address;
		return msg;
	}

	// String title, String content, List<String> receiverList, List<String>
	// chaosongList,
	// String attachFilePath
	/**
	 * 发送带附件，和正文贴图邮件
	 * @param imgPaths	图片路径
	 * @param content	邮件正文
	 * @param title		邮件主题
	 * @param receiverList	收件人
	 * @param chaosongList	抄送人
	 * @param attachFilePath	附件路径
	 * @return
	 */
	public static boolean sendEmailWithAttachAndImg(String[] imgPaths, String content, String title,
			List<String> receiverList, List<String> chaosongList, String attachFilePath) {
		boolean ret = true;
		if (StringUtils.isEmpty(ConfigProp.getEmailUserName()) || StringUtils.isEmpty(ConfigProp.getEmailHost())) {
			throw new RuntimeException("没有设置邮件服务器信息，请检查");
		}
		String hostStr = props.get("mail.smtp.host") + "";
		if (StringUtils.isEmpty(hostStr)) {
			props.put("mail.smtp.host", ConfigProp.getEmailHost());
		}
		MimeMessage msg = new MimeMessage(Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ConfigProp.getEmailUserName(), ConfigProp.getEmailPassword());
			}
		}));
		// 设置邮件的发件人
		try {
			msg.setFrom(new InternetAddress(ConfigProp.getEmailUserName(), ConfigProp.getEmailName()));			
//			msg.setFrom(new InternetAddress(ConfigProp.getEmailUserName()));
			// 设置多个收件人地址
			InternetAddress[] internetAddressTo = new InternetAddress[receiverList.size()];
			for (int i = 0; i < receiverList.size(); i++) {
				internetAddressTo[i] = new InternetAddress(receiverList.get(i));
			}
			msg.setRecipients(Message.RecipientType.TO, internetAddressTo);

			// 设置多个抄送地址
			if (chaosongList != null && chaosongList.size() > 0) {
				InternetAddress[] internetAddressCC = new InternetAddress[chaosongList.size()];
				for (int i = 0; i < chaosongList.size(); i++) {
					if(StringUtils.isEmpty(chaosongList.get(i))){
						continue;
					}
					internetAddressCC[i] = new InternetAddress(chaosongList.get(i));
				}
				msg.setRecipients(Message.RecipientType.CC, internetAddressCC);
			}

			// 设置邮件的主题
			msg.setSubject(title);
			msg.setSentDate(new Date());

			// 关系 正文和图片的
			MimeMultipart multipart = new MimeMultipart();

			// 创建附件
			MimeBodyPart htmlBodyPart = new MimeBodyPart();
			DataHandler dh1 = new DataHandler(new FileDataSource(attachFilePath));
			htmlBodyPart.setDataHandler(dh1);
			String filename1 = dh1.getName();
			// MimeUtility 是一个工具类，encodeText（）用于处理附件字，防止中文乱码问题
			htmlBodyPart.setFileName(MimeUtility.encodeText(filename1));

			// 正文内容
			Object htmlContent = buildMsgAndImg(imgPaths, content);
			// 创建邮件的正文
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(htmlContent, "text/html;charset=utf-8");
			multipart.addBodyPart(text);

			for (int i = 0; i < imgPaths.length; i++) {
				// 创建图片
				MimeBodyPart img = new MimeBodyPart();
				DataHandler dh = new DataHandler(new FileDataSource(imgPaths[i]));
				img.setDataHandler(dh);
				// 创建图片的一个表示用于显示在邮件中显示
				img.setContentID("png" + i);
				multipart.addBodyPart(img);
			}
			// setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
			multipart.addBodyPart(htmlBodyPart);
			multipart.setSubType("related");// 设置正文与图片之间的关系
			msg.setContent(multipart);
			msg.saveChanges(); // 保存修改
			Transport.send(msg);// 发送邮件
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 发送带有图片的附件
	 * 
	 * @param imgPaths
	 *            图片全路径
	 * @param title
	 *            邮件标题
	 * @param receivers
	 *            收件人
	 * @param cc
	 *            抄送人
	 * @param attchPath
	 *            附件全路径
	 * @param mail
	 * @param dashboardList
	 * @return
	 */
	public static boolean sendEmailWithImg(String[] imgPaths, String title, List<String> receivers, List<String> cc,
			String attchPath, Mail mail, List<Map<String, Object>> dashboardList) {
		boolean ret = true;
		if (StringUtils.isEmpty(ConfigProp.getEmailUserName()) || StringUtils.isEmpty(ConfigProp.getEmailHost())) {
			throw new RuntimeException("没有设置邮件服务器信息，请检查");
		}
		String hostStr = props.get("mail.smtp.host") + "";
		if (StringUtils.isEmpty(hostStr)) {
			props.put("mail.smtp.host", ConfigProp.getEmailHost());
		}
		MimeMessage msg = new MimeMessage(Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ConfigProp.getEmailUserName(), ConfigProp.getEmailPassword());
			}
		}));
		// 设置邮件的发件人
		try {
			msg.setFrom(new InternetAddress(ConfigProp.getEmailUserName()));
			// 设置多个收件人地址
			InternetAddress[] internetAddressTo = new InternetAddress[receivers.size()];
			for (int i = 0; i < receivers.size(); i++) {
				internetAddressTo[i] = new InternetAddress(receivers.get(i));
			}
			msg.setRecipients(Message.RecipientType.TO, internetAddressTo);

			// 设置多个抄送地址
			if (cc != null && cc.size() > 0) {
				InternetAddress[] internetAddressCC = new InternetAddress[cc.size()];
				for (int i = 0; i < cc.size(); i++) {
					internetAddressCC[i] = new InternetAddress(cc.get(i));
				}
				msg.setRecipients(Message.RecipientType.CC, internetAddressCC);
			}

			// 设置邮件的主题
			msg.setSubject(title);
			msg.setSentDate(new Date());

			// 关系 正文和图片的
			MimeMultipart multipart = new MimeMultipart();

			// 创建附件
			MimeBodyPart htmlBodyPart = new MimeBodyPart();
			DataHandler dh1 = new DataHandler(new FileDataSource(attchPath));
			htmlBodyPart.setDataHandler(dh1);
			String filename1 = dh1.getName();
			// MimeUtility 是一个工具类，encodeText（）用于处理附件字，防止中文乱码问题
			htmlBodyPart.setFileName(MimeUtility.encodeText(filename1));

			// 正文内容
			Object htmlContent = buildMsgAndImg(imgPaths, dashboardList, mail);
			// 创建邮件的正文
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(htmlContent, "text/html;charset=utf-8");
			multipart.addBodyPart(text);

			for (int i = 0; i < imgPaths.length; i++) {
				// 创建图片
				MimeBodyPart img = new MimeBodyPart();
				DataHandler dh = new DataHandler(new FileDataSource(imgPaths[i]));
				img.setDataHandler(dh);
				// 创建图片的一个表示用于显示在邮件中显示
				img.setContentID("png" + i);
				multipart.addBodyPart(img);
			}
			// setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
			multipart.addBodyPart(htmlBodyPart);
			multipart.setSubType("related");// 设置正文与图片之间的关系
			msg.setContent(multipart);
			msg.saveChanges(); // 保存修改
			Transport.send(msg);// 发送邮件
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		}
		return ret;
	}

	private static String buildMsgAndImg(String[] imgPaths, String content) {
		String msg = content + "<br/><br/>";
		String imgs = "";
		int max_width = 1000;
		int max_height = 500;
		for (int i = 0; i < imgPaths.length; i++) {
			String img = "<img style='position:absolute;border:1px solid #F2F2F2;";// border:1px
			 img += "' src='cid:png" + i + "' />";
			 imgs += img;
		}
		msg += "<div style='width: " + max_width + "px;height:" + max_height + "px;overflow: auto;'>";
		msg += imgs;
		msg += "</div>";
		return msg;
	}

	private static String buildMsgAndImg(String[] imgPaths, List<Map<String, Object>> dashboardList, Mail mail) {
		String msg = mail.getMessage() + "<br/><br/>";
		String imgs = "";
		int max_width = 0;
		int max_height = 0;
		for (int i = 0; i < imgPaths.length; i++) {
			String img = "<img style='position:absolute;border:1px solid #F2F2F2;";// border:1px
																					// solid
																					// red;
			Map<String, Object> map = dashboardList.get(i);
			int left = Integer.parseInt(map.get("dataCol") + "");
			int top = Integer.parseInt(map.get("dataRow") + "");
			int width = Integer.parseInt(map.get("dataSizeX") + "");
			int height = Integer.parseInt(map.get("dataSizeY") + "");
			int width_tmp = 0;
			int height_tmp = 0;
			int baseHeight = 95;
			int baseWidth = 100;
			// int screenWidth = ((int) java.awt.Toolkit.getDefaultToolkit()
			// .getScreenSize().width);
			// if(screenWidth != 0){
			// baseWidth = (int) (screenWidth*0.0732);
			// baseHeight = (int) (screenWidth*0.0732);
			// }
			if (left > 1) {
				width_tmp += left * baseWidth;
				img += "left: " + left * baseWidth + "px;";
			} else {
				width_tmp += baseWidth;
				img += "left: " + baseWidth + "px;";
			}
			if (top > 1) {
				height_tmp += top * baseHeight;
				img += "top: " + top * baseHeight + "px;";
			} else {
				height_tmp += baseHeight;
				img += "top: " + baseHeight + "px;";
			}
			img += "width: " + width * baseWidth + "px;";
			width_tmp += width * baseWidth;
			img += "height: " + height * baseHeight + "px;";
			height_tmp += height * baseHeight;
			img += "' src='cid:png" + i + "' />";
			imgs += img;
			if (width_tmp > max_width) {
				max_width = width_tmp;
			}
			if (height_tmp > max_height) {
				max_height = height_tmp;
			}
		}
		msg += "<div style='width: " + max_width + "px;height:" + max_height + "px;overflow: auto;'>";
		msg += imgs;
		msg += "</div>";
		// msg += getTableHtml();
		return msg;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
		// // mail.send("send email test !!!", " send by liyo!!!","");
		// // System.out.println("=================邮件发送完毕=============");
		// String from = "366806529@sina.cn";
		// String to = "let_free@163.com";
		// String subject = "创建一个内含图片的邮件！";
		// String body = "<h4>内含图片的邮件测试！！！</h4> " + "<img src =
		// \"cid:haolloyin_logo_jpg\"></a>";
		// /*
		// * 上面的 cid 是指 MIME 协议中的 Content-ID 头，它是 对于文件资源的唯一标识符，下面的
		// * setContentID()方法 中传入的 ID 号就必须与这个haolloyin_logo_jpg相一致
		// */
		//
		// sendTest(to, subject, body);
		// System.out.println("=================end=================");

		MailUtil util = new MailUtil();
		// Mail mail = new Mail();
		// mail.setAddress(ConfigProp.getEmailAddress());
		// mail.setCompany(ConfigProp.getEmailCompany());
		// mail.setHost(ConfigProp.getEmailHost());
		// mail.setReceiver("liaodehui@mindai.com");
		// mail.setUsername(ConfigProp.getEmailUserName());
		// mail.setName(ConfigProp.getEmailName());
		// mail.setPassword(ConfigProp.getEmailPassword());
		// mail.setSender(ConfigProp.getEmailUserName());
		// mail.setSubject("每日报表");
		// mail.setMessage("这是一封测试邮件！");
		// util.sendEmail(mail);
		List<String> receList = new ArrayList<String>();
		List<String> chaosongList = new ArrayList<String>();
		receList.add("liaodehui@mindai.com");

		// chaosongList.add("liangweixiong@mindai.com");
		chaosongList.add("wanghanzhao@mindai.com");
		util.send("每日报表", "这是一封测试邮件！", receList, chaosongList);
		System.err.println("++++++++++end++++++++++++++++");
	}

	private static void sendTest(String to, String subject, String body) throws MessagingException, AddressException {
		MimeMessage msg = new MimeMessage(Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("bigData_system@shsnc.com", "bigData@shsnc!@");
			}
		}));
		// 以下几句是设置邮件的基本信息
		msg.setFrom(new InternetAddress("bigData_system@shsnc.com"));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		msg.setSubject(subject);
		msg.setSentDate(new Date());

		/*
		 * 创建一个子类型为 “related” 的 MIME 消息组合对象， 其实 MimeMultipart 类还有另外两种子类型，请自行
		 * 查阅并比较其中的适用场景
		 */
		MimeMultipart multipart = new MimeMultipart("related");

		/*
		 * 创建一个表示 HTML 正文部分的 MimeBodyPart 对象， 并加入 到上一语句创建的 MimeMultiPart 对象中， 由于
		 * HTML 正文中包含中文，故下面应该指定字符集为 gbk 或 gb2312
		 */
		MimeBodyPart htmlBodyPart = new MimeBodyPart();
		htmlBodyPart.setContent(body, "text/html;charset=gbk");
		multipart.addBodyPart(htmlBodyPart);

		/*
		 * 创建一个表示图片文件的 MimeBodyPart 对象， 并加入 到上面所创建的 MimeMultiPart 对象中， 使用 JAF
		 * 框架中的 FileDataSource 类获取图片文件源， 它能够自动获知文件的 MIME 格式并设置好消息头
		 */
		MimeBodyPart jpgBodyPart = new MimeBodyPart();
		FileDataSource fds = new FileDataSource("C:\\Users\\liyo\\Desktop\\a.png");
		jpgBodyPart.setDataHandler(new DataHandler(fds));
		jpgBodyPart.setContentID("haolloyin_logo_jpg");

		multipart.addBodyPart(jpgBodyPart);

		/*
		 * 这里不用再设置字符集了，因为上面的 HTML MimeBodyPart 对象中 已经设置了，而图片文件的因为使用了 DataSource
		 * 对象，即 JAF 框架 中的具体数据处理操作类，能够自动判断获知数据的 MIME 类型
		 */
		msg.setContent(multipart);

		// 保存对邮件的修改并写入文件中
		msg.saveChanges();
		Transport.send(msg);
	}

	/**
	 * 报表推送
	 * 
	 * @param title
	 * @param content
	 * @param sendEmail
	 * @param path
	 * @param imgPath
	 */
	public boolean sendReport(String title, String content, String receivers, String attachFilePath,
			Map<String, String> imgPath) {
		if (StringUtils.isEmpty(receivers)) {
			return false;
		}
		boolean ret = true;
		if (StringUtils.isEmpty(ConfigProp.getEmailUserName()) || StringUtils.isEmpty(ConfigProp.getEmailHost())) {
			throw new RuntimeException("没有设置邮件服务器信息，请检查");
		}
		String hostStr = props.get("mail.smtp.host") + "";
		if (StringUtils.isEmpty(hostStr)) {
			props.put("mail.smtp.host", ConfigProp.getEmailHost());
		}
		MimeMessage msg = new MimeMessage(Session.getInstance(props, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ConfigProp.getEmailUserName(), ConfigProp.getEmailPassword());
			}
		}));
		// 设置邮件的发件人
		try {
			msg.setFrom(new InternetAddress(ConfigProp.getEmailUserName()));
			// 设置多个收件人地址
			String[] split = receivers.split(",");
			InternetAddress[] internetAddressTo = new InternetAddress[split.length];
			for (int i = 0; i < split.length; i++) {
				internetAddressTo[i] = new InternetAddress(split[i]);
			}
			msg.setRecipients(Message.RecipientType.TO, internetAddressTo);

			// 设置邮件的主题
			msg.setSubject(title);
			msg.setSentDate(new Date());

			// 关系 正文和图片的
			MimeMultipart multipart = new MimeMultipart();

			String img_path = imgPath.get("path");
			// 创建附件
			// MimeBodyPart htmlBodyPart = new MimeBodyPart();
			// File attchPath = new File(img_path);
			// DataHandler dh1 = new DataHandler(new FileDataSource(attchPath));
			// htmlBodyPart.setDataHandler(dh1);
			// String filename1 = dh1.getName();
			// // MimeUtility 是一个工具类，encodeText（）用于处理附件字，防止中文乱码问题
			// htmlBodyPart.setFileName(MimeUtility.encodeText(filename1));

			// 正文内容
			Object htmlContent = buildReportMsgAndImg(img_path, content);
			// 创建邮件的正文
			MimeBodyPart text = new MimeBodyPart();
			text.setContent(htmlContent, "text/html;charset=utf-8");
			multipart.addBodyPart(text);

			// 创建图片
			MimeBodyPart img = new MimeBodyPart();
			DataHandler dh = new DataHandler(new FileDataSource(img_path));
			img.setDataHandler(dh);
			// 创建图片的一个表示用于显示在邮件中显示
			img.setContentID("png0");
			multipart.addBodyPart(img);

			// setContent(“邮件的正文内容”,”设置邮件内容的编码方式”)
			multipart.setSubType("related");// 设置正文与图片之间的关系
			msg.setContent(multipart);
			msg.saveChanges(); // 保存修改
			Transport.send(msg);// 发送邮件
		} catch (Exception e) {
			ret = false;
			e.printStackTrace();
		}
		return ret;
	}

	private Object buildReportMsgAndImg(String img_path, String content) {
		String msg = content + "<br/><br/><img src = \"cid:png0\" />";
		return msg;
	}
}
