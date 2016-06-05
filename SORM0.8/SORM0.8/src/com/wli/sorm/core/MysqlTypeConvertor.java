package com.wli.sorm.core;

/**
 * 
 * mysql数据库类型与java数据类型互相转换  
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:07:45
 */
public class MysqlTypeConvertor implements TypeConvertor{
	
	/**
	 * mysql数据库类型转为java类型
	* @param @return  
	* @return Connection 
	*
	 */
	@Override
	public String dbTypeToJavaType(String dbType) {
		if("tinyint".equalsIgnoreCase(dbType)||"int".equalsIgnoreCase(dbType)){
			return "Integer";
		}else if("bigint".equalsIgnoreCase(dbType)){
			return "Long";
		}else if("char".equalsIgnoreCase(dbType)||"varchar".equalsIgnoreCase(dbType)){
			return "String";
		}else if("date".equalsIgnoreCase(dbType)){
			return "java.sql.Date";
		}else if("timestamp".equalsIgnoreCase(dbType)){
			return "java.util.Date";
		}
		return null;
	}
	
	/**
	 * 
	* @Description: java转mysql数据库类型
	* @param @return  
	* @return Connection 
	*
	 */
	@Override
	public String javaTypeToDbType(String javaType) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
