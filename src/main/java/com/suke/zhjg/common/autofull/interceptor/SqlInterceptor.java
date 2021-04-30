package com.suke.zhjg.common.autofull.interceptor;

import cn.hutool.core.collection.CollUtil;
import com.suke.zhjg.common.autofull.cache.AutoFullRedisCache;
import com.suke.zhjg.common.autofull.util.SQLTableUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author czx
 * @title: SqlInterceptor
 * @projectName zhjg
 * @description: TODO 删除自动填充的缓存数据
 *  如果是增删改操作 就删除缓存
 * @date 2021/3/1714:36
 */
@Slf4j
@Component
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlInterceptor implements Interceptor {

    public final String boundSqlStr = "delegate.boundSql.sql";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        if (target instanceof Executor) {

        }else {
            // StatementHandler
            StatementHandler statementHandler = (StatementHandler) target;
            // 目前只有StatementHandler.getBoundSql方法args才为null
            if (null == args) {

            }else {
                statementHandler = this.realTarget(statementHandler);
                MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
                String originalSql = (String) metaStatementHandler.getValue(boundSqlStr);
                String sqlHead = originalSql.substring(0, originalSql.indexOf(" ")).toLowerCase();
                List<String> tables = new ArrayList<>();
                switch (sqlHead) {
                    case "insert":
                        Insert insert = (Insert) CCJSqlParserUtil.parse(originalSql);
                        tables = SQLTableUtil.getTableName(insert);
                        break;
                    case "update":
                        Update update = (Update) CCJSqlParserUtil.parse(originalSql);
                        tables = SQLTableUtil.getTableName(update);
                        break;
                    case "delete":
                        Delete delete = (Delete) CCJSqlParserUtil.parse(originalSql);
                        tables = SQLTableUtil.getTableName(delete);
                        break;
                }
                if(CollUtil.isNotEmpty(tables)){
                    tables.forEach(table -> AutoFullRedisCache.deleteData(null,table));
                }
            }
        }
        // 传递给下一个拦截器处理
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof Executor || target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获得真正的处理对象,可能多层代理.
     */
    @SuppressWarnings("unchecked")
    public static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue("h.target"));
        }
        return (T) target;
    }
}
