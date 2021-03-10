package cn.chiaki.mybatis.sqlsession.defaults;

import cn.chiaki.mybatis.cfg.Configuration;
import cn.chiaki.mybatis.sqlsession.SqlSession;
import cn.chiaki.mybatis.sqlsession.SqlSessionFactory;

/**
 * SqlSessionFactory的默认实现类
 * @author chenliang258
 * @date 2021-03-13 10:33
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 用于创建一个新的操作数据库对象
     * @return SqlSession
     */
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
