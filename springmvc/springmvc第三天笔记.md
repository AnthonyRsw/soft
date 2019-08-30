# springMVC的第三天

### 一、教学目标

1. 整合ssm框架
2. 拦截器

### 二、整合SSM

##### 1、整合思路

```java
a. SSM介绍
	springmvc+ spring + mybatis=ssm
	mybatis 持久层的CURD
	spring 业务层  IOC、DI(解耦) 和AOP(事务问题), ssm 综合练习中：aop解决日志问题
	springMVC 表现层 MVC的操作
b. 整合使用的技术
	1). Spring		5.0.2
	2). mybatis		3.4.5	
	3). SpringMVC	5.0.2
	4). log4J2		2.9.1
	5). bootstrap	3.3.5
	6). jquery		1.9.1
	......
c. 业务介绍
	我们需要完成一张账户表的增删改查操作
```

##### 2、引入依赖和依赖分析

```java
a. Spring相关的
    1). spring-context : Spring容器
    2). spring-tx : Spring事务
    3). spring-jdbc : SpringJDBC
    4). spring-test : Spring单元测试
    5). spring-webmvc : SpringMVC
b. mybatis相关的
    1). mybatis : mybatis核心
    2). mybatis-spring ：mybatis与spring整合
    3) 切面相关的 
    	aspectjweaver : AOP切面
    4) 数据源相关（选择使用）:
    	c3p0
    	commons-dbcp
    	spring自带的数据源
    5) 单元测试相关的:
    	junit : 单元测试,与spring-test放在一起做单元测试
    6) ServletAPI相关的
    	jsp-api : jsp页面使用request等对象
    	servlet-api : java文件使用request等对象
    7) 日志相关的: 
        log4j-core : log4j2核心包
        log4j-api : log4j2的功能包
        log4j-web : web项目相关日志功能
        slf4j-api : 另外一种日志包,	
			  slf4j:Simple Logging Facade for Java为java做简单的日志记录此处和log4j一起
        log4j-slf4j-impl : slf4j的log4j实现类,也就是说slf4j的日志记录功能由log4j实现
        log4j-jcl : 程序运行的时候检测用了哪种日志实现类现在叫Apache Common Logging
    8) 数据库相关的
        mysql-connector-java : mysql的数据库驱动包
        ojdbc.jar : oracle的驱动jar包
    9) 页面表达式
        JSTL : JSTL标签库必须jar包 基础功能
        standard : JSTL标签库的必须jar包 进阶功能
    10) 文件上传
        commons-fileupload : 上传插件
        commons-io : IO操作包
```

##### 3、表和实体类的创建

```java
1. 表

2. 实体类
public class Account {
    private Integer id;
    private String name;
    private Float money;
}
```

##### 4、Dao层的编写

