package com.example.travel_project.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/*
该类型处理器用于将 List<String> 类型的数据与数据库中的 VARCHAR 类型进行映射。这段代码的功能主要是处理 List<String> 类型数据的存储和读取，
特别是在存取数据库时，如何将其转换为字符串存储到数据库中，以及从数据库中取出字符串并转换为 List<String>
如何使用ListTypeHandler：在需要的entity中打上标记，如：TableName(value = "tb_user", autoResultMap = true)
                       并且对需要映射的字段进行转换，如：@TableField(jdbcType = JdbcType.VARCHAR, typeHandler = ListTypeHandler.class)
*/


@Slf4j
//@MappedJdbcTypes(JdbcType.VARCHAR): 这个注解告诉 MyBatis，当数据库中的字段类型为 VARCHAR 时，使用此类型处理器进行处理
@MappedJdbcTypes(JdbcType.VARCHAR)
//@MappedTypes({List.class}): 这个注解指定了此类型处理器适用于 List 类型，具体是 List<String>
@MappedTypes({List.class})
public class ListTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String hobbys = dealListToOneStr(parameter);
        ps.setString(i, hobbys);
    }

//    @Override
//    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
//        return Arrays.asList(rs.getString(columnName).split(","));
//    }
//
//    @Override
//    public List<String> getResult(ResultSet rs, int columIndex) throws SQLException {
//        return Arrays.asList(rs.getString(columIndex).split(","));
//    }
//
//    @Override
//    public List<String> getResult(CallableStatement cs, int columIndex) throws SQLException {
//        String hobbys = cs.getString(columIndex);
//        return Arrays.asList(hobbys.split(","));
//    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        // 判断是否为null或空字符串
        if (columnValue == null || columnValue.isEmpty()) {
            return Arrays.asList(); // 返回空列表
        }
        return Arrays.asList(columnValue.split(","));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columIndex) throws SQLException {
        String columnValue = rs.getString(columIndex);
        // 判断是否为null或空字符串
        if (columnValue == null || columnValue.isEmpty()) {
            return Arrays.asList(); // 返回空列表
        }
        return Arrays.asList(columnValue.split(","));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columIndex) throws SQLException {
        String columnValue = cs.getString(columIndex);
        // 判断是否为null或空字符串
        if (columnValue == null || columnValue.isEmpty()) {
            return Arrays.asList(); // 返回空列表
        }
        return Arrays.asList(columnValue.split(","));
    }


    private String dealListToOneStr(List<String> parameter){
        if (parameter == null || parameter.size() <= 0){
            return null;
        }
        String res = "";
        for (int i=0; i<parameter.size(); i++){
            if (i == parameter.size() - 1){
                res += parameter.get(i);
                return res;
            }
            res += parameter.get(i) + ",";
        }
        return null;
    }
}
