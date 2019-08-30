package frame.factory;

import java.io.InputStream;

/**
 * 使用构建者模式创建SqlSessionFactory对象
 */
public class SqlSessionFactoryBuilder {
    /**
     * 通过配置文件的输入流创建SqlSessionFactory对象
     * @param inputStream
     * @return
     */
    public SqlSessionFactory build(InputStream inputStream){
        return new SqlSessionFactory(inputStream);
    }

    /**
     * 通过配置文件的输入流创建SqlSessionFactory对象
     * @param path
     * @return
     */
    public SqlSessionFactory build(String path){
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        return new SqlSessionFactory(inputStream);
    }

    /**
     * 没有参数创建SqlSessionFactory对象
     * 假设某位置有有sqlMapConfig
     * @return
     */
    public SqlSessionFactory build(){
        String path = "SqlMapConfig.xml";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        return new SqlSessionFactory(inputStream);
    }
}
