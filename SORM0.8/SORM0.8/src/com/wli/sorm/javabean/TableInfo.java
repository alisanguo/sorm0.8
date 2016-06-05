package com.wli.sorm.javabean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 数据库表信息   
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:21:36
 */
public class TableInfo {
	
	private List<ColumnInfo> columnInfos;
	
	private Map<String,ColumnInfo> columns4Map;
	
	private ColumnInfo onlyPriKey;//唯一主键
	
	private List<ColumnInfo> priKeys;//联合主键
	
	private String tname;

	public List<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}

	public void setColumnInfos(List<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}

	

	public ColumnInfo getOnlyPriKey() {
		return onlyPriKey;
	}

	public void setOnlyPriKey(ColumnInfo onlyPriKey) {
		this.onlyPriKey = onlyPriKey;
	}

	public List<ColumnInfo> getPriKeys() {
		return priKeys;
	}

	public void setPriKeys(List<ColumnInfo> priKeys) {
		this.priKeys = priKeys;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}
	
	

	public Map<String, ColumnInfo> getColumns4Map() {
		return columns4Map;
	}

	public void setColumns4Map(Map<String, ColumnInfo> columns4Map) {
		this.columns4Map = columns4Map;
	}

	public TableInfo(String tname,List<ColumnInfo> columnInfos,Map<String, ColumnInfo> columns4Map,List<ColumnInfo> priKeys) {
		super();
		this.columnInfos = columnInfos;
		this.tname = tname;
		this.columns4Map=columns4Map;
		this.priKeys=priKeys;
	}
	
	public TableInfo(String tname,List<ColumnInfo> columnInfos,Map<String, ColumnInfo> columns4Map,ColumnInfo onlyPriKey) {
		super();
		this.columnInfos = columnInfos;
		this.tname = tname;
		this.columns4Map=columns4Map;
		this.onlyPriKey=onlyPriKey;
	}
	
	
	public TableInfo() {
	}
	
	
}
