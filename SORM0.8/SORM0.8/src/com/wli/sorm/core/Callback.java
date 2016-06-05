package com.wli.sorm.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 
 * 回调接口，用于数据库操作 
 * @author ali
 * @version  0.8 
 * 2016年6月2日 下午9:48:15
 */
public interface Callback {
	/**
	 * 
	* @Description: 回调方法
	* @param @return  
	* @return Connection 
	*
	 */
	public Object doExecute(Connection conn,PreparedStatement ps,ResultSet rs);
}
