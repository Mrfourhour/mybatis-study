package com.xinyet.param;

import com.xinyet.param.entity.Employee;
import com.xinyet.param.mapper.EmployeeMapper;
import com.xinyet.param.to.IdAndNameTo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyTest {

    private SqlSessionFactory getSqlSessionFactory() {
        //加载xml配置文件
        String resource = "mybatis.xml";
        //获取输入流
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取SqlSessionFactory
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void test01() {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
            sqlSession = sqlSessionFactory.openSession();
            //没有参数的情况
            System.out.println("没有参数的情况");
            EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
            List<Employee> list = mapper.getEmployees();
            for (Employee employee : list) {
                System.out.println(employee);
            }
            //有单个参数的情况
            System.out.println("有单个参数的情况");
            Employee id = mapper.getEmployeeById(1);
            System.out.println(id);

            //有多个参数的情况
            System.out.println("有多个参数的情况");
            Employee idAndName = mapper.getEmployeeByIdAndName(1, "张三");
            System.out.println(idAndName);


            //有pojo的情况
            System.out.println("有pojo的情况");
            IdAndNameTo to = new IdAndNameTo();
            to.setId(1);
            to.setName("张三");
            Employee employeeOfTo = mapper.getEmployeeByTo(to);
            System.out.println(employeeOfTo);




            //手动提交事务
            //sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

}
