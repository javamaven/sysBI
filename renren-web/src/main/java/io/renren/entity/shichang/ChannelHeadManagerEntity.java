package io.renren.entity.shichang;

import java.io.Serializable;

/**
 * 渠道负责人架构关系管理
 * 
 * @author Administrator
 *
 */
public class ChannelHeadManagerEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String channelHeadId;
	private String channelHead;
	private String sysAccount;
	private String parentChannelHead;
	private String parentChannelHeadId;
	private String description;
	private String channelName;
	
	public ChannelHeadManagerEntity(){}
	
	public void setChannelHeadId(String channelHeadId) {
		this.channelHeadId = channelHeadId;
	}
	
	public String getChannelHeadId() {
		return channelHeadId;
	}
	
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	public String getChannelName() {
		return channelName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setParentChannelHeadId(String parentChannelHeadId) {
		this.parentChannelHeadId = parentChannelHeadId;
	}

	public String getParentChannelHeadId() {
		return parentChannelHeadId;
	}


	public String getChannelHead() {
		return channelHead;
	}

	public void setChannelHead(String channelHead) {
		this.channelHead = channelHead;
	}

	public String getSysAccount() {
		return sysAccount;
	}

	public void setSysAccount(String sysAccount) {
		this.sysAccount = sysAccount;
	}

	public String getParentChannelHead() {
		return parentChannelHead;
	}

	public void setParentChannelHead(String parentChannelHead) {
		this.parentChannelHead = parentChannelHead;
	}

	@Override
	public String toString() {
		return "ChannelHeadManagerEntity [channelHead=" + channelHead + ", sysAccount=" + sysAccount
				+ ", parentChannelHead=" + parentChannelHead + "]";
	}

}
