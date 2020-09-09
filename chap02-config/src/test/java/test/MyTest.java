package test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import xinyet.mapper.UserMapper;
import xinyet.pojo.User;

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
            List<User> users = session.selectList("xinyet.mapper.UserMapper.getAllUser");
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
}
