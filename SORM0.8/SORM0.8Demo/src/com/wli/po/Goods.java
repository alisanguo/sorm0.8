package com.wli.po;
import java.util.*;
import java.sql.*;


public class Goods{
	private Integer id;
	private String gname;
	private Integer userId;
	public void setId(Integer id){
		this.id=id;
	}
	public Integer getId(){
		return this.id;
	}
	public void setGname(String gname){
		this.gname=gname;
	}
	public String getGname(){
		return this.gname;
	}
	public void setUserId(Integer userId){
		this.userId=userId;
	}
	public Integer getUserId(){
		return this.userId;
	}

}