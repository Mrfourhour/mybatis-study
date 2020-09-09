# mybatis quick start
## 1.建立mybatis主配置文件 mybatis.xml
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql:///mybatis?serverTimezone=UTC&amp;characterEncoding=utf8&amp;useUnicode=true&amp;useSSL=false&amp;allowPublicKeyRetrieval=true"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="com/xinyet/mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```
## 2.建立mapper映射文件
```xml
<?xml version="1.0" encoding="UTF-8" ?> <!DOCTYPE mapper  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.xinyet.mapper.UserMapper">

    <select id="getAllUser" resultType="com.xinyet.pojo.User">
    select * from user
    </select>

</mapper>
```
## 3.建立mapper接口
```java
package com.xinyet.mapper;

import com.xinyet.pojo.User;

import java.util.List;

public interface UserMapper {
    /**
     * 获取所有的用户
     *
     * @return
     */
    List<User> getAllUser();
}

```
## 4.创建测试类
```java
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

            //不推荐使用(namespace + mapper.xml文件中的sql_id)
            List<User> users = session.selectList("com.xinyet.mapper.UserMapper.getAllUser");
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

```

## 5. 最后附上maven依赖
```xml
 <dependencies>
        <!--用于测试 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13</version>
            <scope>test</scope>
        </dependency>

        <!--mybatis 依賴-->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.0</version>
        </dependency>

        <!--数据库驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.20</version>
        </dependency>

    </dependencies>
```
## 6.当mybatis的主配置文件不在类路径下的时候,在pom文件中加上
```xml
<build>
    <resources>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.xml</include>
            </includes>
        </resource>
    </resources>
</build>
```
## 7. 当使用MySQL8.0以上版本的时候,若MySQL服务器重启过,再次连接MySQL的时候会出现Public Key Retrieval is not allowed此时需要再URL后面加上allowPublicKeyRetrieval=true