package com.wli.sorm.javabean;

/**
 * 
 * 数据库表字段信息 
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:19:34
 */
public class ColumnInfo {
	
	private String name;//字段名
	
	private int key;//0普通字段1主键2外键
	
	private String type;//字段类型

	
	public ColumnInfo(String name,String type,int key) {
		super();
		this.name = name;
		this.key = key;
		this.type = type;
	}
	
	public ColumnInfo() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
