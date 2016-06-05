package com.wli.sorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wli.sorm.core.DBManager;
import com.wli.sorm.utils.JdbcUtil;

/**
 * 
 * 数据库连接池
 * @author ali  
 * @version  0.8 
 * 2016年6月2日 下午10:14:05
 */
@SuppressWarnings("all")
public class DBConnectionPool {
	
	/**
	 * 连接集合
	 */
	private static List<Connection> conns=new ArrayList<Connection>();
	
	/**
	 * 最大连接数
	 */
	private static int poolMaxSize;
	/**
	 * 最小连接数
	 */
	private static int poolMinSize;
	
	private static DBConnectionPool pool;
	
	
	private DBConnectionPool(){}
	
	static{
		poolMaxSize=DBManager.getConf().getPoolMaxSize();
		poolMinSize=DBManager.getConf().getPoolMinSize();
		while(conns.size()<poolMinSize){
			Connection coon=DBManager.createConnection();
			conns.add(coon);
		}
	}
	
	/**
	 * 
	* @Description: 从连接池得到一个连接
	* @param @return  
	* @return Connection 
	*
	 */
	public synchronized  Connection getConnectionFromPool(){
		int size=conns.size();
		Connection conn=null;
		if(size>0){
			int lastIndex=size-1;
			conn= conns.get(lastIndex);
			conns.remove(lastIndex);
		}
		return conn;
	}
	
	
	public synchronized void close(Connection conn){
		if(conns.size()<poolMaxSize){
			conns.add(conn);
		}else{
			try {
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static DBConnectionPool getPool(){
		if(pool==null){
			synchronized (DBConnectionPool.class) {
				if(pool==null){
					pool=new DBConnectionPool();
				}
			}
		}
		return pool;
	}
	
	
}