```java
a、引入依赖
<!--mybatis -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.4.5</version>
    </dependency>
    <!--mysql驱动-->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.36</version>
    </dependency>
    <!--数据源-->
    <dependency>
      <groupId>c3p0</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.1.2</version>
    </dependency>
    <dependency>
      <groupId>commons-dbcp</groupId>
      <artifactId>commons-dbcp</artifactId>
      <version>1.4</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
    <!--单元测试-->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
    </dependency>
b、接口编写
package com.itheima.dao;

import com.itheima.domain.Account;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface AccountDao {
    /**
     * 查询所有
     * @return
     */
    @Select("select * from account")
    public List<Account> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from account where id = #{id}")
    public Account findById(Integer id);

    /**
     * 保存账户
     * @param account
     */
    @Insert("insert into account values(null ,#{name},#{money})")
    public void save(Account account);

    /**
     * 更新账户
     * @param account
     */
    @Update("update account set name = #{name},money=#{money} where id = #{id}")
    public void update(Account account);

    /**
     * 删除一个账户
     * @param id
     */
    @Delete("delete from account where id = #{id}")
    public void del(Integer id);
}


c、配置文件
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--引入数据的属性文件-->
    <properties resource="jdbc.properties"></properties>

    <!--数据库的环境-->
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="${jdbc.driver}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="username" value="${jdbc.user}"/>
                <property name="password" value="${jdbc.password}"/>
            </dataSource>
        </environment>
    </environments>
    <!--映射配置文件:指定持久层接口的包路径-->
    <mappers>
        <package name="com.itheima.dao"></package>
    </mappers>
</configuration>
d、测试
package com.itheima;

import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class TestDao {

    @Test
    public void test(){
//        配置文件的输入流对象
        InputStream inputStream =  this.getClass().getClassLoader().getResourceAsStream("SqlMapConfig.xml");
//        session工厂对象
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        获取一个SqlSession对象
        SqlSession sqlSession = sessionFactory.openSession();
//        获取动态代理对象
        AccountDao accountDao = sqlSession.getMapper(AccountDao.class);
//        执行方法
        List<Account> accountList = accountDao.findAll();
//        遍历结果
        for (Account account : accountList) {
            System.out.println(account.getName());
        }
//        释放资源
        sqlSession.close();
    }
}
e. spring 与mybatis整合操作
1）添加依赖
<!--整合mybatis与spring需要的jar包-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.1</version>
    </dependency>
    <!--spring的核心-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
2) 删除了SqlMapConfig.xml
3) 添加一个applicationContext.xml文件
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!--引入数据库外部属性文件-->
    <context:property-placeholder location="classpath:jdbc.properties"></context:property-placeholder>

    <!--创建数据源对象-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <!--创建sqlSessionFactory对象-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--注入数据源对象-->
        <property name="dataSource" ref="dataSource"></property>
        <!--配置方法一：引入SqlMapConfig.xml文件-->
        <!--<property name="configLocation" value="classpath:SqlMapConfig.xml"></property>-->
        <!--配置方法二-->
        <!--别名映射-->
        <!--<property name="typeAliasesPackage" value="com.itheima.domain"></property>-->
        <!--可以注入其他的属性-->
        <!--<property name="configurationProperties" value=""></property>-->
    </bean>

    <!--扫描dao层接口的包， 创建动态代理对象, 存入到spring容器中-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--需要指定dao层接口的包名-->
        <property name="basePackage" value="com.itheima.dao"></property>
    </bean>
</beans>
4） 测试
 @Test
    public void testMybatisWithSpring(){
        //创建spring容器
        ApplicationContext ac  = new ClassPathXmlApplicationContext("applicationContext.xml");
        //获取容器中的对象
        AccountDao accountDao = ac.getBean(AccountDao.class);
        //执行方法
        List<Account> accountList = accountDao.findAll();
        //遍历结果
        for (Account account : accountList) {
            System.out.println(account.getName());
        }
    }
```

##### 5、Service层编写

```java
a、引入依赖
 <!--spring的核心-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
    <!--事务相关-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
    <!--aop的切面相 关-->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.8.9</version>
    </dependency>
    <!--spring 的测试包-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
b、接口编写
package com.itheima.service;

import com.itheima.domain.Account;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public interface AccountService {
    /**
     * 查询所有
     * @return
     */
    public List<Account> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Account findById(Integer id);

    /**
     * 保存账户
     * @param account
     */
    public void save(Account account);

    /**
     * 更新账户
     * @param account
     */
    public void update(Account account);

    /**
     * 删除一个账户
     * @param id
     */
    public void del(Integer id);
}

c、实现类编写
package com.itheima.service.impl;

import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountDao accountDao;

    @Override
    public List<Account> findAll() {
        return accountDao.findAll();
    }

    @Override
    public Account findById(Integer id) {
        return accountDao.findById(id);
    }

    @Override
    public void save(Account account) {
        accountDao.save(account);
    }

    @Override
    public void update(Account account) {
        accountDao.update(account);
    }

    @Override
    public void del(Integer id) {
        accountDao.del(id);
    }
}

d、配置文件,在applicationContext.xml中添加
<!--业务层配置开始-->
    <!--扫描包，创建业务层所有类对象-->
    <context:component-scan base-package="com.itheima.service"></context:component-scan>
    <!--声明式事务-->
    <!--1. 事务管理类对象-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--注入：数据源-->
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--2. 事务增强对象-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--该类方法只读的事务， 如果有事务，加入事务执行，如果没有事务，非事务执行-->
            <tx:method name="find*" read-only="true" propagation="SUPPORTS"/>
            <tx:method name="query*" read-only="true" propagation="SUPPORTS"/>
            <tx:method name="get*" read-only="true" propagation="SUPPORTS"/>
            
            <!--其他方法：非只读事务，如果没有事务，创建一个事务，如果有事务，加入事务执行-->
            <tx:method name="*" read-only="false" propagation="REQUIRED"></tx:method>
        </tx:attributes>
    </tx:advice>
    <!--3.aop配置：切面配置-->
    <aop:config>
        <!--切面配置-->
        <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.itheima.service.impl.*.*(..))"></aop:advisor>
    </aop:config>
    <!--业务层配置结束-->
e、测试
package com.itheima;

import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class TestService {

    @Autowired
    AccountService accountService;

    @Test
    public void test(){
        List<Account> accountList = accountService.findAll();
        for (Account account : accountList) {
            System.out.println(account.getName());
        }
    }
}
```

