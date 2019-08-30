## Mybatis第三天

#### 一、内容介绍

1. mybatis自带数据源
2. 事务问题
3. 动态的sql语句：if标签和循环标签
4. 多表之间的关系配置
  一对一
  一对多
  多对多

#### 二、mybatis自带数据源

```
	<!--数据库的环境:
    default； 指定默认的环境
        -->
    <environments default="development">
        <!--id: 环境唯一的标志 -->
        <environment id="development">
            <!--事务管理： jdbc-->
            <transactionManager type="JDBC"/>
            <!--
                dataSource: 数据源（数据库连接池）配置
                type="POOLED" ： 数据源的类型配置
                    POOLED :使用mybatis的自带数据源配置
                    UNPOOLED: 不使用数据源配置， 使用Connection操作数据库
                    JNDI:JNDI服务 数据源配置
            -->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/mybatisdb_331"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
```

#### 三、事务问题

```
package com.itheima;

import com.itheima.domain.User;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 回顾jdbc代码
 */
public class TestJDBC {

    @Test
    public void test(){

        List<User> userList = new ArrayList<>();

        //1. 注册驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://localhost:3306/mybatisdb_331";
        String username = "root";
        String password = "root";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            //2. 获取连接
            conn = DriverManager.getConnection(url, username ,password);
            //事务1 ：设置事务手动提交（不能自动提交）
            conn.setAutoCommit(false);
            // 3. SQL语句
            String sql1 = "insert into ......";
            String sql2 = "insert into ......";
            //4. 创建statement对象: Statement , PreparedStatement
            pst = conn.prepareStatement(sql1);
            //5. 执行SQL语句，返回结果集
            pst.executeUpdate();

            pst = conn.prepareStatement(sql2);
            pst.executeUpdate();
            //事务2：提交事务
            conn.commit();
        } catch (SQLException e) {
            //事务3：出现异常，回顾
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            //事务4： 还原状态，设置事务为自动提交
            if(conn != null){
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            //7. 释放资源: 先开后关
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(pst != null){
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }

        //打印结果
        for (User user : userList) {
            System.out.println(user);
        }

    }
}
```

#### 四、动态sql语句

##### 1、问题

```
	<!--
        多条件查询
        如果传递参数个数不对，会出现异常，动态sql可以解决
    -->
    <select id="findByCondition" resultMap="users" parameterType="user">
        select * from user where uname like "%"#{username}"%"  and sex = #{sex}
    </select>
```

##### 2、if语句

```
<!--
        多条件查询
    -->
    <select id="findByCondition" resultMap="users" parameterType="user">
        select * from user where 1=1
        <if test="username != null">
            and uname like "%"#{username}"%"
        </if>
        <if test="sex != null">
            and sex = #{sex}
        </if>
    </select>
```

##### 3、where语句

```
 <!--
        where : 帮助程序员处理第一个and
            多条件 都写上  and关键字
    -->
    <select id="findByCondition" resultMap="users" parameterType="user">
        select * from user
        <where>
            <if test="username != null">
                and uname like "%"#{username}"%"
            </if>
            <if test="sex != null">
                and sex = #{sex}
            </if>
        </where>
    </select>
```

##### 4、SQL片段

```
<!--SQL片段
把重复的sql语句提取出来,需要使用时引用即可

id="": 唯一标志
文本：sql语句
-->
    <sql id="select_user">select * from user</sql>

--       关联使用sql片段
--         include :包含
--         refid: 关联的sql片段的id
--         ref ：references
        <include refid="select_user"></include>
```

##### 5、foreach语句

```
<delete id="delByArray" parameterType="integer[]">
        delete from user where
        <!--
            foreach循环标签
                collection: 参数的类型：如果是集合：list,如果是数组： array
                open :前缀
                close:后缀
                separator: 分隔符
                item:  循环中的每一个对象
                index:循环中的索引（ 一般不用）
        -->
        <foreach collection="array" open="uid in (" close=")" separator="," item="id">
            #{id}
        </foreach>
    </delete>

    <delete id="delByList" parameterType="list">
        delete from user where
        <!--
            foreach循环标签
                collection: 参数的类型：如果是集合：list,如果是数组： array
                open :前缀
                close:后缀
                separator: 分隔符
                item:  循环中的每一个对象
                index:循环中的索引（ 一般不用）
        -->
        <foreach collection="list" open="uid in (" close=")" separator="," item="id">
            #{id}
        </foreach>
    </delete>
```

#### 五、多表关联

##### 1、一对一

