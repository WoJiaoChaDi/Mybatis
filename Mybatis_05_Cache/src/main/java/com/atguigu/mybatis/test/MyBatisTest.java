package com.atguigu.mybatis.test;

import com.atguigu.mybatis.bean.Department;
import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.dao.DepartmentMapper;
import com.atguigu.mybatis.dao.EmployeeMapper;
import com.atguigu.mybatis.dao.EmployeeMapperAnnotation;
import com.atguigu.mybatis.dao.EmployeeMapperDynamicSQL;
import com.atguigu.mybatis.dao.EmployeeMapperPlus;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1、接口式编程
 * 	原生：		Dao		====>  DaoImpl
 * 	mybatis：	Mapper	====>  xxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样她都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		（将接口和xml进行绑定）
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息：
 * 					将sql抽取出来。	
 * 
 * 
 * @author lfy
 *
 */
public class MyBatisTest {
	

	public SqlSessionFactory getSqlSessionFactory() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		return new SqlSessionFactoryBuilder().build(inputStream);
	}

	/**
	 * 1、根据xml配置文件（全局配置文件）创建一个SqlSessionFactory对象 有数据源一些运行环境信息
	 * 2、sql映射文件；配置了每一个sql，以及sql的封装规则等。 
	 * 3、将sql映射文件注册在全局配置文件中
	 * 4、写代码：
	 * 		1）、根据全局配置文件得到SqlSessionFactory；
	 * 		2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
	 * 			一个sqlSession就是代表和数据库的一次会话，用完关闭
	 * 		3）、使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
	 * 
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException {

		// 2、获取sqlSession实例，能直接执行已经映射的sql语句
		// sql的唯一标识：statement Unique identifier matching the statement to use.
		// 执行sql要用的参数：parameter A parameter object to pass to the statement.
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			Employee employee = openSession.selectOne(
					"com.atguigu.mybatis.dao.EmployeeMapper.selectEmp", 1);
			System.out.println(employee);
		} finally {
			openSession.close();
		}

	}

	@Test
	public void test01() throws IOException {
		// 1、获取sqlSessionFactory对象
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		// 2、获取sqlSession对象
		SqlSession openSession = sqlSessionFactory.openSession();
		try {
			// 3、获取接口的实现类对象
			//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			Employee employee = mapper.getEmpById(1);
			System.out.println(mapper.getClass());
			System.out.println(employee);
		} finally {
			openSession.close();
		}

	}
	
	@Test
	public void test02() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperAnnotation mapper = openSession.getMapper(EmployeeMapperAnnotation.class);
			Employee empById = mapper.getEmpById(1);
			System.out.println(empById);
		}finally{
			openSession.close();
		}
	}

	/**
	 * 测试增删改
	 * 1、mybatis允许增删改直接定义以下类型返回值
	 * 		Integer、Long、Boolean、void
	 * 2、我们需要手动提交数据
	 * 		sqlSessionFactory.openSession();===》手动提交
	 * 		sqlSessionFactory.openSession(true);===》自动提交
	 * @throws IOException
	 */
	@Test
	public void test03() throws IOException{

		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		//1、获取到的SqlSession不会自动提交数据
		SqlSession openSession = sqlSessionFactory.openSession();

		try{
			EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
			//测试添加
			Employee employee = new Employee(null, "jerry4",null, "1");
			mapper.addEmp(employee);
			System.out.println(employee.getId());

			//测试修改
			//Employee employee = new Employee(2, "Tom", "jerry@atguigu.com", "0");
			//boolean updateEmp = mapper.updateEmp(employee);
			//System.out.println(updateEmp);
			//测试删除
			mapper.deleteEmpById(2);
			//2、手动提交数据
			openSession.commit();
		}finally{
			openSession.close();
		}
	}

    @Test
    public void test04() throws IOException{

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
        //1、获取到的SqlSession不会自动提交数据
        SqlSession openSession = sqlSessionFactory.openSession();

        try{
            EmployeeMapper mapper = openSession.getMapper(EmployeeMapper.class);
            //Employee employee = mapper.getEmpByIdAndLastName(1, "tom");

            Map<String, Object> map = new HashMap<>();
            map.put("id", 1);
            map.put("lastName", "tom");
            map.put("tableName", "tbl_employee");
            Employee employee = mapper.getEmpByMap(map);
            System.out.println(employee);

            System.out.println("模糊查询List");
            List<Employee> like = mapper.getEmpsByLastNameLike("%e%");
			for (Employee employee2 : like) {
				System.out.println(employee2);
			}

            System.out.println("返回Map类型的");
			Map<String, Object> map1 = mapper.getEmpByIdReturnMap(1);
			System.out.println(map1);

			//返回Map类型，Key值为
			Map<String, Employee> map2 = mapper.getEmpByLastNameLikeReturnMap("%e%");
			System.out.println(map2);

        }finally{
            openSession.close();
        }
    }

	@Test
	public void test05() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
			//Employee empById = mapper.getEmpById(1);
			//System.out.println(empById);

			//Employee empAndDept = mapper.getEmpAndDept(1);
			//System.out.println(empAndDept);
			//System.out.println(empAndDept.getDept());

			//Employee empAndDept2 = mapper.getEmpAndDept2(3);
			//System.out.println(empAndDept2);
			//System.out.println(empAndDept2.getDept());

			Employee employee = mapper.getEmpByIdStep(3);
			System.out.println(employee);
			System.out.println(employee.getDept());
		}finally{
			openSession.close();
		}
	}

	@Test
	public void test06() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();

		try{
			DepartmentMapper mapper = openSession.getMapper(DepartmentMapper.class);
			Department department = mapper.getDeptByIdPlus(1);
			System.out.println(department);
			System.out.println(department.getEmps());

			System.out.println("=====================");

			Department deptByIdStep = mapper.getDeptByIdStep(1);
			System.out.println(deptByIdStep.getDepartmentName());
			System.out.println(deptByIdStep.getEmps());
		}finally{
			openSession.close();
		}
	}

	@Test
	public void test_discriminator() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();

		try{
			EmployeeMapperPlus mapper = openSession.getMapper(EmployeeMapperPlus.class);
			Employee employee = mapper.getEmpById_MyEmpDis(2);
			System.out.println(employee);
		}finally{
			openSession.close();
		}
	}

	@Test
	public void testDynamicSql() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
			//select * from tbl_employee where id=? and last_name like ?
			//测试if\where
			Employee employee = new Employee(null, "%to%", null, null);
			List<Employee> emps = mapper.getEmpsByConditionIf(employee );
			for (Employee emp : emps) {
				System.out.println(emp);
			}

			System.out.println("===测试Trim===");
			//测试Trim
			List<Employee> emps2 = mapper.getEmpsByConditionTrim(employee);
			for (Employee emp : emps2) {
				System.out.println(emp);
			}


			System.out.println("===测试choose===");
			//测试choose
			employee = new Employee(null, null, null, null);
			List<Employee> list = mapper.getEmpsByConditionChoose(employee);
			for (Employee emp : list) {
				System.out.println(emp);
			}

			System.out.println("===测试set标签===");
			//测试set标签
			employee = new Employee(3, "改名叫张三", null, null);
			mapper.updateEmp(employee);
			openSession.commit();

			System.out.println("===测试foreach标签===");
			List<Employee> list2 = mapper.getEmpsByConditionForeach(Arrays.asList(3,2));
			for (Employee emp : list2) {
				System.out.println(emp);
			}

		}finally{
			openSession.close();
		}
	}

	@Test
	public void testBatchSave() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
			List<Employee> emps = new ArrayList<>();
			emps.add(new Employee(null, "smith0x1", "smith0x1@atguigu.com", "1",new Department(1)));
			emps.add(new Employee(null, "allen0x1", "allen0x1@atguigu.com", "0",new Department(1)));
			mapper.addEmps(emps);
			openSession.commit();
		}finally{
			openSession.close();
		}
	}

	@Test
	public void testInnerParam() throws IOException{
		SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
		SqlSession openSession = sqlSessionFactory.openSession();
		try{
			EmployeeMapperDynamicSQL mapper = openSession.getMapper(EmployeeMapperDynamicSQL.class);
			Employee employee2 = new Employee();
			employee2.setLastName("e");
			List<Employee> list = mapper.getEmpsTestInnerParameter(employee2);
			for (Employee employee : list) {
				System.out.println(employee);
			}
		}finally{
			openSession.close();
		}
	}
}