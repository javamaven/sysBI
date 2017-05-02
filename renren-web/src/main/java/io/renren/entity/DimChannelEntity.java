package io.renren.entity;

import java.io.Serializable;

/**
 * 渠道信息表
 * 
 * @author liyo
 *
 */
public class DimChannelEntity implements Serializable {

	private static final long serialVersionUID = -4286976293814644773L;

	private Integer channelId;
	private String channelCenter;
	private String channelLabel;
	private String channelName;
	private String channelNameBack;
	private Integer channelStatus;
	private Integer status;
	private String type;
	
	public DimChannelEntity(){}

	public DimChannelEntity(Integer channelId, String channelCenter, String channelLabel, String channelName,
			String channelNameBack, Integer channelStatus, Integer status, String type) {
		super();
		this.channelId = channelId;
		this.channelCenter = channelCenter;
		this.channelLabel = channelLabel;
		this.channelName = channelName;
		this.channelNameBack = channelNameBack;
		this.channelStatus = channelStatus;
		this.status = status;
		this.type = type;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public String getChannelCenter() {
		return channelCenter;
	}

	public void setChannelCenter(String channelCenter) {
		this.channelCenter = channelCenter;
	}

	public String getChannelLabel() {
		return channelLabel;
	}

	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelNameBack() {
		return channelNameBack;
	}

	public void setChannelNameBack(String channelNameBack) {
		this.channelNameBack = channelNameBack;
	}

	public Integer getChannelStatus() {
		return channelStatus;
	}

	public void setChannelStatus(Integer channelStatus) {
		this.channelStatus = channelStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "DimChannelEntity [channelId=" + channelId + ", channelCenter=" + channelCenter + ", channelLabel="
				+ channelLabel + ", channelName=" + channelName + ", channelNameBack=" + channelNameBack
				+ ", channelStatus=" + channelStatus + ", status=" + status + ", type=" + type + "]";
	}

}