```
a、第一种方法：accountUser extends Account
1）配置文件配置
<resultMap id="accountUsers" type="accountUser">
        <result column="uname" property="username"></result>
    </resultMap>

    <select id="findAllAccountUser" resultMap="accountUsers">
         select * from account a, user u where a.u_id = u.uid
 </select>
 2) pojo配置
 	/**
 * 继承了Account，就拥有Account中所有的属性
 * 单独添加User的属性
 */
public class AccountUser extends  Account {
    private Integer uid;
    private String username;
    private String password;
    private String address;
    private Date birthday;
    private String sex;
}
3) Dao接口
	/**
     * 查询所有的账户:包含对应的用户信息
     * @return
     */
    public List<AccountUser> findAllAccountUser();
b、第二种方法
1)配置文件
	<resultMap id="accounts" type="Account">
        <id column="id" property="id"></id>
        <result column="name" property="name"></result>
        <result column="money" property="money"></result>
        <result column="u_id" property="u_id"></result>
        <!--映射user中的属性-->
        <result column="uid" property="user.id"></result>
        <result column="uname" property="user.username"></result>
        <result column="address" property="user.address"></result>
        <result column="birthday" property="user.birthday"></result>
        <result column="sex" property="user.sex"></result>
        <result column="password" property="user.password"></result>
    </resultMap>
  2)pojo 
  public class Account {
    private Integer id;
    private String name;
    private Float money;
    private Integer u_id;
//    一个账户对应一个用户
    private User user;
  }
  3）  Dao接口
  /**
     * 查询所有的账户:包含对应的用户信息
     * @return
     */
    public List<Account> findAllAccount();
c、第三种方法
1)配置文件
	<resultMap id="accounts" type="Account">
        <id column="id" property="id"></id>
        <result column="name" property="name"></result>
        <result column="money" property="money"></result>
        <result column="u_id" property="u_id"></result>
        <!--映射user中的属性-->
        <!--association 映射单个对象
        	property：属性名
        	javaType:属性对应的类型
        -->
        <association property="user" javaType="user">
            <id column="uid" property="id"></id>
            <result column="uname" property="username"></result>
            <result column="address" property="address"></result>
            <result column="birthday" property="birthday"></result>
            <result column="password" property="password"></result>
            <result column="sex" property="sex"></result>
        </association>
    </resultMap>
  2)pojo 
  public class Account {
    private Integer id;
    private String name;
    private Float money;
    private Integer u_id;
//    一个账户对应一个用户
    private User user;
  }
  3）  Dao接口
  /**
     * 查询所有的账户:包含对应的用户信息
     * @return
     */
    public List<Account> findAllAccount();
```

##### 2、一对多

```
a.  pojo
	public class User {
    private Integer id;
    private String username;
    private String password;
    private String address;
    private Date birthday;
    private String sex;
    //一个用户对应多个账户
    private List<Account> accountList;
  }
b. 配置文件
	    <resultMap id="users" type="User">
        <id column="uid" property="id"></id>
        <result column="uname" property="username"></result>
        <result column="password" property="password"></result>
        <result column="address" property="address"></result>
        <result column="birthday" property="birthday"></result>
        <result column="sex" property="sex"></result>
        <!--Collection : 映射accountList 属性
                property: 对应的属性名
                ofType: 集合中的元素类型: association 中javaType效果一致
        -->
        <collection property="accountList" ofType="account">
            <id column="id" property="id"></id>
            <result column="name" property="name"></result>
            <result column="money" property="money"></result>
            <result column="u_id" property="u_id"></result>
        </collection>
    </resultMap>

    <select id="findAll" resultMap="users">
         select * from user u left join account a on u.uid = a.u_id
    </select>
 c、Dao接口
 /**
     * 返回所有的user对象，包含用户对应账户信息
     * @return
     */
    public List<User> findAll();
```

##### 3、多对多

```
a. sql语句
create table role(
	id int primary key auto_increment,
	roleName varchar(20),
	roleDesc varchar(20)
)

create table user_role(
	uid int , 
	rid int , 
	-- 联合主键: 两列以上为主键列, 两列不能同时相同
	primary key(uid,rid),
	foreign key(uid) references user(uid),
	foreign key(rid) references role(id)
)
b. pojo
  public class User {
    private Integer id;
    private String username;
    private String password;
    private String address;
    private Date birthday;
    private String sex;
//    一个用户有多个角色
    private List<Role> roleList;
  }
c、配置文件
<mapper namespace="com.itheima.dao.UserDao">
    <resultMap id="users" type="user">
        <id column="uid" property="id"></id>
        <result column="uname" property="username"></result>
        <result column="address" property="address"></result>
        <result column="birthday" property="birthday"></result>
        <result column="sex" property="sex"></result>
        <result column="password" property="password"></result>
        <!--一个用户对应多个角色: roleList-->
        <collection property="roleList" ofType="role">
            <id column="id" property="id"></id>
            <result column="roleName" property="roleName"></result>
            <result column="roleDesc" property="roleDesc"></result>
        </collection>
    </resultMap>

    <select id="findAll" resultMap="users">
      select u.* ,r.* from user u left join user_role ur on u.uid = ur.uid left join role r on r.id = ur.rid
    </select>
</mapper>
d、dao接口
    /**
     * 返回所有的user对象，包含用户对应的角色对象
     * @return
     */
    public List<User> findAll();
```

角色到用户的关系

```
a、pojo
	public class Role {
    private Integer id;
    private String roleName;
    private String roleDesc;
//    一个角色对应多个用户'
    private List<User> userList;
  }
b、配置文件
<mapper namespace="com.itheima.dao.RoleDao">
    <resultMap id="roles" type="role">
        <id column="id" property="id"></id>
        <result column="roleName" property="roleName"></result>
        <result column="roleDesc" property="roleDesc"></result>
        <collection property="userList" ofType="User">
            <id column="uid" property="id"></id>
            <result column="uname" property="username"></result>
            <result column="address" property="address"></result>
            <result column="birthday" property="birthday"></result>
            <result column="sex" property="sex"></result>
            <result column="password" property="password"></result>
        </collection>
    </resultMap>

    <select id="findAll" resultMap="roles">
      select r.*,u.* from role r left join user_role ur on r.id = ur.rid left join user u on u.uid = ur.uid
    </select>
</mapper>
c、dao接口
		/**
     * 查询所有的角色，包含用户对象
     * @return
     */
    public List<Role> findAll();
```



