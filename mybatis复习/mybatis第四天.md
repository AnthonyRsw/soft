### mybatis第四天

#### 一、教学目标

1、mybatis延迟加载

2、mybatis缓存

3、mybatis注解开发

#### 二、mybatis延迟加载

##### a、什么是延迟加载

```
1. 也叫懒加载
2. 什么时候需要，什么时候去获取
	什么时候需要该数据，什么时候执行sql语句去查询
```

##### b、一对一延迟加载

```
1. pojo
	public class Account {
    private Integer id;
    private String name;
    private Float money;
    private Integer u_id;
    //一个账户对应一个用户对象
    private User user;
  }
2. accountDao.java
	public interface AccountDao {

    /**
     * 查询所有的账户：包含用户信息
     * @return
     */
    public List<Account> findAll();
	}	
3. userDao.java
public interface UserDao {

    /**
     * 根据id查询用户对象
     * @param id
     * @return
     */
    public User findById(Integer id);
}
4. AccountDao.xml
<mapper namespace="com.itheima.dao.AccountDao">
    <resultMap id="accounts" type="account">
        <id column="id" property="id"></id>
        <result column="name" property="name"></result>
        <result column="money" property="money"></result>
        <result column="u_id" property="u_id"></result>
        <!--映射user属性:原来的代码-->
        <!--<association property="user" javaType="User">-->
            <!--<id column="uid" property="id"></id>-->
            <!--<result column="uname" property="username"></result>-->
            <!--<result column="address" property="address"></result>-->
            <!--<result column="birthday" property="birthday"></result>-->
            <!--<result column="password" property="password"></result>-->
            <!--<result column="sex" property="sex"></result>-->
        <!--</association>-->
        <!--映射user属性:现在的代码
            column="u_id": 要通过该列去查询用户的对象
            select: 映射到了要执行的方法 ：mapperId=namespace.id

            fetchType="lazy"  :加载的方法：lazy 延迟加载,  eager:立即加载
        -->
        <association property="user" javaType="User" column="u_id"
                     select="com.itheima.dao.UserDao.findById" fetchType="lazy"></association>
    </resultMap>
    <select id="findAll" resultMap="accounts">
        select * from account
    </select>
</mapper>
5. UserDao.xml
<mapper namespace="com.itheima.dao.UserDao">
    <resultMap id="users" type="user">
        <id column="uid" property="id"></id>
        <result column="uname" property="username"></result>
    </resultMap>
   <select id="findById" resultMap="users" parameterType="int">
        select * from user where uid = #{id}
   </select>
</mapper>
```

##### c、一对多延迟加载

```
1. User.java
public class User {
    private Integer id;
    private String username;
    private String address;
    private String password;
    private Date birthday;
    private String sex;

    //一个用户对应多个账户
    private List<Account> accountList;
 }
 2. UserDao.java
 	public interface UserDao {

    /**
     * 查询所有的用户：包含账户信息
     * @return
     */
    public List<User> findAll();
}
3. AccountDao.java
public interface AccountDao {

    /**
     * 根据用户名查询对应的账户信息
     * @param userId
     * @return
     */
    public List<Account> findByUserId(Integer userId);
}
4. UserDao.xml
	<mapper namespace="com.itheima.dao.UserDao">

    <resultMap id="users" type="User">
        <id column="uid" property="id"></id>
        <result column="uname" property="username"></result>
        <result column="address" property="address"></result>
        <result column="birthday" property="birthday"></result>
        <result column="password" property="password"></result>
        <result column="sex" property="sex"></result>
        <!--映射List<Account> accountList
            coulmn :属性对应的列
        -->
        <collection property="accountList" ofType="account" column="uid"
        select="com.itheima.dao.AccountDao.findByUserId" fetchType="lazy"></collection>
    </resultMap>

    <select id="findAll" resultMap="users">
      select * from user
    </select>
</mapper>
5.AccountDao.xml
<mapper namespace="com.itheima.dao.AccountDao">
    <select id="findByUserId" resultType="account" parameterType="int">
        select * from account where u_id = #{userId}
    </select>
</mapper>
```

##### d、开启全局的延迟加载

```
<!--核心配置文件的全局的设置-->
<settings>
	<!--开启全局的延迟加载-->
	<setting name="lazyLoadingEnabled" value="true"/>
</settings>
```

#### 三、mybatis缓存

##### a、一级缓存

