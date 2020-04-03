package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapperPlus {
	
	public Employee getEmpById(Integer id);

	public Employee getEmpById_resultMap(Integer id);

    public Employee getEmpAndDept(Integer id);

}
