package cn.chiaki.test;

import cn.chiaki.entity.Student;
import cn.chiaki.mapper.StudentMapper;
import cn.chiaki.mybatis.io.Resources;
import cn.chiaki.mybatis.sqlsession.SqlSession;
import cn.chiaki.mybatis.sqlsession.SqlSessionFactory;
import cn.chiaki.mybatis.sqlsession.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 自定义MyBatis测试
 * @author chenliang258
 * @date 2021-03-13 17:44
 */
public class MybatisTest {

    private InputStream in;
    private SqlSession sqlSession;

    @Before
    public void init() {
        // 读取MyBatis的配置文件
        in = Resources.getResourcesAsStream("mybatis-config.xml");
        // 创建SqlSessionFactory的构建者对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 使用builder创建SqlSessionFactory对象
        SqlSessionFactory factory = builder.build(in);
        // 使用factory创建sqlSession对象
        sqlSession = factory.openSession();
    }

    @Test
    public void testMyMybatis() {
        // 使用SqlSession创建Mapper接口的代理对象
        StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
        // 使用代理对象执行方法
        List<Student> students = studentMapper.findAll();
        System.out.println(students);
    }

    @After
    public void close() throws IOException {
        // 关闭资源
        sqlSession.close();
        in.close();
    }
}
