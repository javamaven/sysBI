package io.renren.service.schedule;

/**
 * 常量
 *
 */
public class Constants {

	/**
	 * 报表推送任务类型
	 *
	 */
	public static class TaskType {

		/**
		 * 渠道首投复投
		 */
		public static String CHANNEL_STFT = "1";

		/**
		 * 渠道流失分析
		 */
		public static String CHANNEL_LOSS = "2";
		/**
		 * 渠道投资次数分析
		 */
		public static String CHANNEL_INVEST_TIMES = "3";
		/**
		 * 渠道续费
		 */
		public static String CHANNEL_RENEW = "4";
		/**
		 * 用户激活
		 */
		public static String USER_ACTIVE_INFO = "5";
		/**
		 * 用户投资
		 */
		public static String USER_INVEST_INFO = "6";
		
		/**
		 * 渠道负责人
		 */
		public static String MARKET_CHANNEL = "7";
		
		/**
		 * 渠道分次投资情况
		 */
		public static String CHANNEL_ALL = "8";
		
	}
}
