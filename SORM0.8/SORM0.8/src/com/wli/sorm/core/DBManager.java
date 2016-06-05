package com.wli.sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.wli.sorm.javabean.Configuration;
import com.wli.sorm.pool.DBConnectionPool;

/**
 * 
 *数据库相关信息管理   
 * @author ali  
 * @version  0.8 
 * 2016年6月2日 下午10:10:57
 */
public class DBManager {
	
	/**
	 * 配置文件对应对象
	 */
	private static Configuration conf;
	
	private static DBConnectionPool pool;
	
	
	
	static{
		Properties pro=new Properties();
		try {
			pro.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		conf=new Configuration();
		conf.setDriver(pro.getProperty("driver"));
		conf.setUrl(pro.getProperty("url"));
		conf.setUser(pro.getProperty("user"));
		conf.setPwd(pro.getProperty("pwd"));
		conf.setUsingDB(pro.getProperty("usingDB"));
		conf.setPoPackage(pro.getProperty("poPackage"));
		conf.setBasePath(pro.getProperty("basePath"));
		conf.setPoolMaxSize(Integer.parseInt(pro.getProperty("poolMaxSize")));
		conf.setPoolMinSize(Integer.parseInt(pro.getProperty("poolMinSize")));
		
		pool=DBConnectionPool.getPool();
	
	};
	
	/**
	 * 
	* @Description: 创建新的数据库连接
	* @param @return  
	* @return Connection 
	*
	 */
	public static Connection createConnection(){
		Connection connection=null;
		try {
			Class.forName(conf.getDriver());
			connection=DriverManager.getConnection(conf.getUrl(), conf.getUser(), conf.getPwd());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
		
	}
	
	/**
	 * 
	* @Description: 从数据库连接池获取连接
	* @param @return  
	* @return Connection 
	*
	 */
	public static Connection getConnectionFromPool(){
		return pool.getConnectionFromPool();
	}
	
	
	/**
	 * 
	* @Description: 关闭Connection（放回连接池） PreparedStatement ResultSet
	* @param @param connection
	* @param @param ps
	* @param @param rs  
	* @return void 
	*
	 */
	public static void close(Connection connection,PreparedStatement ps,ResultSet rs ){
		pool.close(connection);
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	* @Description: 关闭Connection（放回连接池） PreparedStatement 
	* @param @param connection
	* @param @param ps  
	* @return void 
	*
	 */
	public static void close(Connection connection,PreparedStatement ps){
		pool.close(connection);
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	public static Configuration getConf() {
		return conf;
	}

	public static void setConf(Configuration conf) {
		DBManager.conf = conf;
	}
	
	
	
}
