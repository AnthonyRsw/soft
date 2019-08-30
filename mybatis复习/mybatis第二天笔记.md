## Mybatis第二天

#### 一、回顾



#### 二、教学目标

1、mybatis实现CRUD

2、两种Dao开发

3、核心配置文件详解

4、输入参数和输出参数

#### 三、mybatis实现CRUD

```
a、mybatis 的中文文档网址：http://www.mybatis.org/mybatis-3/zh/getting-started.html
b、 selectList 查询多个对象,返回一个list集合(也能查询一个)
    selectOne: 查询单个对象，返回一个对象
c、日志记录日常操作
	引入依赖: 
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
  引入日志的配置文件：log4j.properties
 d、增删改查
 	<!--UserMapper删除操作-->
    <delete id="del" parameterType="java.lang.Integer">
        delete from user where id = #{id}
    </delete>
 	<!--测试类-->   
    @Test
    public void testDel(){
        //获取输入流对象
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("SqlMapConfig.xml");
        //获取SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //获取SqlSession对象
        //获取的sqlsession不能自动提交
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //执行sql语句
        sqlSession.delete("userMapper.del",3);
        //提交:只要修改了数据库必须提交
        sqlSession.commit();

        sqlSession.close();
    }
 e、模糊查询
 	参数： %a% 
 		配置文件： username like #{username}
 	参数: a
 		配置文件1： username like "%"#{username}"%"
 		配置文件2： username like "%${value}%"
 							如果传的是简单类型必须使用value
 							如果是pojo,属性名引用
 d、参数
 		简单类型： 基本数据类型,String
 				如果${} 必须用value引用
 				如果#{} 随便写
 		pojo类型：属性名引用	
 e、${} 与 #{}区别
 	${}:直接拼接,不会转换类型, 不能防注入
 	#{}:转换类型后拼接, 相当于占位符?,可以防注入

```

#### 四、两种Dao开发

##### 1、传统dao开发

```

```

##### 2、动态代理模式开发

```

```

#### 五、核心配置文件详解

```
1. 配置文件中的标签和顺序
properties?, 						配置属性(学习)
settings?, 							全局配置：缓存，延迟加载
typeAliases?, 					类型别名(学习)
typeHandlers?, 					类型转换(操作)(了解)
objectFactory?, 	 objectWrapperFactory?,  reflectorFactory?, 
plugins?, 							插件:分页插件
environments?, 					环境配置（数据源）
databaseIdProvider?, 		
mappers?								引入映射配置文件(学习)

? : 一个或者零个
| : 任选其一
+ : 最少一个
* : 零个或多个
, : 必须按照此顺序编写

```

#### 六、输入参数类型和输出参数类型

```
1. 输入参数
	简单类型: 基本数据类型+ String类型
				#{} :名称随便
				${} :${value}
	pojo 类型
		#{} :${} : 属性名引用
	包装对象类型
		引用  #{属性.属性名}
	Map集合
		引用： #{key}
	多个参数
		引用时：#{param1},#{param2}.....#{paramN}
2. 返回值类型
	如果列与属性名不一致，对应的属性为null, 必须写映射配置
```

#### 七、总结

```

```

