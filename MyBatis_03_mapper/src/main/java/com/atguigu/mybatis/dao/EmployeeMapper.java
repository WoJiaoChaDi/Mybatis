package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Param;

public interface EmployeeMapper {

	public Employee getEmpById(Integer id);

	public Long addEmp(Employee employee);

	public boolean updateEmp(Employee employee);

	public void deleteEmpById(Integer id);

	public Employee getEmpByIdAndLastName(@Param("id")Integer id, @Param("lastName")String lastName);

}
