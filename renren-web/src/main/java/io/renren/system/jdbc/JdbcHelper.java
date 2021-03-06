package io.renren.system.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
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

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.StringUtils;

/**
 * 获取系统连接池的连接，直接操作jdbc
 * 
 * @author liaodehui
 *
 */
public class JdbcHelper {

	private DruidDataSource dataSource;

	private DruidPooledConnection connectionPool;

	private Connection connection;

	public JdbcHelper() {

	}

	public JdbcHelper(DruidDataSource dataSource) {
		this.dataSource = dataSource;
		try {
			connectionPool = this.dataSource.getConnection();
			connection = this.connectionPool.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从系统连接池中获取连接
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * 归还连接到系统连接池
	 */
	public void returnConnection() {
		try {
			connectionPool.recycle();
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public void execute(String sql, Object... paramters) throws SQLException {
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = connection.prepareStatement(sql);
			for (int i = 0; i < paramters.length; i++) {
				prepareStatement.setObject(i + 1, paramters[i]);
			}
			prepareStatement.execute();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			free(prepareStatement, null);
		}
	}
	
	/**
	 * 将指定路径的文件(比如：图片，word文档等)存储到数据库
	 * @param filePath  文件路径，如D:\\a.jpg
	 * @param coloumnIndex 图片字段在表中属于第几个字段
	 * @param insertSql
	 * @param paramters
	 * @throws Exception
	 */
	public void storeImg(String filePath, int coloumnIndex, String insertSql, Object... paramters) throws Exception {
		File file = null;
		PreparedStatement ps = null;
		FileInputStream fis = null;
		if(!StringUtils.isEmpty(filePath)){
			try {
				file = new File(filePath);
				fis = new FileInputStream(file);
			} catch (Exception e) {
				filePath = null;
			}
		}
		try {
			ps = connection.prepareStatement(insertSql);
			for (int i = 0; i < paramters.length; i++) {
				
				if (i + 1 == coloumnIndex) {
					if(StringUtils.isEmpty(filePath)){
						ps.setBinaryStream(i + 1, null);
					}else{
						ps.setBinaryStream(i + 1, fis, (int) file.length());
					}
				} else {
//					System.err.println(paramters[i]);
					ps.setString(i + 1, paramters[i]+"");
				}
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			free(ps, null);
		}
	}
	
	/**
	 * 将存储在数据库中的文件(比如：图片，word文档等)读取到指定路径
	 * 
	 * @param path
	 *            从数据库里读取出来的文件存放路径 如D:\\a.jpg
	 * @param id
	 *            数据库里记录的id
	 */
	public void readImg(String path, String fileName,String fieldName, String querySql, OutputStream out) throws Exception {
		byte[] buffer = new byte[4096];
		InputStream is = null;
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(querySql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Blob blob = rs.getBlob(fieldName); // img为数据库存放图片字段名称
			is = blob.getBinaryStream();
			int size = 0;
			while ((size = is.read(buffer)) != -1) {
				out.write(buffer, 0, size);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			free(ps, null);
		}
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
				map.put(md.getColumnLabel(i), rs.getObject(i));
			}
			list.add(map);
		}
		return list;
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
//							cmd.setDate(j + 1, new java.sql.Date(((Date) obj).getTime()));//该方法没了时分秒
							long time = ((Date) obj).getTime();
//							cmd.setDate(j + 1, new java.sql.Date(time));
							cmd.setTimestamp(j + 1, new java.sql.Timestamp(time));  //指定时间
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

}
