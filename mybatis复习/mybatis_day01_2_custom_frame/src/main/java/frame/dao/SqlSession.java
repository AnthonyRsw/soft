package frame.dao;

import java.util.List;

/**
 * 框架的入口，提供增删改查方法
 *
 */
public interface SqlSession {

    /**
     * 执行查询的方法
     * @param mapperId  唯一的id
     * @return   返回对象的集合
     */
    public List selectList(String mapperId);

    /**
     * 释放资源
     */
    public void close();
}
