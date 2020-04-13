package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapper {

	//接口式编程
	public Employee getEmpById(Integer id);

}
