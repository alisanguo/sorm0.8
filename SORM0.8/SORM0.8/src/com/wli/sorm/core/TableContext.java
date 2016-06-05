package com.wli.sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.jdbc.StringUtils;
import com.wli.sorm.javabean.ColumnInfo;
import com.wli.sorm.javabean.TableInfo;
import com.wli.sorm.utils.JavaFileUtil;
import com.wli.sorm.utils.StringUtil;

/***
 * 
 * 数据库表上下文类 
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:15:22
 */
@SuppressWarnings("all")
public class TableContext {

	/**
	 * 表信息
	 */
	public static  Map<String,TableInfo>  tables = new HashMap<String,TableInfo>();
	
	/**
	 * 表信息与po类对应map
	 */
	public static  Map<Class,TableInfo>  poClassTableMap = new HashMap<Class,TableInfo>();
	
	private TableContext(){}
	
	static {
		try {
			Connection con = DBManager.getConnectionFromPool();
			DatabaseMetaData dbmd = con.getMetaData(); 
			
			ResultSet tableRet = dbmd.getTables(null, "%","%",new String[]{"TABLE"}); 
			
			while(tableRet.next()){
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				
				TableInfo ti = new TableInfo(tableName, new ArrayList<ColumnInfo>(),new HashMap<String,ColumnInfo>(),new ArrayList<ColumnInfo>());
				tables.put(tableName, ti);
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");  
				while(set.next()){
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"), 
							set.getString("TYPE_NAME"), 0);
					ti.getColumns4Map().put(set.getString("COLUMN_NAME"), ci);
					ti.getColumnInfos().add(ci);
				}
				
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);  
				while(set2.next()){
					ColumnInfo ci2 = (ColumnInfo) ti.getColumns4Map().get(set2.getObject("COLUMN_NAME"));
					ci2.setKey(1);  
					ti.getPriKeys().add(ci2);
				}
				
				if(ti.getPriKeys().size()>0){  
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		updateJavaPOFile();
		loadPOTables();
		

	}
	
	/**
	 * 
	* @Description:更新表到po的映射
	* @param   
	* @return void 
	*
	 */
	public static void updateJavaPOFile(){
		Map<String,TableInfo> map = TableContext.tables;
		JavaFileUtil.createAllJavaPOFile();
	}
	
	/**
	 * 
	* @Description: 加载数据库表信息
	* @param   
	* @return void 
	*
	 */
	public static void loadPOTables(){
		
		for(TableInfo tableInfo:tables.values()){
			try {
				Class c = Class.forName(DBManager.getConf().getPoPackage()
						+"."+StringUtil.FirstToUpperCase(tableInfo.getTname()));
				poClassTableMap.put(c, tableInfo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}

}
