package com.suke.zhjg.common.autofull.util;

import lombok.experimental.UtilityClass;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author czx
 * @title: SQLTableUtil
 * @projectName zhjg
 * @description: TODO
 * @date 2021/3/1716:34
 */
@UtilityClass
public class SQLTableUtil {

    public List<String> getTableName(Statement statement){
        List<String> list = new ArrayList<>();
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        List tableList = tablesNamesFinder.getTableList(statement);
        for (Iterator iter = tableList.iterator(); iter.hasNext();) {
            String tableName = iter.next().toString();
            list.add(tableName);
        }
        return list;
    }

    public List<String> getSelectTableName(String sql){
        Select select = null;
        try {
            select = (Select) CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        if(select == null){
            return null;
        }
        return getTableName(select);
    }

}
