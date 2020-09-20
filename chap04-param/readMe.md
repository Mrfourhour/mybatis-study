# mybatis:参数处理
## 一.单个参数
### 若查询方法的参数只有一个并且没有注解的话,mybatis不会做特殊的处理,取值方式直接通过#{参数名/任意名}
#### 1.测试单个参数没有注解的情况
//这是mapper接口中定义的方法
```java
public interface EmployeeMapper {
    Employee getEmployeeById(Integer id);
}
```
    
//这是mapper.xml中的sql
```xml
<select id="getEmployeeById" resultType="com.xinyet.param.entity.Employee">
        select * from employee where id = #{id}
</select>
```
    
//这是输出结果
        
    Employee{id=1, name='张三', gender=1, age=14, email='zhangsan@qq.com', departmentId=null}
-----------------------------------------------------------
    //将mapper.xml中的参数id换成其他参数再测试,name
    <select id="getEmployeeById" resultType="com.xinyet.param.entity.Employee">
        select * from employee where id = #{name}
    </select>
    //输出结果
    Employee{id=1, name='张三', gender=1, age=14, email='zhangsan@qq.com', departmentId=null}
#### 2. 单个参数有注解的情况的话, mybatis会根据注解的名字封装到map中 

---------------------------------------------------------------------------------------------
## 二.多个参数
### 1. mybatis会做特殊处理。多个参数会被封装成 一个map，key：param1...paramN,或者参数的索引也可以;value：传入的参数值;#{}就是从map中获取指定的key的值；
这是mapper接口中定义的方法
```java
public interface EmployeeMapper {
    Employee getEmployeeByIdAndName(Integer id, String name);
}
```
mapper.xml中的sql
```xml
    <select id="getEmployeeByIdAndName" resultType="com.xinyet.param.entity.Employee">
        select * from employee where id = #{id} and name = #{name}
    </select>
```
exception
       
    Caused by: org.apache.ibatis.binding.BindingException: Parameter 'id' not found. Available parameters are [arg1, arg0, param1, param2]

根据提示将参数改成 arg0 arg1/ param1 param2 皆可运行成功
```xml
    <select id="getEmployeeByIdAndName" resultType="com.xinyet.param.entity.Employee">
        select * from employee where id = #{arg0} and name = #{arg1}
    </select>
```
```xml
    <select id="getEmployeeByIdAndName" resultType="com.xinyet.param.entity.Employee">
        select * from employee where id = #{param1} and name = #{param2}
    </select>
```

推荐方式: 通过命名参数的方式,将mapper接口的参数均加上@Param注解

备注:  多个参数,想要直接根据参数名取值,jdk必须处于1.8版本，且编译时带上了-parameters 参数，那么获取的就是实际的参数名，getEmployeeByIdAndName(Integer id, String name)
        
        
            <select id="getEmployeeByIdAndName" resultType="com.xinyet.param.entity.Employee">
                select * from employee where id = #{id} and name = #{name}
            </select>
            
            
            <settings>
                <!--是否开启使用参数名作为mapper取值的名称;3.4.1 过后默认为true-->
                <setting name="useActualParamName" value="false"/>
            </settings>
            
            关于编译时带上了-parameters 参数,maven的配置方式为
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.1</version>
                    <configuration>
                        <compilerArgument>-parameters</compilerArgument>
                        <source>1.8</source>
                        <target>1.8</target>
                    </configuration>
                </plugin>
            </plugins>

### 三.参数为对象/map
mapper接口中的方法
```java
public interface EmployeeMapper {
    Employee getEmployeeByTo(IdAndNameTo to); //IdAndNameTo对象中包含 id , name两个字段
}
```
mapper.xml中的sql
```xml
    <!--取值直接用对象中的属性-->
    <select id="getEmployeeByTo" resultType="com.xinyet.param.entity.Employee">
        select * from employee where id = #{id} and name = #{name}
    </select>
```
测试结果:成功获取结果集

    Employee{id=1, name='张三', gender=1, age=14, email='zhangsan@qq.com', departmentId=null}
    
### 四.思考
    ========================思考================================
    public Employee getEmp(@Param("id")Integer id,String lastName);
    取值：id==>#{id/param1} lastName==>#{param2}
    
    public Employee getEmp(Integer id,@Param("e")Employee emp);
    取值：id==>#{param1} lastName===>#{param2.lastName/e.lastName}
    
    ##特别注意：如果是Collection（List、Set）类型或者是数组，
    也会特殊处理。也是把传入的list或者数组封装在map中。
    key：Collection（collection）,如果是List还可以使用这个key(list)
    数组(array)
    public Employee getEmpById(List<Integer> ids);
    取值：取出第一个id的值： #{list[0]}
### 五.mybatis map参数的封装(源码这块不是很熟悉,后面点回来研究)

```java
public class ParamNameResolver {
    public ParamNameResolver(Configuration config, Method method) {
        // 获取参数列表中每个参数的类型
        Class<?>[] paramTypes = method.getParameterTypes();
        // 获取参数列表上的注解  @Param
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        // 该集合用于记录参数索引与参数名称的对应关系
        SortedMap<Integer, String> map = new TreeMap();
        int paramCount = paramAnnotations.length;
        // 遍历所有参数
        for(int paramIndex = 0; paramIndex < paramCount; ++paramIndex) {
            // 只判断参数不是RowBounds类型或者不是ResultHandler类型
            if (!isSpecialParameter(paramTypes[paramIndex])) {
                String name = null;
                Annotation[] var9 = paramAnnotations[paramIndex];
                int var10 = var9.length;
                //遍历该参数上的注解集合
                for(int var11 = 0; var11 < var10; ++var11) {
                    Annotation annotation = var9[var11];
                    if (annotation instanceof Param) {
                        this.hasParamAnnotation = true;
                        // 获取@Param注解指定的参数名称
                        name = ((Param)annotation).value();
                        break;
                    }
                }
                // 没有@Param注解的话 执行下面逻辑
                if (name == null) {
                    // useActualParamName==true时  即name = arg0 ...
                    if (config.isUseActualParamName()) {
                        name = this.getActualParamName(method, paramIndex);
                    }
                    // 使用参数的索引作为其名称
                    if (name == null) {
                        name = String.valueOf(map.size());
                    }
                }
                map.put(paramIndex, name);
            }
        }

        this.names = Collections.unmodifiableSortedMap(map);
    }
}

//总结:1.将参数放到map中    Key:参数位置(0开始)  value:参数值
```