##### 6、dao和service最终配置文件

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:aop="http://www.springframework.org/schema/aop"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd
       http://www.springframework.org/schema/aop
       http://www.springframework.org/schema/aop/spring-aop.xsd">

    <!--持久层配置 开始-->
    <!--引入数据库外部属性文件-->
    <context:property-placeholder location="classpath:jdbc.properties"></context:property-placeholder>

    <!--创建数据源对象-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="${jdbc.driver}"></property>
        <property name="url" value="${jdbc.url}"></property>
        <property name="username" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>
    <!--创建sqlSessionFactory对象-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--注入数据源对象-->
        <property name="dataSource" ref="dataSource"></property>
        <!--配置方法一：引入SqlMapConfig.xml文件-->
        <!--<property name="configLocation" value="classpath:SqlMapConfig.xml"></property>-->
        <!--配置方法二-->
        <!--别名映射-->
        <!--<property name="typeAliasesPackage" value="com.itheima.domain"></property>-->
        <!--可以注入其他的属性-->
        <!--<property name="configurationProperties" value=""></property>-->
    </bean>

    <!--扫描dao层接口的包， 创建动态代理对象, 存入到spring容器中-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--需要指定dao层接口的包名-->
        <property name="basePackage" value="com.itheima.dao"></property>
    </bean>
    <!--持久层配置结束-->

    <!--业务层配置开始-->
    <!--扫描包，创建业务层所有类对象-->
    <context:component-scan base-package="com.itheima.service"></context:component-scan>
    <!--声明式事务-->
    <!--1. 事务管理类对象-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--注入：数据源-->
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--2. 事务增强对象-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--该类方法只读的事务， 如果有事务，加入事务执行，如果没有事务，非事务执行-->
            <tx:method name="find*" read-only="true" propagation="SUPPORTS"/>
            <tx:method name="query*" read-only="true" propagation="SUPPORTS"/>
            <tx:method name="get*" read-only="true" propagation="SUPPORTS"/>
            
            <!--其他方法：非只读事务，如果没有事务，创建一个事务，如果有事务，加入事务执行-->
            <tx:method name="*" read-only="false" propagation="REQUIRED"></tx:method>
        </tx:attributes>
    </tx:advice>
    <!--3.aop配置：切面配置-->
    <aop:config>
        <!--切面配置-->
        <aop:advisor advice-ref="txAdvice" pointcut="execution(* com.itheima.service.impl.*.*(..))"></aop:advisor>
    </aop:config>
    <!--业务层配置结束-->
</beans>
```

##### 7、web层编写

```java
a、引入依赖
 <!--springmvc-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
    <!--servlet相关的-->
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
      <scope>provided</scope>
    </dependency>
