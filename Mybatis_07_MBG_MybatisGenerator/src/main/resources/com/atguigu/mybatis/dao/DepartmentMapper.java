package com.atguigu.mybatis.dao;

import com.atguigu.mybatis.bean.Department;
import java.util.List;

public interface DepartmentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dept
     *
     * @mbg.generated Wed Apr 08 15:37:11 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dept
     *
     * @mbg.generated Wed Apr 08 15:37:11 CST 2020
     */
    int insert(Department record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dept
     *
     * @mbg.generated Wed Apr 08 15:37:11 CST 2020
     */
    Department selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dept
     *
     * @mbg.generated Wed Apr 08 15:37:11 CST 2020
     */
    List<Department> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dept
     *
     * @mbg.generated Wed Apr 08 15:37:11 CST 2020
     */
    int updateByPrimaryKey(Department record);
}