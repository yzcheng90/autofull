package com.suke.zhjg.common.autofull.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author czx
 * @title: StringSQLUtil
 * @projectName zhjg
 * @description: TODO
 * @date 2020/9/98:45
 */
@UtilityClass
public class StringSQLUtil {

    public final String pattern = "(?!\\{)([^\\{\\}]+)(?=\\})";

    public Matcher parse(String sql){
        Matcher matcher = Pattern.compile(pattern).matcher(sql);
        return matcher;
    }

}
