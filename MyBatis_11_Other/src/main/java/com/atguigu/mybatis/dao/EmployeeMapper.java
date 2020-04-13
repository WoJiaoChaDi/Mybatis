package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import com.atguigu.mybatis.bean.OraclePage;

import java.util.List;

public interface EmployeeMapper {

	//接口式编程
	public Employee getEmpById(Integer id);

	public List<Employee> getEmps();

    public Long addEmp(Employee employee);

    //Oracle分页查询数据
	public void getPageByProcedure(OraclePage page);
}
