package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;

public interface EmployeeMapperPlus {
	
	public Employee getEmpById(Integer id);

	public Employee getEmpById_resultMap(Integer id);

    public Employee getEmpAndDept(Integer id);

    public Employee getEmpAndDept2(Integer id);

    public Employee getEmpByIdStep(Integer id);

    public Employee getEmpById_MyEmpDis(Integer id);

}
