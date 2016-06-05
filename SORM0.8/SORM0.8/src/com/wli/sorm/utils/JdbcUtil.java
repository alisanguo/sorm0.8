package com.wli.sorm.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("all")
public class JdbcUtil {
	
	public static Connection getConnection(){
		return null;
	}

	public static void handleParam(PreparedStatement ps,Object[] param) {
		if(param!=null){
			for(int i=0;i<param.length;i++){
				try {
					ps.setObject(i+1, param[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	
}
