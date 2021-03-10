package cn.chiaki.mybatis.sqlsession;

/**
 * SqlSessionFactory接口
 * @author chenliang258
 * @date 2021-03-13 09:56
 */
public interface SqlSessionFactory {
    /**
     * 用于打开一个新的SqlSession对象
     * @return SqlSession
     */
    SqlSession openSession();
}
