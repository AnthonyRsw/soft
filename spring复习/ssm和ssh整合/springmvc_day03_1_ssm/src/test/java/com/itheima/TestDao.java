package com.itheima;

import com.itheima.dao.AccountDao;
import com.itheima.domain.Account;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
}