```
package com.itheima;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 一级缓存
 *  1,在同一sqlSession对象范围下，两次执行同一个sql语句，第二层没有执行sql语句，说明缓存的存在
 *  2.如果执行了增删改,提交操作，会清空缓存
 *  3. sqlSession.clearCache();清空缓存
 *  4. 一级缓存是SqlSession级别的, 必须是在一个sqlsession对象范围下才可以的得到一级缓存
 *
 * 一级缓存运行流程
 *  第一次执行sql语句，查询到数据，会在一级缓存中存储sql语句和数据，以 sql语句为key值, 以数据为value值
 *  在第二层执行sql语句时，会先从缓存中查询 ，以sql为key查询，得到数据，直接返回，如果没有相应的sql语
 *  句，则查询数据库
 *
 */
public class TestMybatisOTMLazy {

    @Test
    public void test(){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        //SqlSession工厂对象:单例模式
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao1 = sqlSession.getMapper(UserDao.class);

        List<User> userList1 = userDao1.findAll();
        for (User user : userList1) {
            System.out.println(user);
        }
        sqlSession.close();
        //删除操作
//        UserDao  userDao2 = sqlSession.getMapper(UserDao.class);
//        userDao2.del(14);
//        sqlSession.commit();
        //清空缓存
//        sqlSession.clearCache();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        UserDao  userDao3 = sqlSession2.getMapper(UserDao.class);
        List<User> userList3 = userDao3.findAll();
        for (User user : userList3) {
            System.out.println(user);
        }

        sqlSession2.close();
    }
}

```

##### b、二级缓存

```
package com.itheima;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 二级缓存
 *   1. 是sessionFactory级别的, 可以在多个SqlSession对象共享缓存数据
 *   2. 默认的是开启的
 *      <setting name="cacheEnabled" value="true"/>
 *   3. 在需要使用二级缓存的配置映射文件中开启
 *      <cache/>
 *   4. 需要在二级缓存中保存的pojo对象必须实现序列化接口
 *       User  implements Serializable
 *   5. 在同一namespace范围下，执行提交操作，会清空该namespace的缓存
 *   
 * 二级缓存的工作流程
 *     1，在任意一个sqlSession对象中执行了sql查询语句，当关闭sqlSession对象时,在二级缓存中保存数据：以 namespace.sql语句为key值
 *      以对象为value存储
 *     2. 当其他sqlSession对象执行时， 需要根据namespace.sql 查询是否存在缓存
 *      
 *  
 */
public class TestMybatisOTMLazy {

    @Test
    public void test(){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        //SqlSession工厂对象:单例模式
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao1 = sqlSession.getMapper(UserDao.class);

        List<User> userList1 = userDao1.findAll();
        for (User user : userList1) {
            System.out.println(user);
        }
        sqlSession.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        UserDao  userDao2 = sqlSession2.getMapper(UserDao.class);
        List<User> userList2 = userDao2.findAll();
        for (User user : userList2) {
            System.out.println(user);
        }

        sqlSession2.close();

        SqlSession sqlSession3 = sqlSessionFactory.openSession();
        UserDao  userDao3 = sqlSession3.getMapper(UserDao.class);
        List<User> userList3 = userDao3.findAll();
        for (User user : userList3) {
            System.out.println(user);
        }

        sqlSession3.close();
    }
}

```

#### 四、mybatis注解开发

##### a、实现增删改查

```
package com.itheima.dao;

import com.itheima.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserDao {

    /**
     * 查询所有
     * @return
     */
    @Select("select * from user")
//    @Results 映射结果集, value = @Result 数组对象
//    @Result 映射列名与属性名不一样的
//           id 的默认值是false 默认为非主键
//    id=true 指定该列为主键
//    注解中：只需要写列名与属性名不一样的，一样的可以不写
//    特殊情况： 如果某列数据使用了两次或者两次以上，则两次映射都需要写出来
    @Results({
            @Result(id=true, column = "uid",property = "id"),
            @Result(column = "uname",property = "username")

    })
    public List<User> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from user where uid=#{id}")
    public User findById(Integer id);

    /**
     * 根据姓名模块查询
     * @param username
     * @return
     */
    @Select("select * from user where uname like \"%\"#{username}\"%\" ")
    public List<User> findByUsername(String username);

    /**
     * 查询总的记录数
     * @return
     */
    @Select("select count(*) from user")
    public Integer findTotalCount();

    /**
     * 添加用户
     * @param user
     */
//    保存获取主键(主键回显): @SelectKey   在oracle中会使用
//    keyProperty: 主键的属性名
//    keyColumn ：主键列名
//    resultType: 主键的类型
//    before=false : 不是在添加之前， 添加之后查询
//    before=true : 在添加之前查询
//    statement: 需要执行的sql语句
//    select last_insert_id() :最后一次执行添加生成的主键
    /**
     *  在xml 主键回显
     *  <selectKey keyColumn="uid" keyProperty="id" resultType="int" order="AFTER">
            select last_insert_id()
         </selectKey>
     */
    @SelectKey(keyProperty = "id",keyColumn = "uid",resultType = Integer.class,before = false,
    statement = "select last_insert_id()")
    @Insert("insert into user values(null ,#{username},#{password},#{sex},#{address},#{birthday})")
    public void insert(User user);

    /**
     * 更新用户
     * @param user
     */
    @Update("update user set uname = #{username}, password=#{password}, sex = #{sex}" +
            ",address=#{address},birthday = #{birthday} where uid = #{id}")
    public void update(User user);

    /**
     * 删除用户
     * @param id
     */
    @Delete("delete from user where uid = #{id}")
    public void del(Integer id);
}

```