b、配置文件:springmvc.xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--扫描包，创建类对象-->
    <context:component-scan base-package="com.itheima.controller"></context:component-scan>
    <!--视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
    <!--注解驱动-->
    <mvc:annotation-driven></mvc:annotation-driven>
    <!--自定义类型转换器-->

    <!--文件上传-->

    <!--拦截器-->

    <!--静态资源放行-->
    <!--<mvc:resources mapping="/js/*" location="/js/"></mvc:resources>-->
    <!--静态资源全部放行-->
    <mvc:default-servlet-handler></mvc:default-servlet-handler>
</beans>
c. 控制类的创建
package com.itheima.controller;

import com.itheima.domain.Account;
import com.itheima.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccountService accountService;

    /**
     * 查询全部
     * @return
     */
    @RequestMapping("/findAll")
    public ModelAndView findAll(){
        //查询数据
        List<Account> accountList = accountService.findAll();
        System.out.println(accountList);
        ModelAndView modelAndView = new ModelAndView();
        //添加数据
        modelAndView.addObject("accountList",accountList);
        //指定页面
        modelAndView.setViewName("show");
        return modelAndView;
    }
    /**
     * 保存操作
     *  执行service中的save方法
     *  最后查询数据库中所有的的内容，在页面展示
     *
     */
    @RequestMapping("/save")
    public String save(Account account){
        //执行保存操作
        accountService.save(account);
        //执行查询所有
        return "redirect:findAll";
    }

    @RequestMapping("/del")
    public String del(Integer id){

        //执行删除操作
        accountService.del(id);
        //执行查询所有
        return "redirect:findAll";
    }

    /**
     * 更新页面数据回显
     * @return
     */
    @RequestMapping("/updateUI")
    public ModelAndView updateUI(Integer id){
        //根据id查询一个账户
        Account account = accountService.findById(id);
        //创建模型视图对象
        ModelAndView modelAndView = new ModelAndView();
        //添加数据
        modelAndView.addObject("account", account);
        //指定页面
        modelAndView.setViewName("update");
        return modelAndView;
    }

    /**
     * 更新账户
     * @param account
     * @return
     */
    @RequestMapping("/update")
    public String update(Account account){
        //更新操作
        accountService.update(account);
        //执行查询所有
        return "redirect:findAll";
    }
}


```

##### 8、编写web.xml

```xml
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>
  <display-name>Archetype Created Web Application</display-name>
  <!--配置一个全局的参数：指定spring容器的配置文件-->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
  </context-param>
  <!--编码过滤器-->
  <filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  
  <!--配置监听器：创建spring容器对象-->
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <!--前端控制器-->
  <servlet>
    <servlet-name>DispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!--指定配置文件的路径-->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>DispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>

```

##### 9、编写页面

```java
bootstrap 文档网站：https://www.w3cschool.cn/bootstrap/
a,   查询页面编写
1. show.jsp
<%--
  Created by IntelliJ IDEA.
  User: sun
  Date: 2018/11/22
  Time: 16:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<%--引入c 标签--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入CSS样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<table class="table table-bordered">
    <caption>边框表格布局</caption>
    <thead>
    <tr>
        <th>编号</th>
        <th>账户名</th>
        <th>余额</th>
    </tr>
    </thead>
    <tbody>
    <%--foreach循环
        items: 要循环的集合对象
        var：循环中的每一个对象
    --%>
    <c:forEach items="${accountList}" var="account">
    <tr>
        <td>${account.id}</td>
        <td>${account.name}</td>
        <td>${account.money}</td>
    </tr>
    </c:forEach>
    </tbody></table>
</body>
<!-- 引入JS文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</html>
2. 引入bootstrap资源
3.  jstl标签的依赖
<!-- JSTL标签库 -->
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>taglibs</groupId>
      <artifactId>standard</artifactId>
      <version>1.1.2</version>
    </dependency>
b ,添加账户
<%--
  Created by IntelliJ IDEA.
  User: sun
  Date: 2018/11/22
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入CSS样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<form class="form-horizontal" role="form" method="post" action="${pageContext.request.contextPath}/account/save">
    <div class="form-group">
        <label for="username" class="col-sm-2 control-label">账户名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="username" name="name"
                   placeholder="请输入账户名">
        </div>
    </div>
    <div class="form-group">
        <label for="money" class="col-sm-2 control-label">余额</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="money" name="money"
                   placeholder="请输入余额">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">保存</button>
        </div>
    </div></form>
