package cn.chiaki.mybatis.sqlsession.defaults;

import cn.chiaki.mybatis.configuration.Configuration;
import cn.chiaki.mybatis.configuration.MappedStatement;
import cn.chiaki.mybatis.sqlsession.SqlSession;
import cn.chiaki.mybatis.sqlsession.proxy.MapperProxyFactory;
import cn.chiaki.mybatis.utils.DataSourceUtil;
import cn.chiaki.mybatis.utils.Executor;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;

/**
 * SqlSession默认实现类
 * @author chenliang258
 * @date 2021-03-13 10:33
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;
    private final Connection connection;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
        connection = DataSourceUtil.getConnection(configuration);
    }

    /**
     * 用于创建代理对象
     * @param mapperInterfaceClass mapper接口的Class对象
     * @param <T> 泛型
     * @return mapper接口的代理对象
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getMapper(Class<T> mapperInterfaceClass) {
        return (T) Proxy.newProxyInstance(mapperInterfaceClass.getClassLoader(),
                new Class[]{mapperInterfaceClass},
                new MapperProxyFactory(configuration.getMappers(), connection));

    }

    /**
     * 用于释放资源
     */
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