##### b、实现一对一

```
1. Account.java
public class Account {

    private Integer id;
    private String name;
    private Float money;
    private Integer u_id;
//    一个账户对应一个用户
    private User user;
}
2. AccountDao.java
public interface AccountDao {

    /**
     * 查询全部账户（包含用户信息）
     *
     *  one = @One(select = "" , fetchType=""):对应一个对象
     *          select 属性：mapperId = namespace.id
     *
     *           fetchType = FetchType.LAZY:提取方式为延迟加载,默认是立即加载
     *  Many: 对应多个对象
     *
     *
     * @return
     */
    @Select("select * from account")
    @Results({
            @Result(property = "user", column = "u_id",javaType = User.class,
                    one = @One(select = "com.itheima.dao.UserDao.findById", fetchType = FetchType.LAZY))
    })
    public List<Account> findAll();
}
3.UserDao.java
public interface UserDao {

    /**
     * 根据id查询一个用户对象
     * @param id
     * @return
     */
    @Select("select * from user where uid = #{id}")
    @Results({
            @Result(id = true,column = "uid" ,property = "id"),
            @Result(column = "uname",property = "username")
    })
    public User findById(Integer id);
}
```

##### c、实现一对多

```
1. User.java
public class User {
    private Integer id;
    private String username;
    private String address;
    private String password;
    private Date birthday;
    private String sex;

//    一个用户对应多个账户
    private List<Account> accountList;
 }
2. UserDao.java
public interface UserDao {

    /**
     * 查询所有的用户对象（包含账户信息）
     *
     *  @Result(property = "accountList", column = "uid", javaType = List.class,
            many = @Many(select = "com.itheima.dao.AccountDao.findByUserId",fetchType = FetchType.LAZY))


        javaType = List.class,可以省略


        <collection property="accountList" column="uid" ofType="Account"
            select="com.itheima.dao.AccountDao.findByUserId" fetchType="lazy">
        </collection>

     * @return
     */
    @Select( "select * from user")
    @Results({
            @Result(id=true, column = "uid" ,property = "id"),
            @Result(column = "uname",property = "username"),
            @Result(property = "accountList", column = "uid", javaType = List.class,
            many = @Many(select = "com.itheima.dao.AccountDao.findByUserId",fetchType = FetchType.LAZY))
    })
    public List<User> findAll();
}

3. AccountDao.java
public interface AccountDao {

    /**
     * 根据userId获取账户信息
     * @param userId
     * @return
     */
    @Select("select * from account where u_id = #{userId}")
    public List<Account> findByUserId(Integer userId);
}

```

##### d、动态sql

```
1. UserDao.java
public interface UserDao {

    /**
     * 根据姓名模糊查询，性别等于查询
     *
     * Provider:提供者
     * @SelectProvider： sql语句提供者
     *
     * Type: sql语句提供者的类的字节码
     * method: 提供者类中的方法
     * @return
     */
//    @Select("select * from user where sex = #{sex} and uname like \"%\"#{username}\"%\"   ")
    @SelectProvider(type = UserSqlProvider.class ,method = "findAll")
    @Results({
            @Result(id=true, column = "uid",property = "id"),
            @Result(column = "uname",property = "username")

    })
    public List<User> findByCondition(User user);

}

2. UserSqlProvider.java
/**
 * user sql 语句的提供者
 *
 * 在匿名内部类中访问的局部变量必须是final修饰的局部变量
 *  jdk 1.8以上版本，会默认添加final
 *  jdk 1.7以下版本，必须手动添加final
 */
public class UserSqlProvider {

    public String findAll(User user){
        StringBuffer sb = new StringBuffer();
        sb.append("select * from user where 1 = 1 ");
        if(user.getSex() != null){
            sb.append(" and sex = #{sex} ");
        }
        if(user.getUsername() != null){
            sb.append(" and uname like \"%\"#{username}\"%\" ");
        }

        return sb.toString();
    }
}
```

