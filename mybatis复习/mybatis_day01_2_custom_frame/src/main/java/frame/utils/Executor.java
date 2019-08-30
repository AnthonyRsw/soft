package frame.utils;

import frame.domain.Configuration;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    private Configuration cfg;
    public Executor(Configuration cfg) {
        this.cfg = cfg;
    }
    private Connection conn = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    /**
     * driverClass，url,username ,password
     * @param sql
     * @param resultType
     * @return
     */
    public List executeQuery(String sql , String resultType){
        List list = new ArrayList();
        //1. 加载驱动类
        try {
            Class.forName(cfg.getDriverClass());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //2. 获取连接
            conn = DriverManager.getConnection(cfg.getUrl(), cfg.getUsername(), cfg.getPassword());
            //3. 创建Statement对象
            pst = conn.prepareStatement(sql);
            //4. 执行sql语句，返回结果集
            rs = pst.executeQuery();
            //5. 处理结果集
            //5.1 通过结果集获取所有的列名
            List<String> columnNames = new ArrayList<>();
            //获取结果集的元数据: 修饰代码的代码 -- 注解
            ResultSetMetaData metaData = rs.getMetaData();
            //获取列的个数
            int columnCount = metaData.getColumnCount();
            //根据列的索引得到列名 metaData.getColumnName() ,列的索引从1开始
            for (int i = 1; i <= columnCount ; i++) {
                String columnName = metaData.getColumnName(i);
                columnNames.add(columnName);
            }
            //通过全限类名创建对象: 获取字节码文件 -- 反射创建对象
            Class clz = Class.forName(resultType);
            while(rs.next()){ //rs.next() 判断是否有下一个元素，如果有，就应该有一个新的对象
                //创建一个对象
                Object o = clz.newInstance();
                //给对象赋值
                //获取某列对应set方法，执行set方法，赋值
                Method[] methods = clz.getMethods();
                for (Method method : methods) {
                    for (String columnName : columnNames) {
                        //使用每一个列名与每一个方法进行比较,找到某列对应set方法
                        //setusername  忽略大小写比较  setUsername
                        if(("set"+columnName).equalsIgnoreCase(method.getName())){
                            //method就是我需要的set方法，
                            //属性对应的值
                            Object columnValue = rs.getObject(columnName);
                            //执行set方法
                            //invoke(要执行的对象, 方法的参数)
                            method.invoke(o ,columnValue);
                        }
                    }
                }

                //添加到返回的集合中
                list.add(o);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void close(){
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
}
