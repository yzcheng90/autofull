package com.suke.zhjg.common.autofull.sql;

import cn.hutool.core.util.ArrayUtil;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @author czx
 * @title: AutoFullSqlJdbcTemplate
 * @projectName zhjg
 * @description: TODO
 * @date 2021/3/1715:21
 */
@Slf4j
@UtilityClass
public class AutoFullSqlJdbcTemplate {

    private JdbcTemplate getJdbcTemplate(){
        JdbcTemplate jdbcTemplate = ApplicationContextRegister.getApplicationContext().getBean(JdbcTemplate.class);
        return jdbcTemplate;
    }

    public <T> T queryObj(String sql, Class<T> resultType, Object... params){
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if(ArrayUtil.isNotEmpty(params)){
            try {
                return jdbcTemplate.queryForObject(sql, resultType, params);
            }catch (EmptyResultDataAccessException e){
                return null;
            }
        }else {
            try {
                return jdbcTemplate.queryForObject(sql,resultType);
            }catch (EmptyResultDataAccessException e){
                return null;
            }
        }
    }

    public <T> List<T> queryList(String sql, Class<T> resultType, Object... params){
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        if(ArrayUtil.isNotEmpty(params)){
            return jdbcTemplate.queryForList(sql,resultType,params);
        }else {
            return jdbcTemplate.queryForList(sql,resultType);
        }
    }

}