</body>
<!-- 引入JS文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</html>
c, 删除操作
<a href="${pageContext.request.contextPath}/account/del?id=${account.id}" class="btn btn-success">删除</a>
d, 更新操作
<%--
  Created by IntelliJ IDEA.
  User: sun
  Date: 2018/11/22
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入CSS样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<form class="form-horizontal" role="form" method="post" action="${pageContext.request.contextPath}/account/update">
    <%--使用隐藏域，保存id的值，更新时作为条件--%>
    <input type="hidden" name="id" value="${account.id}">
    <div class="form-group">
        <label for="username" class="col-sm-2 control-label">账户名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="username" name="name" value="${account.name}"
                   placeholder="请输入账户名">
        </div>
    </div>
    <div class="form-group">
        <label for="money" class="col-sm-2 control-label">余额</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="money" name="money" value="${account.money}"
                   placeholder="请输入余额">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">更新</button>
        </div>
    </div></form>
</body>
<!-- 引入JS文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</html>

```

### 三、拦截器

##### 1、拦截器的作用

```
a. 拦截器类似于Servlet开发中的过滤器Filter，用于对处理器进行预处理和后处理。
b. 拦截器链（Interceptor Chain）。拦截器链就是将拦截器按一定的顺序联结成一条链。在访问被拦截的方法或字段时，拦截器链中的拦截器就会按其之前定义的顺序被调用。
c. 拦截器，过滤器，监听器的区别
	过滤器：是servlet的一部分，任何web项目都可以使用
		 配置  /* 后会过滤所有的资源（请求）
	拦截器：是springMVC的一部分，只能在springMVC中使用
		 配置了/* 只会拦截请求，不会拦截静态资源
    监听器：Web监听器是Servlet规范中的一种特殊类，用于监听ServletContext、HttpSession和				  	ServletRequest等域对象的创建与销毁事件，当Web应用启动时启动，当Web应用销毁时销毁。用于监听域对象的属性发生修改的事件，可以在事件发生前、发生后做一些必要的处理
d. 底层采用的是aop的思想
```

##### 2、拦截器的代码

```java
a. 引入依赖
 <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.0.2.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>servlet-api</artifactId>
      <version>2.5</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>jsp-api</artifactId>
      <version>2.0</version>
      <scope>provided</scope>
    </dependency>
b.spring-mvc.xml
  <?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--扫描包，创建类对象-->
    <context:component-scan base-package="com.itheima.controller"></context:component-scan>
    <!--视图解析器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/pages/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
    <!--注解驱动-->
    <mvc:annotation-driven></mvc:annotation-driven>
    <!--自定义类型转换器-->

    <!--文件上传-->

    <!--引入拦截器:配置拦截器链-->
    <mvc:interceptors>
        <!--配置单个拦截器对象-->
        <mvc:interceptor>
            <!--拦截所有的请求-->
            <mvc:mapping path="/**"/>
            <!--指定拦截器类-->
            <bean class="com.itheima.interceptor.MyInterceptor2"></bean>
        </mvc:interceptor>
        <!--配置单个拦截器对象-->
        <mvc:interceptor>
            <!--拦截所有的请求-->
            <mvc:mapping path="/**"/>
            <!--指定拦截器类-->
            <bean class="com.itheima.interceptor.MyInterceptor1"></bean>
        </mvc:interceptor>
    </mvc:interceptors>

    <!--静态资源放行-->
    <!--<mvc:resources mapping="/js/*" location="/js/"></mvc:resources>-->
    <!--静态资源全部放行-->
    <mvc:default-servlet-handler></mvc:default-servlet-handler>
</beans>
c. 创建拦截器类
package com.itheima.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 创建自己的拦截器类，需要实现接口HandlerInterceptor
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class MyInterceptor1 implements HandlerInterceptor{

    /**
     * 执行顺序：在控制器方法执行前执行
     * 作用：拦截所有的请求，判断是否可以进行下一步执行
     *  举例：如果判断你是否登录成功了,如果登录成功，则放行，返回值配置为true
     *          如果登录失败，则拦截，返回值配置为false
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器1：preHandle执行了");
        return true;
    }

    /**
     * 执行的顺序： preHandel放行操作，可以执行
     *          控制器方法返回值之前执行
     *    作用：可以对返回的数据验证
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("拦截器1：postHandle执行了");
    }

    /**
     * 执行顺序：preHandler必须放行, 执行完postHandle，之后执行
     *  作用：释放资源
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("拦截器1：afterCompletion执行了");
    }
}

```

##### 3、多个拦截器测试

```java
a. preHandler: 按照拦截器的配置顺序正序执行, 如果返回的都是true，则执行控制器的方法
b. postHandler: 所有的preHandler执行完之后，返回的都是true， 按照拦截器配置顺序倒序执行
c. afterCompletion: 所有postHandler执行完之后，
d. 配置多个拦截器
<!--引入拦截器:配置拦截器链-->
    <mvc:interceptors>
        <!--配置单个拦截器对象-->
        <mvc:interceptor>
            <!--拦截所有的请求-->
            <mvc:mapping path="/**"/>
            <!--指定拦截器类-->
            <bean class="com.itheima.interceptor.MyInterceptor2"></bean>
        </mvc:interceptor>
        <!--配置单个拦截器对象-->
        <mvc:interceptor>
            <!--拦截所有的请求-->
            <mvc:mapping path="/**"/>
            <!--指定拦截器类-->
            <bean class="com.itheima.interceptor.MyInterceptor1"></bean>
        </mvc:interceptor>
    </mvc:interceptors>
```

##### 4、在ssm中使用拦截器示例

```java
a, 登录页面
<%--
  Created by IntelliJ IDEA.
  User: sun
  Date: 2018/11/22
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- 引入CSS样式 -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
</head>
<body>
<form class="form-horizontal" role="form" method="post" action="${pageContext.request.contextPath}/login">
    <div class="form-group">
        <label for="username" class="col-sm-2 control-label">用户名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="username" name="username"
                   placeholder="请输入用户名">
        </div>
    </div>
    <div class="form-group">
        <label for="money" class="col-sm-2 control-label">密码</label>
        <div class="col-sm-10">
            <input type="password" class="form-control" id="money" name="password"
                   placeholder="请输入密码">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-default">登录</button>
        </div>
    </div></form>
</body>
<!-- 引入JS文件 -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

</html>
b, spring-mvc.xml 添加拦截器配置
 <!--拦截器-->
    <mvc:interceptors>
        <mvc:interceptor>
            <mvc:mapping path="/**"/>
            <!--如果拦截了静态资源，需要配置放行-->
            <mvc:exclude-mapping path="/js/*"></mvc:exclude-mapping>
            <mvc:exclude-mapping path="/css/*"></mvc:exclude-mapping>
            <mvc:exclude-mapping path="/fonts/*"></mvc:exclude-mapping>
            <bean class="com.itheima.interceptor.LoginInterceptor"></bean>
        </mvc:interceptor>
    </mvc:interceptors>
c, 创建拦截器类
package com.itheima.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
public class LoginInterceptor implements HandlerInterceptor{

    /**
     * 登录验证
     *      如果session有登录信息，放行
     *      如果session没有登录信息，拦截
     * 判断是否是登录请求，如果登录请求，直接放行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求路径
        String requestURI = request.getRequestURI();
        //判断是否是登录请求
        if(requestURI.contains("login")){
//            如果是登录请求，直接放行
            return true;
        }
        //从session中获取登录信息
        Object username = request.getSession().getAttribute("username");
        if(username != null){
            //session中有登录信息，放行
            return true;
        }else{
            //session没有登录信息，跳转到登录页面
            response.sendRedirect("/login.jsp");
            return false;
        }
    }
}

```

