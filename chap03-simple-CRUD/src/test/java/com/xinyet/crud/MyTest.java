package com.xinyet.crud;

import com.xinyet.crud.mapper.UserMapper;
import com.xinyet.crud.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MyTest {
    @Test
    public void getAllUser() throws IOException {
        //加载xml配置文件
        String resource = "mybatis.xml";
        //获取输入流
        InputStream inputStream = null;
        SqlSession session = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            //获取SqlSessionFactory
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            //获取SqlSession
            session = sqlSessionFactory.openSession();

            //不推荐使用
            List<User> users = session.selectList("com.xinyet.crud.mapper.UserMapper.getAllUser");
            for (User user1 : users) {
                System.out.println(user1);
            }

            System.out.println("=========================================");

            //推荐使用
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> userList = mapper.getAllUser();
            for (User user1 : userList) {
                System.out.println(user1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private SqlSessionFactory getSqlSessionFactory() throws IOException {
        //加载xml配置文件
        String resource = "mybatis.xml";
        //获取输入流
        InputStream inputStream = null;
        inputStream = Resources.getResourceAsStream(resource);
        //获取SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;
    }

    @Test
    public void testInsert() throws IOException {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
            sqlSession = sqlSessionFactory.openSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.insertUser(new User(null, "刘敏敏", 24));

            //手动提交事务
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDelete() throws IOException {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
            sqlSession = sqlSessionFactory.openSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.deleteUserById(3);

            //手动提交事务
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate() throws IOException {
        SqlSession sqlSession = null;
        try {
            SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();
            sqlSession = sqlSessionFactory.openSession();
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            mapper.updateUser(new User(4,"msw",null));

            //手动提交事务
            sqlSession.commit();
        } finally {
            sqlSession.close();
        }
    }


}
