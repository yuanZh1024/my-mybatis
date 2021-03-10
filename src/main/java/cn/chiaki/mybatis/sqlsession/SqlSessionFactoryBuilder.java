package cn.chiaki.mybatis.sqlsession;

import cn.chiaki.mybatis.cfg.Configuration;
import cn.chiaki.mybatis.sqlsession.defaults.DefaultSqlSessionFactory;
import cn.chiaki.mybatis.utils.XMLConfigBuilder;

import java.io.InputStream;

/**
 * SqlSessionFactory工厂类
 * @author chenliang258
 * @date 2021-03-13 09:54
 *
 * 用于创建一个SqlSessionFactory对象
 */
public class SqlSessionFactoryBuilder {

    /**
     * 根据参数的字节输入流构建一个SqlSessionFactory工厂
     * @param in 配置文件的输入流
     * @return SqlSessionFactory
     */
    public SqlSessionFactory build(InputStream in) {
        Configuration configuration = XMLConfigBuilder.loadConfiguration(in);
        return new DefaultSqlSessionFactory(configuration);
    }
}
