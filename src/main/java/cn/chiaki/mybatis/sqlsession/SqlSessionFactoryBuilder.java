package cn.chiaki.mybatis.sqlsession;

import cn.chiaki.mybatis.configuration.Configuration;
import cn.chiaki.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import cn.chiaki.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * 会话工厂构建者类
 * @author chenliang258
 * @date 2021-03-13 09:54
 *
 */
public class SqlSessionFactoryBuilder {

    /**
     * 根据配置文件输入流构建一个SqlSessionFactory
     * @param in 配置文件的输入流
     * @return SqlSessionFactory
     */
    public SqlSessionFactory build(InputStream in) {
        Configuration configuration = XMLConfigBuilder.loadConfiguration(in);
        return new DefaultSqlSessionFactory(configuration);
    }
}
