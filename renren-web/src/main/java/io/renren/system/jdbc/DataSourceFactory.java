package io.renren.system.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 本地数据源
 * 
 * @author Administrator
 *
 */
public class DataSourceFactory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1721654089059901305L;
	private String mysqlUrl;
	private String mysqlUserName;
	private String mysqlPassword;
	private String mysqlDriver;
	private int mysqlInitSize;
	private int mysqlMaxSize;

	private String oracleUrl;
	private String oracleUserName;
	private String oraclePassword;
	private String oracleDriver;
	private int oracleInitSize;
	private int oracleMaxSize;

	// private Connection[] mysqlConnections;// 保存数据库连接
	// private String[] mysqlConnStatus;// 已连可用Y 已连不可用N 未连接X

	private BlockingQueue<Connection> mysqlConnectQueue;

	// private Connection[] oracleConnections;// 保存数据库连接
	// private String[] oracleConnStatus;// 已连可用Y 已连不可用N 未连接X
	// private Date[] lastQueryTime;// 时间戳

	private BlockingQueue<Connection> oracleConnectQueue;

	public DataSourceFactory() {

	}

	/**
	 * 系统启动的时候马上会执行
	 */
	public void init() {
		initMysqlConnectPool();
		initOracleConnectPool();
	}

	public void printMysqlConnectionPoll() {
		Iterator<Connection> iterator = mysqlConnectQueue.iterator();
		while (iterator.hasNext()) {
			System.err.println("++++++++++++++mysql-状态：" + iterator.next());
		}
	}

	public void printOracleConnectionPoll() {
		Iterator<Connection> iterator = oracleConnectQueue.iterator();
		while (iterator.hasNext()) {
			System.err.println("++++++++++++++oracle-状态：" + iterator.next());
		}
	}

	public synchronized Connection getMysqlConnection() {
		try {
			return mysqlConnectQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void returnMysqlConnection(Connection connection) {
		mysqlConnectQueue.offer(connection);
	}

	public synchronized Connection getOracleConnection() {
		try {
			return oracleConnectQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized void returnOracleConnection(Connection connection) {
		oracleConnectQueue.offer(connection);
	}

	private void initOracleConnectPool() {
		oracleConnectQueue = new ArrayBlockingQueue<Connection>(this.oracleInitSize);
		System.err.println("+++++++++初始化oracle连接池+++++++++++++++++");
		if (oracleInitSize < 1) {
			System.out.println("请正确设置连接池oracle个数");
		} else {
			try {
				Class.forName(this.oracleDriver);// register class
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < this.oracleInitSize; i++) {
				try {
					oracleConnectQueue.add(createOracleConnection());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("initPool is ready...");
		} // end if
	}

	private Connection createOracleConnection() {
		try {
			return DriverManager.getConnection(this.oracleUrl, this.oracleUserName, this.oraclePassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void initMysqlConnectPool() {
		mysqlConnectQueue = new ArrayBlockingQueue<Connection>(this.mysqlInitSize);
		System.err.println("+++++++++初始化mysql连接池+++++++++++++++++");
		if (mysqlInitSize < 1) {
			System.out.println("请正确设置mysql连接池个数");
		} else {
			try {
				Class.forName(this.mysqlDriver);// register class
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (int i = 0; i < this.mysqlInitSize; i++) {
				try {
					mysqlConnectQueue.add(createMysqlConnection());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println("initPool is ready...");
		} // end if
	}

	private Connection createMysqlConnection() {
		try {
			return DriverManager.getConnection(this.mysqlUrl, this.mysqlUserName, this.mysqlPassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将连接全部释放掉，重新生成连接池
	 */
	public void reInitConnectionPoll(){
		try {
			close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		init();
	}

	public void close() {
		System.err.println("+++++++++close+++++++++++++++++");
		Iterator<Connection> iterator_ora = oracleConnectQueue.iterator();
		while (iterator_ora.hasNext()) {
			try {
				iterator_ora.next().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		Iterator<Connection> iterator = mysqlConnectQueue.iterator();
		while (iterator.hasNext()) {
			try {
				iterator.next().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getMysqlUrl() {
		return mysqlUrl;
	}

	public void setMysqlUrl(String mysqlUrl) {
		this.mysqlUrl = mysqlUrl;
	}

	public String getMysqlUserName() {
		return mysqlUserName;
	}

	public void setMysqlUserName(String mysqlUserName) {
		this.mysqlUserName = mysqlUserName;
	}

	public String getMysqlPassword() {
		return mysqlPassword;
	}

	public void setMysqlPassword(String mysqlPassword) {
		this.mysqlPassword = mysqlPassword;
	}

	public String getMysqlDriver() {
		return mysqlDriver;
	}

	public void setMysqlDriver(String mysqlDriver) {
		this.mysqlDriver = mysqlDriver;
	}

	public int getMysqlInitSize() {
		return mysqlInitSize;
	}

	public void setMysqlInitSize(int mysqlInitSize) {
		this.mysqlInitSize = mysqlInitSize;
	}

	public int getMysqlMaxSize() {
		return mysqlMaxSize;
	}

	public void setMysqlMaxSize(int mysqlMaxSize) {
		this.mysqlMaxSize = mysqlMaxSize;
	}

	public String getOracleUrl() {
		return oracleUrl;
	}

	public void setOracleUrl(String oracleUrl) {
		this.oracleUrl = oracleUrl;
	}

	public String getOracleUserName() {
		return oracleUserName;
	}

	public void setOracleUserName(String oracleUserName) {
		this.oracleUserName = oracleUserName;
	}

	public String getOraclePassword() {
		return oraclePassword;
	}

	public void setOraclePassword(String oraclePassword) {
		this.oraclePassword = oraclePassword;
	}

	public String getOracleDriver() {
		return oracleDriver;
	}

	public void setOracleDriver(String oracleDriver) {
		this.oracleDriver = oracleDriver;
	}

	public int getOracleInitSize() {
		return oracleInitSize;
	}

	public void setOracleInitSize(int oracleInitSize) {
		this.oracleInitSize = oracleInitSize;
	}

	public int getOracleMaxSize() {
		return oracleMaxSize;
	}

	public void setOracleMaxSize(int oracleMaxSize) {
		this.oracleMaxSize = oracleMaxSize;
	}

}
