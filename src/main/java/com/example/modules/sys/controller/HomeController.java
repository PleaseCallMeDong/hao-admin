package com.example.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.common.base.MyResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author: dj
 * @create: 2021-07-01 15:58
 * @description:
 **/
@RestController
@RequestMapping("sys/home")
public class HomeController {

    @GetMapping("test")
    public MyResult test() {
        StpUtil.login(1);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        return MyResult.ok();
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
