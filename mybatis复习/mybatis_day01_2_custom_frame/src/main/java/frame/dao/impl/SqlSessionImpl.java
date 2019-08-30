package frame.dao.impl;

import frame.dao.SqlSession;
import frame.domain.Configuration;
import frame.domain.Mapper;
import frame.utils.Executor;

import java.util.List;

/**
 * SqlSession的实现类
 */
public class SqlSessionImpl implements SqlSession {

    private Configuration cfg;
    private Executor executor;
    //外部传入Configuration对象
    public SqlSessionImpl(Configuration cfg) {
        this.cfg = cfg;
        //为了保证两个方法使用同一执行器
        executor = new Executor(cfg);
    }

    @Override
    public List selectList(String mapperId) {
        //根据mapperId获取sql和resultType
        Mapper mapper = cfg.getXmlMap().get(mapperId);
        String sql = mapper.getSql();
        String resultType = mapper.getResultType();
        return executor.executeQuery(sql ,resultType);
    }

    @Override
    public void close() {
        executor.close();
    }
}
