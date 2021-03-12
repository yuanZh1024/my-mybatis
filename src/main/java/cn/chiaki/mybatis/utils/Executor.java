package cn.chiaki.mybatis.utils;

import cn.chiaki.mybatis.configuration.MappedStatement;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Executor类负责执行SQL语句并封装结果集
 * @author chenliang258
 * @date 2021-03-13 15:32
 */
public class Executor {

    public Object query(MappedStatement mappedStatement, Connection connection) {
        return selectList(mappedStatement, connection);
    }

    /**
     * selectList()方法
     * @param mappedStatement mapper接口
     * @param connection 数据库连接
     * @param <T> 泛型
     * @return 结果
     */
    public <T> List<T> selectList(MappedStatement mappedStatement, Connection connection) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // 取出SQL语句
            String queryString = mappedStatement.getQueryString();
            // 取出结果类型
            String resultType = mappedStatement.getResultType();
            Class<?> clazz = Class.forName(resultType);
            // 获取PreparedStatement对象并执行
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            // 从结果集对象封装结果
            List<T> list = new ArrayList<>();
            while(resultSet.next()) {
                //实例化要封装的实体类对象
                @SuppressWarnings("unchecked")
                T obj = (T) clazz.getDeclaredConstructor().newInstance();
                // 取出结果集的元信息
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                // 取出总列数
                int columnCount = resultSetMetaData.getColumnCount();
                // 遍历总列数给对象赋值
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSetMetaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(columnName);
                    PropertyDescriptor descriptor = new PropertyDescriptor(columnName, clazz);
                    Method writeMethod = descriptor.getWriteMethod();
                    writeMethod.invoke(obj, columnValue);
                }
                // 把赋好值的对象加入到集合中
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(preparedStatement, resultSet);
        }
    }

    /**
     * 释放资源
     * @param preparedStatement preparedStatement对象
     * @param resultSet resultSet对象
     */
    private void release(PreparedStatement preparedStatement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
