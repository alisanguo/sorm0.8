package com.wli.sorm.core;

/**
 * 
 * Query接口工厂  单例
 * @author ali  
 * @version  0.8 
 * 2016年6月4日 下午4:11:22
 */
public class QueryFactory {
	
	private static QueryFactory factory;
	
	private QueryFactory(){}
	
	public static QueryFactory getFactory(){
		if(factory==null){
			synchronized (QueryFactory.class) {
				if(factory==null){
					factory=new QueryFactory();
				}
			}
		}
		return factory;
	}
	
	public Query createMysqlQuery(){
		return new MysqlQuery();
	}
	
}
