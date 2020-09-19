package com.xinyet.param.mapper;


import com.xinyet.param.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<Employee> getEmployees();
}
