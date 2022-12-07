package com.suke.zhjg.common.autofull.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据包装类
 *
 * @author czx
 * @email object_czx@163.com
 */
public class R extends HashMap<String, Object> {

    public R() {
        put("code", 0);
        put("msg", "success");
    }

    public static R error() {
        return error(500, "服务未知异常");
    }

    public static R error(String msg) {
        return error(500, msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg, String data) {
        R r = new R();
        r.put("msg", msg);
        r.put("data", data);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R setPage(Object value) {
        super.put("page", value);
        return this;
    }

    public R setData(Object value) {
        super.put("data", value);
        return this;
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
