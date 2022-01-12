package com.example.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.example.common.annotation.Limiter;
import com.example.common.base.MyResult;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dj
 * @create: 2021-07-01 15:58
 * @description:
 **/
@Slf4j
@RestController
@RequestMapping("sys/home")
public class HomeController {

    @GetMapping("test")
    public MyResult test() {
        StpUtil.login(1);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        return MyResult.ok();
    }

    /**
     * 测试缓存
     *
     * @return MyResult
     * @CacheUpdate(name="userCache-", key="#user.userId", value="#user")
     * @CacheInvalidate(name="userCache-", key="#userId")
     */
    @GetMapping("cacheTest")
    @Cached(name = "HomeController.cacheTest", expire = 60, cacheType = CacheType.BOTH, localLimit = 50)
    public MyResult cacheTest() {
        val time = DateUtil.now();
        log.info("time:{}", time);
        //log.info("userName:{},time:{}", userName, time);
        return MyResult.ok(time);
    }

    // 登录认证：只有登录之后才能进入该方法
    @SaCheckLogin
    @GetMapping("test1")
    public MyResult test1() {
        return MyResult.ok();
    }

    // 角色认证：必须具有指定角色才能进入该方法
    @SaCheckRole("admin")
    @GetMapping("test2")
    public MyResult test2() {
        return MyResult.ok();
    }

    // 权限认证：必须具有指定权限才能进入该方法
    @SaCheckPermission("sys:user:list")
    @GetMapping("test3")
    public MyResult test3() {
        return MyResult.ok();
    }

    //限流
    @Limiter(value = 50, timeout = 300)
    @PostMapping("test2")
    public MyResult test2(@RequestBody Object o) {
        String data = JSONObject.toJSONString(o);
        return MyResult.ok(data);
    }

    @RequestMapping("isLogin")
    public String isLogin(String username, String password) {
        return "当前会话是否登录：" + StpUtil.isLogin();
    }

}
