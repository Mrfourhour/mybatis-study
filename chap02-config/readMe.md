# mybaits 配置文件
## 1. 配置文件列表
- configuratio
  - properties
  - settings
  - typeAliases
  - typeHandlers
  - objectFactory
  - plugins
  - environments
    + environment
        + transactionManager
        + dataSource
  - databaseIdProvider
  - mappers
## 2.配置文件所有学习记录放在mybatis.xml文件中
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>

    <!-- ##################################################################### properties ###################################################################### -->
    <!--
    resource: 引入类路径下的其他properties配置文件
    url: 引入网络路径或者磁盘路径下的properties配置文件
    jdbc.driver=com.mysql.cj.jdbc.Driver
    jdbc.url=jdbc:mysql:///mybatis?serverTimezone=UTC&amp;characterEncoding=utf8&amp;useUnicode=true&amp;useSSL=false&amp;allowPublicKeyRetrieval=true
    jdbc.username=root
    jdbc.password=123456
    配置文件的信息,通过${jdbc.driver}...来获取
    重要: xml中存在5个转义字符
        <   &lt;
        >   &gt;
        &   $amp;
        "   &quot;
        空格 &nbsp;
    -->
    <properties resource="jdbc.properties"/>


    <!-- ##################################################################### settings ###################################################################### -->
    <!--
        These are extremely important tweaks that modify the way that MyBatis behaves at runtime.
        An example of the settings element fully configured is as follows:
    -->
    <settings>
        <setting name="cacheEnabled" value="true"/>
        <setting name="lazyLoadingEnabled" value="true"/>
        <setting name="multipleResultSetsEnabled" value="true"/>
        <setting name="useColumnLabel" value="true"/>
        <setting name="useGeneratedKeys" value="false"/>
        <setting name="autoMappingBehavior" value="PARTIAL"/>
        <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
        <setting name="defaultExecutorType" value="SIMPLE"/>
        <setting name="defaultStatementTimeout" value="25"/>
        <setting name="defaultFetchSize" value="100"/>
        <setting name="safeRowBoundsEnabled" value="false"/>
        <setting name="mapUnderscoreToCamelCase" value="false"/>
        <setting name="localCacheScope" value="SESSION"/>
        <setting name="jdbcTypeForNull" value="OTHER"/>
        <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
    </settings>


    <!-- ##################################################################### typeAliases ###################################################################### -->
    <!--
    1. A type alias is simply a shorter name for a Java type. It's only relevant to the XML configuration and simply exists to reduce redundant typing of fully qualified classnames. For example:
    -->
    <!--    <typeAliases>-->
    <!--        <typeAlias alias="Author" type="domain.blog.Author"/>-->
    <!--        <typeAlias alias="Blog" type="domain.blog.Blog"/>-->
    <!--        <typeAlias alias="Comment" type="domain.blog.Comment"/>-->
    <!--        <typeAlias alias="Post" type="domain.blog.Post"/>-->
    <!--        <typeAlias alias="Section" type="domain.blog.Section"/>-->
    <!--        <typeAlias alias="Tag" type="domain.blog.Tag"/>-->
    <!--    </typeAliases>-->

    <!--
    2. With this configuration, Blog can now be used anywhere that domain.blog.Blog could be.
    You can also specify a package where MyBatis will search for beans. For example:
    -->
    <typeAliases>
        <package name="domain.blog"/>
    </typeAliases>


    <!--
    3. Each bean found in domain.blog , if no annotation is found, will be registered as an alias
    using uncapitalized non-qualified class name of the bean.
    That is domain.blog.Author will be registered as author.
    If the @Alias annotation is found its value will be used as an alias. See the example below:

    @Alias("author") public class Author {    ... }
    -->

    <!--
       4. There are many built-in type aliases for common Java types. They are all case insensitive,
        note the special handling of primitives due to the overloaded names.
        _byte byte
        _long long
        _short short
        _int int
        _integer int
        _double double
        _float float
        _boolean boolean
    -->

    <!-- ##################################################################### typeHandlers ###################################################################### -->
    <!-- 此章节后面会详细研究
    Whenever MyBatis sets a parameter on a PreparedStatement or retrieves a value from a ResultSet,
    a TypeHandler is used to retrieve the value in a means appropriate to the Java type.
    -->

    <!-- ##################################################################### environments ###################################################################### -->
    <!--
    environments:可以配置多中环境
        environment: 必须包含transactionManager,dataSource这两个标签
            transactionManager: 事务管理器 JDBC|MANAGED   可自定义,自定义的话实现 TransactionFactory 接口
                this.typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
                this.typeAliasRegistry.registerAlias("MANAGED", ManagedTransactionFactory.class);
        dataSource: 数据库访问配置,三种类型
            this.typeAliasRegistry.registerAlias("JNDI", JndiDataSourceFactory.class);
            this.typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
            this.typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
            自定义数据源可以实现 DataSourceFactory 接口
    -->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>


    <!-- ##################################################################### databaseIdProvider ###################################################################### -->
    <!--
    1.为不同的数据库厂商取别名;
    2.使用方式: 在mapper.xml中编写sql的时候加上databaseId属性,并制定对应的别名 ;
    3.可以结合多数据源 environments标签同时使用
    -->
    <databaseIdProvider type="DB_VENDOR">
        <property name="MySQL" value="mysql"/>
        <property name="Oracle" value="oracle"/>
    </databaseIdProvider>

    <!-- ##################################################################### mappers ###################################################################### -->

    <!--
    1.将mapper文件注入到主配置文件中,
        resource: 引入类路径下的资源
        url: 引入网络或磁盘路径下的资源
        class: 注册接口
                    ①直接引入mapper接口,,,,注意,mapper文件必须与接口同名,并且放在同一目录下
                    ②省略mapper配置文件,直接在mapper的方法上写sql
    2.批量注册
        <package name="包名"/>
    -->
    <mappers>
        <mapper resource="xinyet/mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```