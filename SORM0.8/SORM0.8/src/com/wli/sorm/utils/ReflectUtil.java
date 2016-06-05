package com.wli.sorm.utils;

import java.lang.reflect.Method;

@SuppressWarnings("all")
public class ReflectUtil {
	
	public static Object invokeGetMethod(Object object,String fieldName){
		try {
			Class clazz=object.getClass();
			Method method=clazz.getDeclaredMethod("get"+StringUtil.FirstToUpperCase(fieldName), null);
			return method.invoke(object, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static void invokeSetMethod(Object object,String fieldName,Object fieldValue){
		try {
			Class clazz=object.getClass();
			Method method=clazz.getDeclaredMethod("set"+StringUtil.FirstToUpperCase(fieldName), fieldValue.getClass());
			method.invoke(object, fieldValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

