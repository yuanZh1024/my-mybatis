package cn.chiaki.mybatis.io;

import java.io.InputStream;

/**
 * 自定义Resources获取配置转换为输入流
 * @author chenliang258
 * @date 2021-03-13 09:50
 */
public class Resources {

    /**
     * 获取配置文件并转换为输入流
     * @param filePath 配置文件路径
     * @return 配置文件输入流
     */
    public static InputStream getResourcesAsStream(String filePath) {
        return Resources.class.getClassLoader().getResourceAsStream(filePath);
    }
}
