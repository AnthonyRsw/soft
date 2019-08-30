package frame.factory;

import frame.dao.SqlSession;
import frame.dao.impl.SqlSessionImpl;
import frame.domain.Configuration;
import frame.domain.Mapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * 使用工厂模式创建SqlSession对象
 */
public class SqlSessionFactory {
    //配置文件的输入流
    private InputStream inputStream;
    public SqlSessionFactory(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    private Configuration cfg ;
    public SqlSession openSession(){
        cfg = new Configuration();
        //给cfg赋值
        loadConfiguration();
        //创建sqlSession对象
        SqlSession sqlSession = new SqlSessionImpl(cfg);
        return sqlSession;
    }

    private void loadConfiguration() {
        //dom4j (document for java)解析时 ，直接可以解析InputStream
        //创建解析对象
        SAXReader reader = new SAXReader();
        // 获取文档对象: 根据字节输入流获取文档对象
        Document doc = null;
        try {
            doc = reader.read(inputStream);
            // 获取根节点
            Element root = doc.getRootElement();
            //获取所有property节点
            //selectNodes("//property"); 能得到文档所有的property， 参数必须加 //前缀
            List<Element> list = root.selectNodes("//property");
            for (Element element : list) {
                //element.attributeValue()：  可以获取指定属性的的属性值
                String value = element.attributeValue("value");
                String name = element.attributeValue("name");
                // 判断是哪个一个属性
                if(name.equals("driver")){
                    cfg.setDriverClass(value);
                }
                if(name.equals("url")){
                    cfg.setUrl(value);
                }
                if(name.equals("username")){
                    cfg.setUsername(value);
                }
                if(name.equals("password")){
                    cfg.setPassword(value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //加载映射文件
        //得到根节点对象
        Element root = doc.getRootElement();
        //得到mappers节点
        //element(mappers)：得到第一个名字mappers的子节点
        //elements(mappers)：得到所有名字mappers的子节点
        //elements()：得到所有子节点
        Element mappers = root.element("mappers");
        //获取所有的mapper子节点
        List<Element> mapperList = mappers.elements("mapper");
        //获取mapper的资源路径
        for (Element element : mapperList) {
            String path = element.attributeValue("resource");
            loadXmlConfiguration(path);
        }
    }

    /**
     * 读取映射文件
     */
    private void loadXmlConfiguration(String path){
        //通过资源路径获取资源输入流对象
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(path);
        //dom4j解析对象
        SAXReader reader = new SAXReader();
        try {
            //获取文档对象
            Document doc = reader.read(inputStream);
            //获取根节点
            Element root = doc.getRootElement();
            //获取根节点的属性namespace的属性值
            String namespace = root.attributeValue("namespace");
            //获取根节点中所有的子节点
            List<Element> elements = root.elements();
            //遍历每一个节点，获取 id, resultType ,sql语句
            for (Element element : elements) {
                String id = element.attributeValue("id");
                String resultType = element.attributeValue("resultType");
                //节点中的文本就是sql语句
                String sql = element.getTextTrim();
                // key :mapperId = namespace + id
                // value :Mapper = SQL + resultType
                String key = namespace +"."+ id;
                Mapper value = new Mapper();
                value.setSql(sql);
                value.setResultType(resultType);
                cfg.getXmlMap().put(key ,value);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
