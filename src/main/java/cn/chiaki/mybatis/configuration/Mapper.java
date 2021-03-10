package cn.chiaki.mybatis.configuration;

import lombok.Data;

/**
 * 用于封装执行的SQL语句和结果类型的全限定类名
 * @author chenliang258
 * @date 2021-03-13 10:22
 */
@Data
public class Mapper {

    /**  SQL语句  **/
    private String queryString;

    /**  结果类型  **/
    private String resultType;
}
