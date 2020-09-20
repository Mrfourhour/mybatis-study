package com.xinyet.param.mapper;


import com.xinyet.param.entity.Employee;
import com.xinyet.param.to.IdAndNameTo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<Employee> getEmployees();

    Employee getEmployeeById(Integer id);

    Employee getEmployeeByIdAndName(Integer id, String name);

    Employee getEmployeeByTo(IdAndNameTo to);
}
