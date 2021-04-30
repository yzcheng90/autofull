package com.suke.zhjg.common.autofull.sql;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.util.Map2BeanUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author czx
 * @title: AutoFullSqlExecutor
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2113:31
 */
@Slf4j
@UtilityClass
@Deprecated
public class AutoFullSqlExecutor {

    public <T> List<Map<String,T>> executeQuery(String sql, Map<Integer,T> params){
        if(StrUtil.isEmpty(sql)){
            log.error("解析器：SQL 为空");
            return null;
        }
        try {
            Connection conn = SqlSessionFactoryUtil.getSqlSessionConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(CollUtil.isNotEmpty(params)){
                for(Map.Entry<Integer, T> entry : params.entrySet()){
                    try {
                        stmt.setObject(entry.getKey(), entry.getValue());
                    } catch (SQLException e) {
                        log.error("解析器：设置参数失败,{}",e);
                        e.printStackTrace();
                    }
                }
            }
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            List<Map<String,T>> mapList = new ArrayList<>();
            if(meta.getColumnCount() > 0){
                while(rs.next()){
                    Map<String,T> dataRow = new HashMap<>();
                    for(int i=1; i<=meta.getColumnCount(); i++){
                        dataRow.put(meta.getColumnLabel(i), (T) rs.getObject(i));
                    }
                    mapList.add(dataRow);
                }
                rs.close();
            }
            return mapList;
        }catch (Exception e){
            log.error("解析器：执行SQL失败{}",e);
        }
        return null;
    }

    public <T> List<T> executeQuery(String sql,Class<T> resultType, Map<Integer,Object> params,int level){
        if(StrUtil.isEmpty(sql)){
            log.error("解析器：SQL 为空");
            return null;
        }
        try {
            Connection conn = SqlSessionFactoryUtil.getSqlSessionConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(CollUtil.isNotEmpty(params)){
                for(Map.Entry<Integer, Object> entry : params.entrySet()){
                    try {
                        stmt.setObject(entry.getKey(), entry.getValue());
                    } catch (SQLException e) {
                        log.error("解析器：设置参数失败,{}",e);
                        e.printStackTrace();
                    }
                }
            }
            ResultSet rs = stmt.executeQuery();
            return serializeData(resultType,rs,level);
        }catch (Exception e){
            log.error("解析器：执行SQL失败{}",e);
        }
        return null;
    }

    public <T> List<T> serializeData(Class<T> clazz, ResultSet rs,int level)  {
        List<T> lists = new ArrayList<T>();
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int counts = rsmd.getColumnCount();
            String[] columnNames = new String[counts];
            for (int i = 0; i < counts; i++) {
                columnNames[i] = rsmd.getColumnLabel(i + 1);
            }
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < counts; i++) {
                    Object value = rs.getObject(columnNames[i]);
                    if(ObjectUtil.isNotNull(value)){
                        Map2BeanUtil.setProperty(t,columnNames[i],value,level);
                    }
                }
                lists.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lists;
    }
}
