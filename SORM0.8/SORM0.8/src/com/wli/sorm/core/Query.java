package com.wli.sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.wli.sorm.javabean.TableInfo;
import com.wli.sorm.pool.DBConnectionPool;
import com.wli.sorm.utils.JdbcUtil;
import com.wli.sorm.utils.ReflectUtil;

/**
 * 统一查询抽象类，提供基本数据库操作
 *   
 * @author ali
 * @version 0.8  
 * 2016年6月2日 下午9:22:16
 */
@SuppressWarnings("all")
public abstract class Query {
	
	/**
	 * 
	* @Description: 查询模板，便于查询代码重用
	* @param @param clazz
	* @param @param sql
	* @param @param param
	* @param @param callback 回调对象
	* @param @return  
	* @return Object 
	*
	 */
	public Object queryTemplate(Class clazz,String sql,Object[] param,Callback callback){
		Connection connection=getConnectionFromPool();
		PreparedStatement ps=null;
		ResultSet resultSet=null;
		try {
			ps=connection.prepareStatement(sql);
			JdbcUtil.handleParam(ps, param);
			resultSet=ps.executeQuery();
			return callback.doExecute(connection, ps, resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			DBManager.close(connection, ps, resultSet);
		}
	} 
	
	
	
	/**
	 * 
	* @Description: 插入一条记录
	* @param @param object 表对应实体
	* @param @return  
	* @return int 插入记录数
	*
	 */
	public int insert(Object object) {
		Class clazz=object.getClass();
		TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
		StringBuilder builder=new StringBuilder("insert into ");
		builder.append(tableInfo.getTname()).append("(");
		
		Field[] fields=clazz.getDeclaredFields();
		int fieldCount=0;
		List<Object> params=new ArrayList<Object>();
		for(Field field:fields){
			Object fieldValue=ReflectUtil.invokeGetMethod(object, field.getName());
			if(field!=null){
				fieldCount++;
				builder.append(field.getName()).append(",");
				params.add(fieldValue);
			}
			
		}
		builder.setCharAt(builder.length()-1, ')');
		
		builder.append(" values(");
		for(int i=0;i<fieldCount;i++){
			builder.append("?,");
		}
		builder.setCharAt(builder.length()-1, ')');
		
		return  executeDML(builder.toString(),params.toArray());
		
	}
	
	/**
	 * 
	* @Description:查询多行记录
	* @param @param sql
	* @param @param clazz 表对应实体class对象
	* @param @param param 参数列表
	* @param @return  
	* @return List 
	*
	 */
	public List queryRows(String sql,Class clazz,Object[] param){
		
		return (List) queryTemplate(clazz, sql, param, new Callback() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				List list=null;
				try {
					ResultSetMetaData metaData=rs.getMetaData();
					int columnCount=metaData.getColumnCount();
					while(rs.next()){
						Object object=null;
						try {
							object = clazz.newInstance();
						} catch (Exception e1) {
							e1.printStackTrace();
						} 
						for(int i=1;i<=columnCount;i++){
							if(list==null){
								list=new ArrayList<>();
							}
							try {
								String columnName=metaData.getColumnLabel(i);
								Object columnValue=rs.getObject(i);
								ReflectUtil.invokeSetMethod(object, columnName, columnValue);
								//list.add(object);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						list.add(object);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return list!=null&&list.size()>0?list:null;
			}
		});
		
	}
	
	
	/**
	 * 
	* @Description:查询唯一记录
	* @param @param sql
	* @param @param clazz 表对应实体class对象
	* @param @param param 参数列表
	* @param @return  
	* @return Object 
	*
	 */
	public Object queryUniqueRow(String sql,Class clazz,Object[] param){
		List list=queryRows(sql, clazz, param);
		return list!=null&&list.size()>0?list.get(0):null;
	
	}
	
	/**
	 * 
	* @Description: 根据id查询
	* @param @param clazz 表对应实体class对象
	* @param @param id 主键
	* @param @return  
	* @return Object 
	*
	 */
	public Object queryById(Class clazz,Object id){
		TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
		String sql="select * from "+tableInfo.getTname()+" where "+tableInfo.getOnlyPriKey().getName()+"=?";
		return queryUniqueRow(sql, clazz, new Object[]{id});
	}
	
	/**
	 * 
	* @Description: 查询类型为数字的值
	* @param @param sql
	* @param @param param 
	* @param @return  
	* @return Number 
	*
	 */
	public Number queryNumber(String sql,Object[] param) {
		Object value=queryValue(sql, param);
		return value!=null?(Number)value:null;
	}
	
	
	/**
	 * 
	* @Description:查询值
	* @param @param sql
	* @param @param param 参数列表
	* @return Object 
	*
	 */
	public Object queryValue(String sql,Object[] param){
		return queryTemplate(null, sql, param, new Callback() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				try {
					rs.next();
					return rs!=null?rs.getObject(1):null;
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
		});
	}
	
	/**
	 * 
	* @Description:根据id删除对应的记录
	* @param @param clazz 表对应实体class对象
	* @param @param id 主键
	* @return int 根据id删除对应的记录
	*
	 */
	public int delete(Class clazz,Object id) {
		StringBuilder builder=new StringBuilder("delete from ");
		TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
		builder.append(tableInfo.getTname())
		       .append(" where ")
		       .append(tableInfo.getOnlyPriKey().getName())
		       .append("=?");
		String sql=builder.toString();
		return executeDML(sql, new Object[]{id});
	}

	/**
	 * 
	* @Description: 删除给定实体对应的记录
	* @param @param object 给定实体
	* @return int 删除记录行数
	*
	 */
	public int delete(Object object) {
		Class clazz=object.getClass();
		TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
		Object idValue=ReflectUtil.invokeGetMethod(object, tableInfo.getOnlyPriKey().getName());
		delete(clazz, idValue);
		return 0;
	}
	
	/**
	 * 
	* @Description: 修改
	* @param @param object 实体对象
	* @param @param fieldNames 需要修改的字段名称数组
	* @return int 影响记录行数
	*
	 */
	
	public int update(Object object,String[] fieldNames) {
		Class clazz=object.getClass();
		TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
		StringBuilder builder=new StringBuilder("update ");
		builder.append(tableInfo.getTname())
		       .append(" set ");
		
		int fieldCount=0;
		List<Object> params=new ArrayList<Object>();
		for(String field:fieldNames){
			Object fieldValue=ReflectUtil.invokeGetMethod(object, field);
			if(fieldValue!=null){
				fieldCount++;
				builder.append(field).append("=?,");
				params.add(fieldValue);
			}
		}
		builder.setCharAt(builder.length()-1, ' ');
		
		String priKey=tableInfo.getOnlyPriKey().getName();
		builder.append(" where ")
		       .append(priKey)
		       .append("=?");
		
		params.add(ReflectUtil.invokeGetMethod(object, priKey));
		
		return executeDML(builder.toString(),params.toArray());
	}
	
	
	
	/**
	 * 
	* @Description: update ,insert,delete操作
	* @param @param sql
	* @param @param param 参数列表
	* @return int 影响记录行数
	*
	 */
	public int executeDML(String sql, Object[] param){
		Connection connection=getConnectionFromPool();
		PreparedStatement ps=null;
		int result=0;
		try {
			ps=connection.prepareStatement(sql);
			JdbcUtil.handleParam(ps,param);
			result=ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBManager.close(connection, ps);
		}
		return result;
	}
	
	private Connection getConnectionFromPool(){
		return DBManager.getConnectionFromPool();
	}
	
	
	/**
	 * 
	* @Description: 分页查询
	* @param @param sql sql语句
	* @param @param param 参数列表
	* @param @param beginIndex 开始索引，从1开始
	* @param @param offset 偏移量，即查询条数
	* @return List 记录集合
	*
	 */
	public abstract List queryPage(String sql, Object[] param,int beginIndex,int offset);
	
}
