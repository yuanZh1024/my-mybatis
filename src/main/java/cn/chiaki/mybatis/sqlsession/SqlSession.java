package cn.chiaki.mybatis.sqlsession;

/**
 * SqlSession接口
 * @author chenliang258
 * @date 2021-03-13 09:59
 */
public interface SqlSession {

    /**
     * 根据参数创建一个代理对象
     * @param mapperInterfaceClass mapper接口的Class对象
     * @param <T> 泛型
     * @return mapper接口的代理实例
     */
    <T> T getMapper(Class<T> mapperInterfaceClass);

    /**
     * 释放资源
     */
    void close();
}
