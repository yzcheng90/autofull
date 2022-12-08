package com.suke.zhjg.common.autofull.sql;

import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author czx
 * @title: SqlSessionFactoryUtil
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2117:21
 */
@Slf4j
@UtilityClass
@Deprecated
public class SqlSessionFactoryUtil {

    public Connection connection;

    public Connection getSqlSessionConnection(){
        if(connection == null){
            connection = openSession();
        }

        try {
            if(connection.isClosed()){
                connection = openSession();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private Connection openSession(){
        SqlSessionFactory sqlSessionFactory = ApplicationContextRegister.getApplicationContext().getBean(SqlSessionFactory.class);
        if(sqlSessionFactory == null){
            log.error("解析器：sqlSessionFactory 获取失败");
            return null;
        }
        SqlSession session = sqlSessionFactory.openSession();
        return session.getConnection();
    }

}
