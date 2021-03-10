package cn.chiaki.mybatis.sqlsession.proxy;

import cn.chiaki.mybatis.cfg.Mapper;
import cn.chiaki.mybatis.utils.Executor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;

/**
 * 自定义MapperProxy实现invoke()方法
 * @author chenliang258
 * @date 2021-03-13 11:04
 */
public class MapperProxy implements InvocationHandler {

    private final Map<String, Mapper> mappers;
    private final Connection connection;

    public MapperProxy(Map<String, Mapper> mappers, Connection connection) {
        this.mappers = mappers;
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取方法名
        String methodName = method.getName();
        // 获取方法所在类的名称
        String className = method.getDeclaringClass().getName();
        // 组合key
        String key = className + "." + methodName;
        // 获取mappers中的Mapper对象
        Mapper mapper = mappers.get(key);
        // 判断是否有mapper
        if (mapper != null) {
            // 调用工具类查询所有
            return new Executor().query(mapper, connection);
        }
        else {
            throw new IllegalArgumentException("传入参数有误");
        }
    }
}
