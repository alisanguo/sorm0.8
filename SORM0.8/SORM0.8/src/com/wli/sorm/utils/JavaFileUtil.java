package com.wli.sorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.wli.sorm.core.DBManager;
import com.wli.sorm.core.MysqlTypeConvertor;
import com.wli.sorm.core.TableContext;
import com.wli.sorm.core.TypeConvertor;
import com.wli.sorm.javabean.ColumnInfo;
import com.wli.sorm.javabean.TableInfo;

/**
 * 
 * 生成po类工具  
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:22:56
 */
public class JavaFileUtil {
	
	
	/**
	 * 
	* @Description: 创建所有po类文件
	* @param   
	* @return void 
	*
	 */
	public static void createAllJavaPOFile(){
		BufferedWriter bw=null;
		for(TableInfo tableInfo:TableContext.tables.values()){
			String src=createJavaSrc(tableInfo, new MysqlTypeConvertor());
			String dirPath=DBManager.getConf().getBasePath()+"/"+DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
			File dir=new File(dirPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			
			File distFile=new File(dirPath+"/"+StringUtil.FirstToUpperCase(tableInfo.getTname())+".java");
			try {
				bw=new BufferedWriter(new FileWriter(distFile));
				bw.write(src);
				bw.flush();
				System.out.println(tableInfo.getTname()+"��Ӧpojo�ഴ���ɹ�\n");;
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		
		}
		
	}
	
	/**
	 * 
	* @Description: 生成java源码
	* @param @param tableInfo
	* @param @param convertor
	* @param @return  
	* @return String 
	*
	 */
	private static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor){
		List<JavaFieldSetAndGet> javaFieldSetAndGets=new ArrayList<JavaFieldSetAndGet>();
		for(ColumnInfo c:tableInfo.getColumnInfos()){
			javaFieldSetAndGets.add(createJavaFieldSetAndGet(c, convertor));
		}
		
		StringBuilder builder=new StringBuilder();
		builder.append("package ").append(DBManager.getConf().getPoPackage())
				.append(";\n")
				.append("import java.util.*;\n")
				.append("import java.sql.*;\n\n\n")
				.append("public class ").append(StringUtil.FirstToUpperCase(tableInfo.getTname()))
				.append("{\n");
		
		
		for(JavaFieldSetAndGet j:javaFieldSetAndGets){
			builder.append(j.getFieldInfo());
		}
		
		for(JavaFieldSetAndGet j:javaFieldSetAndGets){
			builder.append(j.getFieldSetInfo()).append(j.getFieldGetInfo());
			
		}
		
		builder.append("\n}");
		
		return builder.toString();
	}
	
	/**
	 * 
	* @Description: 创建set get信息
	* @param @param columnInfo
	* @param @param convertor
	* @param @return  
	* @return JavaFieldSetAndGet 
	*
	 */
	private static JavaFieldSetAndGet createJavaFieldSetAndGet(ColumnInfo columnInfo,TypeConvertor convertor){
		JavaFieldSetAndGet jfs=new JavaFieldSetAndGet();
		StringBuilder builder1=new StringBuilder();
		
		String fielsInfo=builder1.append("\tprivate ")
				.append(convertor.dbTypeToJavaType(columnInfo.getType()))
				.append(" ").append(columnInfo.getName())
				.append(";\n")
				.toString();
		jfs.setFieldInfo(fielsInfo);
		
		StringBuilder builder2=new StringBuilder();
		String setInfo=builder2.append("\tpublic ")
							  .append("void ")
							  .append("set").append(StringUtil.FirstToUpperCase(columnInfo.getName()))
							  .append("(").append(convertor.dbTypeToJavaType(columnInfo.getType())).append(" ")
							  .append(columnInfo.getName()).append("){\n")
							  .append("\t\tthis.").append(columnInfo.getName())
							  .append("=").append(columnInfo.getName())
							  .append(";\n\t}\n")
							  .toString();
		
		jfs.setFieldSetInfo(setInfo);
							
		StringBuilder builder3=new StringBuilder();
		String getInfo=builder3.append("\tpublic ")
				  .append(convertor.dbTypeToJavaType(columnInfo.getType()))
				  .append(" get").append(StringUtil.FirstToUpperCase(columnInfo.getName()))
				  .append("(){\n")
				  .append("\t\treturn ").append("this.").append(columnInfo.getName())
				  .append(";\n\t}\n")
				  .toString();
		
		jfs.setFieldGetInfo(getInfo);
		
		
		return jfs;
	}

	
	
	/**
	 * 
	 * set get方法信息对象  
	 * @author ali  
	 * @version  0.8 
	 * 2016年6月4日 下午4:24:48
	 */
	private static class JavaFieldSetAndGet{
		
		private String fieldInfo;
		
		private String fieldGetInfo;
		
		private String fieldSetInfo;

		public String getFieldInfo() {
			return fieldInfo;
		}

		public void setFieldInfo(String fieldInfo) {
			this.fieldInfo = fieldInfo;
		}

		public String getFieldGetInfo() {
			return fieldGetInfo;
		}

		public void setFieldGetInfo(String fieldGetInfo) {
			this.fieldGetInfo = fieldGetInfo;
		}

		public String getFieldSetInfo() {
			return fieldSetInfo;
		}

		public void setFieldSetInfo(String fieldSetInfo) {
			this.fieldSetInfo = fieldSetInfo;
		}
		
	}
}
