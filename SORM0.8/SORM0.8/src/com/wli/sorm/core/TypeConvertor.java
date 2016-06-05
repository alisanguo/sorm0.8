package com.wli.sorm.core;

/**
 * 
 * 数据库类型与java类型转换接口  
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:18:09
 */
public interface TypeConvertor {
	

	/**
	 *数据库类型转为java类型
	* @param @return  
	* @return Connection 
	*
	 */
	public String dbTypeToJavaType(String dbType);
	
	/**
	 * 
	* @Description: java转数据库类型
	* @param @return  
	* @return Connection 
	*
	 */
	public String javaTypeToDbType(String javaType);
}
