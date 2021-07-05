package com.example.common.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cjl
 * @create 2020/3/11
 */
public class MyResult extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;
    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int NO_TOKEN = 403;
    public static final int UNAUTHORIZED = 401;
    public static final int NO_PATH = 404;
    public static final String SUCCESS_MSG = "success";

    public MyResult() {
        put("code", SUCCESS);
        put("msg", SUCCESS_MSG);
        put("timestamp", System.currentTimeMillis());
    }

    public MyResult(int code, String msg) {
        put("code", code);
        put("msg", msg);
        put("timestamp", System.currentTimeMillis());
    }

    public static MyResult error() {
        return error(ERROR, "未知异常，请联系管理员");
    }

    public static MyResult error(String msg) {
        return error(ERROR, msg);
    }

    public static MyResult unauthorized(String msg) {
        return error(UNAUTHORIZED, msg);
    }

    public static MyResult noToken(String msg) {
        return error(NO_TOKEN, msg);
    }

    public static MyResult noPath() {
        return error(NO_PATH, "访问路径不存在");
    }

    public static MyResult error(int code, String msg) {
        MyResult r = new MyResult();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static MyResult ok(String msg) {
        MyResult r = new MyResult();
        r.put("msg", msg);
        return r;
    }

    public static MyResult ok(Map<String, Object> map) {
        MyResult r = new MyResult();
        r.putAll(map);
        return r;
    }

    public static MyResult ok() {
        return new MyResult();
    }

    public static MyResult list(List list) {
        return new MyResult().put("list", list);
    }

    public static MyResult map(Map map) {
        return new MyResult().put("map", map);
    }

    public static MyResult page(Page page) {
        return new MyResult().put("page", page);
    }

    public static MyResult info(Object o) {
        return new MyResult().put("info", o);
    }

    public static MyResult data(Object o) {
        return new MyResult().put("data", o);
    }

    public static MyResult info(String msg, Object o) {
        MyResult r = new MyResult();
        r.put("msg", msg);
        r.put("info", o);
        return r;
    }

    public static MyResult check(boolean success) {
        return success ? MyResult.ok("操作成功") : MyResult.error("操作失败");
    }

    @Override
    public MyResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }


}
