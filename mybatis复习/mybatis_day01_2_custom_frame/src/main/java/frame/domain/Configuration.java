package frame.domain;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    private String url;
    private String username;
    private String password;
    private String driverClass;

    /**
     * 第一个泛型：String：唯一的标识:mapperId
     * 第二个泛型：Mapper：存储sql + resultType
     */
    private Map<String ,Mapper> xmlMap = new HashMap<>();

    public Map<String, Mapper> getXmlMap() {
        return xmlMap;
    }

    public void setXmlMap(Map<String, Mapper> xmlMap) {
        this.xmlMap = xmlMap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }
}
