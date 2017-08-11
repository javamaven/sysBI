package io.renren.entity.shichang;

import java.io.Serializable;



/**
 * 渠道分类-手动维护
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-08-10 10:03:42
 */
public class DimChannelTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//渠道标签
	private String channelLabel;
	//渠道分类
	private String channelType;
	private String channelCenter;
	private String channelHead;
	private String channelName;
	private String channelNameBack;
	private String channelStarttime;
	private String channelStatus;
	private String channelUptime;
	private String paymentWay; 
	public DimChannelTypeEntity(){
		
	}
	public String getChannelLabel() {
		return channelLabel;
	}
	public void setChannelLabel(String channelLabel) {
		this.channelLabel = channelLabel;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getChannelCenter() {
		return channelCenter;
	}
	public void setChannelCenter(String channelCenter) {
		this.channelCenter = channelCenter;
	}
	public String getChannelHead() {
		return channelHead;
	}
	public void setChannelHead(String channelHead) {
		this.channelHead = channelHead;
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
	public String getChannelStarttime() {
		return channelStarttime;
	}
	public void setChannelStarttime(String channelStarttime) {
		this.channelStarttime = channelStarttime;
	}
	public String getChannelStatus() {
		return channelStatus;
	}
	public void setChannelStatus(String channelStatus) {
		this.channelStatus = channelStatus;
	}
	public String getChannelUptime() {
		return channelUptime;
	}
	public void setChannelUptime(String channelUptime) {
		this.channelUptime = channelUptime;
	}
	public String getPaymentWay() {
		return paymentWay;
	}
	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}
	@Override
	public String toString() {
		return "DimChannelTypeEntity [channelLabel=" + channelLabel + ", channelType=" + channelType
				+ ", channelCenter=" + channelCenter + ", channelHead=" + channelHead + ", channelName=" + channelName
				+ ", channelNameBack=" + channelNameBack + ", channelStarttime=" + channelStarttime + ", channelStatus="
				+ channelStatus + ", channelUptime=" + channelUptime + ", paymentWay=" + paymentWay + "]";
	}
	
	
}
