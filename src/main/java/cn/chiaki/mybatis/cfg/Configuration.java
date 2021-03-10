package cn.chiaki.mybatis.cfg;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义mybatis的配置类
 * @author chenliang258
 * @date 2021-03-13 10:17
 */
@Data
public class Configuration {

    /**  数据库驱动  **/
    private String driver;

    /**  数据库url  **/
    private String url;

    /**  用户名  **/
    private String username;

    /**  密码  **/
    private String password;

    /**  mappers的Map集合  **/
    private Map<String, Mapper> mappers = new HashMap<>();
}
