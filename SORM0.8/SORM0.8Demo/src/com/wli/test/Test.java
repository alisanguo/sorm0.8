package com.wli.test;

import java.util.List;

import com.wli.po.Goods;
import com.wli.po.User;
import com.wli.sorm.core.MysqlQuery;
import com.wli.sorm.core.Query;
import com.wli.vo.GoodsVo;

public class Test {
	
	public static void main(String[] args) {
		///TableContext.loadPOTables();
		//TableContext.updateJavaPOFile();
		testSelect();
	}
	
	public static void testInsert(){
		long begin=System.currentTimeMillis();
		Query query=new MysqlQuery();
		for(int i=1;i<=2000;i++){
			User user=new User();
			Goods goods=new Goods();
			goods.setGname("gname"+i);
			user.setName("admin"+i);
			query.insert(user);
			query.insert(goods);
		}
		System.out.println(System.currentTimeMillis()-begin);//4170 15254
		
	}
	
	public static void testDelete(){
		Query query=new MysqlQuery();
		System.out.println(query.delete(User.class, 4000));
	}
	
	public static void testSelect(){
		Query query=new MysqlQuery();
		String sql="select g.id 'id',g.gname 'gname',u.id 'userId',u.name 'userName' from goods g join User u on g.userId=u.id";
		List<GoodsVo> list=query.queryRows(sql, GoodsVo.class, null);
		for(GoodsVo gv:list){
			System.out.println(gv.getGname());
		}
	}
	
	
}
