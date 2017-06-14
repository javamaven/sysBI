package io.renren.system.jdbc;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcUtil {
	DataSourceFactory factory;
	// 定义数据库的链接
	private Connection connection;
	private String dbType;// oracle , mysql

	public JdbcUtil(DataSourceFactory dataSourceFactory, String dbType) {
		this.factory = dataSourceFactory;
		this.dbType = dbType;
		if ("oracle".equals(dbType)) {
			this.connection = factory.getOracleConnection();
		} else if ("mysql".equals(dbType)) {
			this.connection = factory.getMysqlConnection();
		} else if ("oracle26".equals(dbType)) {
			this.connection = factory.getOracle26Connection();
		}
	}

	/**
	 * 释放resultset
	 * 
	 * @param rs
	 */
	private void freeResultSet(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 归还连接到系统连接池
	 */
	public void returnConnection() {
		if ("oracle".equals(this.dbType)) {
			factory.returnOracleConnection(connection);
		} else if ("mysql".equals(this.dbType)) {
			factory.returnMysqlConnection(connection);
		} else if ("oracle26".equals(this.dbType)) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 释放statement
	 * 
	 * @param statement
	 */
	private void freeStatement(Statement statement) {
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放资源
	 * 
	 * @param conn
	 * @param statement
	 * @param rs
	 */
	public void free(Statement statement, ResultSet rs) {
		if (rs != null) {
			freeResultSet(rs);
		}
		if (statement != null) {
			freeStatement(statement);
		}
		returnConnection();
	}

	/**
	 * 用于带参数的查询，返回结果集
	 * 
	 * @param sql
	 *            sql语句
	 * @param paramters
	 *            参数集合
	 * @return 结果集
	 * @throws SQLException
	 */
	public List<Map<String, Object>> query(String sql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = connection.prepareStatement(sql);
			for (int i = 0; i < paramters.length; i++) {
				prepareStatement.setObject(i + 1, paramters[i]);
			}
			rs = prepareStatement.executeQuery();
			return resultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(prepareStatement, rs);
		}
	}
	
	/**
	 * 用于带参数的查询，返回结果集
	 * 
	 * @param sql
	 *            sql语句
	 * @param paramters
	 *            参数集合
	 * @return 结果集
	 * @throws SQLException
	 */
	public boolean execute(String sql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = connection.prepareStatement(sql);
			for (int i = 0; i < paramters.length; i++) {
				prepareStatement.setObject(i + 1, paramters[i]);
			}
			return prepareStatement.execute();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(prepareStatement, rs);
		}
	}

	public void batchInsert(String sql, List<List<Object>> dataList) throws ClassNotFoundException, SQLException {
		long start = System.currentTimeMillis();
		PreparedStatement cmd = null;
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			connection.setAutoCommit(false);
			cmd = connection.prepareStatement(sql);
			for (int i = 0; i < dataList.size(); i++) {// 100万条数据
				List<Object> data = dataList.get(i);
				for (int j = 0; j < data.size(); j++) {
					Object obj = data.get(j);
					if (obj == null) {
						cmd.setString(j + 1, null);
					} else {
						if (obj instanceof Date) {
							cmd.setDate(j + 1, new java.sql.Date(((Date) obj).getTime()));
						} else {
							cmd.setString(j + 1, obj + "");
						}

					}
				}
				cmd.addBatch();
				if (i != 0 && i % 5000 == 0) {
					cmd.executeBatch();
				}
			}
			cmd.executeBatch();
			connection.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			free(cmd, null);
		}
		long end = System.currentTimeMillis();
		System.out.println("批量插入需要时间:" + (end - start)); // 批量插入需要时间:24675
	}

	/**
	 * 调用存储过程执行查询
	 * 
	 * @param procedureSql
	 *            存储过程
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> callableQuery(String procedureSql) throws SQLException {
		ResultSet rs = null;
		CallableStatement callableStatement = null;
		try {
			callableStatement = connection.prepareCall(procedureSql);
			rs = callableStatement.executeQuery();
			return resultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(callableStatement, rs);
		}
	}

	/**
	 * 调用存储过程（带参数）,执行查询
	 * 
	 * @param procedureSql
	 *            存储过程
	 * @param paramters
	 *            参数表
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> callableQuery(String procedureSql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		CallableStatement callableStatement = null;
		try {
			callableStatement = connection.prepareCall(procedureSql);
			for (int i = 0; i < paramters.length; i++) {
				callableStatement.setObject(i + 1, paramters[i]);
			}
			rs = callableStatement.executeQuery();
			return resultToListMap(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(callableStatement, rs);
		}
	}

	private static List<Map<String, Object>> resultToListMap(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			ResultSetMetaData md = rs.getMetaData();
			Map<String, Object> map = new HashMap<String, Object>();
			int columnCount = md.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				Object obj = rs.getObject(i);
				if(obj instanceof Integer){
					map.put(md.getColumnLabel(i), Integer.parseInt(obj+""));
				}else if(obj instanceof Double){
					map.put(md.getColumnLabel(i), Double.parseDouble(obj+""));
				}else if(obj instanceof Float){
					map.put(md.getColumnLabel(i), Float.parseFloat(obj+""));
				}else if(obj instanceof BigDecimal){
					map.put(md.getColumnLabel(i), new BigDecimal(obj+""));
				}else{
					map.put(md.getColumnLabel(i), obj);
				}
				
			}
			list.add(map);
		}
		return list;
	}
}