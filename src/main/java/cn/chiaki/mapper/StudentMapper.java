package cn.chiaki.mapper;

import cn.chiaki.entity.Student;
import cn.chiaki.mybatis.annotations.Select;

import java.util.List;

/**
 * @author chenliang258
 * @date 2021/3/11 11:47
 */
public interface StudentMapper {

    /**
     * 查询表中所有记录
     * @return 所有记录集合
     */
    @Select("SELECT * FROM STUDENT")
    List<Student> findAll();

}
