package cn.chiaki.mybatis.utils;

import cn.chiaki.mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 用于创建数据源的工具类
 * @author chenliang258
 * @date 2021-03-13 11:16
 */
public class DataSourceUtil {

    public static Connection getConnection(Configuration configuration) {
        try {
            Class.forName(configuration.getDriver());
            return DriverManager.getConnection(configuration.getUrl(), configuration.getUsername(), configuration.getPassword());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
