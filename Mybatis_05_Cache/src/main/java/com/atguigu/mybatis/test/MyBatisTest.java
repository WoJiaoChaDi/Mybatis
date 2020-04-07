package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.EmployeeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisTest {
	

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 * 两级缓存：
	 * 一级缓存：（本地缓存）：sqlSession级别的缓存。一级缓存是一直开启的；SqlSession级别的一个Map
	 * 		与数据库同一次会话期间查询到的数据会放在本地缓存中。
	 * 		以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库；
	 *
	 * 		一级缓存失效情况（没有使用到当前一级缓存的情况，效果就是，还需要再向数据库发出查询）：
	 * 		1、sqlSession不同。
	 * 		2、sqlSession相同，查询条件不同.(当前一级缓存中还没有这个数据)
	 * 		3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)
	 * 		4、sqlSession相同，手动清除了一级缓存（缓存清空）  openSession.clearCache();
	 */

	@Test
	public void testFirstLevelCache() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee emp01 = mapper.getEmpById(1);
			System.out.println(emp01);
			//因为是一样的对象，所有默认使用一级缓存，不用查询两次
			Employee emp02 = mapper.getEmpById(1);
			System.out.println(emp02);
			//结果为true，两次相同的查询默认开启一级缓存
			System.out.println(emp01 == emp02);

			//失效情况
			System.out.println("sqlSession不同");
			//1、sqlSession不同。
			// 第一次查询的结果，只会放在第一次会话的一级缓存中。
			// 第二次查询的结果，只会放在第二次会话的一级缓存中。
			SqlSession openSession2 = sqlSessionFactory.openSession();
			EmployeeMapper mapper2 = openSession2.getMapper(EmployeeMapper.class);
			Employee emp03 = mapper2.getEmpById(1);
			System.out.println(emp03);
			System.out.println(emp01 == emp03);
			openSession2.close();

			System.out.println("sqlSession相同，查询条件不同，在一级缓存中不同");
			//2、sqlSession相同，查询条件不同，在一级缓存中不同
			Employee emp04 = mapper.getEmpById(4);
			System.out.println(emp04);
			System.out.println(emp01 == emp04);

			System.out.println("sqlSession相同，两次查询之间执行了增删改操作");
			//3、sqlSession相同，两次查询之间执行了增删改操作(这次增删改可能对当前数据有影响)
			mapper.addEmp(new Employee(null, "testCache", "cache", "1"));
			System.out.println("数据添加成功");
			Employee emp05 = mapper.getEmpById(5);
			System.out.println(emp01 == emp05);

			System.out.println("sqlSession相同，手动清除了一级缓存（缓存清空）");
			//4、sqlSession相同，手动清除了一级缓存（缓存清空）
			openSession.clearCache();
			Employee emp06 = mapper.getEmpById(6);
			System.out.println(emp01 == emp06);

		}finally{
			openSession.close();
		}
	}
}
