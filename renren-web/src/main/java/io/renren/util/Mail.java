package io.renren.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 邮件发送实体
 * 
 */
public class Mail implements Serializable {
	private static final long serialVersionUID = 5816438474749236901L;

	public static final String ENCODEING = "UTF-8";

	private String host; // 服务器地址

	private String sender; // 发件人的邮箱

	private String receiver; // 收件人的邮箱
	private List<String> receivers = new ArrayList<String>(); // 收件人的邮箱
	private List<String> chaosong = new ArrayList<String>(); // 抄送人
	private String name; // 发件人昵称

	private String username; // 账号

	private String password; // 密码

	private String subject; // 主题

	private String message; // 信息(支持HTML)

	private boolean ssl = false; // 是否开启sll接入 默认为false

	private int port = 25; // 发送邮件端口 修改端口 需要开启ssl

	private boolean debug = false;// 是否开启 debug 默认为false

	private String attachPath;// 附件路径
	private String attachName;// 附件路径

	private String company;// 个性签名(公司)
	private String address;// 个性签名(地址)

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachPath(String attachPath) {
		this.attachPath = attachPath;
	}

	public String getAttachPath() {
		return attachPath;
	}

	public void setChaosong(List<String> chaosong) {
		this.chaosong = chaosong;
	}

	public List<String> getChaosong() {
		return chaosong;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setReceivers(List<String> receiver) {
		this.receivers = receiver;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

}
