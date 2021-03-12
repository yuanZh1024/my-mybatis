package cn.chiaki.mybatis.utils;

import cn.chiaki.mybatis.annotations.Select;
import cn.chiaki.mybatis.configuration.Configuration;
import cn.chiaki.mybatis.configuration.MappedStatement;
import cn.chiaki.mybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义解析xml配置文件的XMLConfigBuilder类
 * @author chenliang258
 * @date 2021-03-13 15:14
 */
public class XMLConfigBuilder {

    /**
     * DOM4J解析配置文件设置Configuration对象
     * @param in 配置文件输入流
     * @return Configuration对象
     */
    @SuppressWarnings("unchecked")
    public static Configuration loadConfiguration(InputStream in) {
        try {
            Configuration configuration = new Configuration();
            // 获取SAXReader对象
            SAXReader reader = new SAXReader();
            // 根据输入流获取Document对象
            Document document = reader.read(in);
            // 获取根节点
            Element root = document.getRootElement();
            // 获取所有property节点
            List<Element> propertyElements = root.selectNodes("//property");
            // 遍历节点进行解析并设置到Configuration对象
            for(Element propertyElement : propertyElements){
                String name = propertyElement.attributeValue("name");
                if("driver".equals(name)){
                    String driver = propertyElement.attributeValue("value");
                    configuration.setDriver(driver);
                }
                if("url".equals(name)){
                    String url = propertyElement.attributeValue("value");
                    configuration.setUrl(url);
                }
                if("username".equals(name)){
                    String username = propertyElement.attributeValue("value");
                    configuration.setUsername(username);
                }
                if("password".equals(name)){
                    String password = propertyElement.attributeValue("value");
                    configuration.setPassword(password);
                }
            }
            // 取出所有mapper标签判断其配置方式
            // 这里只简单配置resource与class两种，分别表示xml配置以及注解配置
            List<Element> mapperElements = root.selectNodes("//mappers/mapper");
            // 遍历集合
            for (Element mapperElement : mapperElements) {
                // 判断mapperElement使用的属性
                Attribute resourceAttribute = mapperElement.attribute("resource");
                // 如果存在resource标签则解析xml文件
                if (resourceAttribute != null) {
                    String mapperXMLPath = resourceAttribute.getValue();
                    // 获取xml路径解析SQL并封装成mappers
                    Map<String, MappedStatement> mappers = loadMapperConfiguration(mapperXMLPath);
                    // 设置Configuration
                    configuration.setMappers(mappers);
                }
                Attribute classAttribute = mapperElement.attribute("class");
                if (classAttribute != null) {
                    String mapperClassPath = classAttribute.getValue();
                    // 解析注解对应的SQL封装成mappers
                    Map<String, MappedStatement> mappers = loadMapperAnnotation(mapperClassPath);
                    // 设置Configuration
                    configuration.setMappers(mappers);
                }
            }
            //返回Configuration
            return configuration;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 根据指定的xml文件路径解析对应的SQL语句并封装成mappers集合
     * @param mapperXMLPath xml配置文件的路径
     * @return 封装完成的mappers集合
     * @throws IOException IO异常
     */
    @SuppressWarnings("unchecked")
    private static Map<String, MappedStatement> loadMapperConfiguration(String mapperXMLPath) throws IOException {
        InputStream in = null;
        try {
            // key值由mapper接口的全限定类名与方法名组成
            // value值是要执行的SQL语句以及实体类的全限定类名
            Map<String, MappedStatement> mappers = new HashMap<>();
            // 获取输入流并根据输入流获取Document节点
            in = Resources.getResourcesAsStream(mapperXMLPath);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            // 获取根节点以及namespace属性取值
            Element root = document.getRootElement();
            String namespace = root.attributeValue("namespace");
            // 这里只针对SELECT做处理（其它SQL类型同理）
            // 获取所有的select节点
            List<Element> selectElements = root.selectNodes("//select");
            // 遍历select节点集合解析内容并填充mappers集合
            for (Element selectElement : selectElements){
                String id = selectElement.attributeValue("id");
                String resultType = selectElement.attributeValue("resultType");
                String queryString = selectElement.getText();
                String key = namespace + "." + id;
                MappedStatement mappedStatement = new MappedStatement();
                mappedStatement.setQueryString(queryString);
                mappedStatement.setResultType(resultType);
                mappers.put(key, mappedStatement);
            }
            return mappers;
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 解析mapper接口上的注解并封装成mappers集合
     * @param mapperClassPath mapper接口的位置
     * @return 封装完成的mappers集合
     * @throws IOException IO异常
     */
    private static Map<String, MappedStatement> loadMapperAnnotation(String mapperClassPath) throws Exception{
        Map<String, MappedStatement> mappers = new HashMap<>();
        // 获取mapper接口对应的Class对象
        Class<?> mapperClass = Class.forName(mapperClassPath);
        // 获取mapper接口中的方法
        Method[] methods = mapperClass.getMethods();
        // 遍历方法数组对SELECT注解进行解析
        for (Method method : methods) {
            boolean isAnnotated = method.isAnnotationPresent(Select.class);
            if (isAnnotated) {
                // 创建Mapper对象
                MappedStatement mappedStatement = new MappedStatement();
                // 取出注解的value属性值
                Select selectAnnotation = method.getAnnotation(Select.class);
                String queryString = selectAnnotation.value();
                mappedStatement.setQueryString(queryString);
                // 获取当前方法的返回值及泛型
                Type type = method.getGenericReturnType();
                // 校验泛型
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] types = parameterizedType.getActualTypeArguments();
                    Class<?> clazz = (Class<?>) types[0];
                    String resultType = clazz.getName();
                    // 给Mapper赋值
                    mappedStatement.setResultType(resultType);
                }
                // 给key赋值
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String key = className + "." + methodName;
                // 填充mappers
                mappers.put(key, mappedStatement);
            }
        }
        return mappers;
    }
}
