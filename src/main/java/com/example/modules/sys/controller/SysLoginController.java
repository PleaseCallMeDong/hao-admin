package com.example.modules.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.example.common.base.MyResult;
import com.example.modules.sys.form.LoginForm;
import com.example.modules.sys.service.SysLoginService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: dj
 * @create: 2021-09-01 09:36
 * @description:
 **/
@RestController
@RequestMapping("sys")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;

    @PostMapping("login")
    public MyResult login(@Validated @RequestBody LoginForm form) {
        return sysLoginService.login(form);
    }

    @SaCheckLogin
    @GetMapping("logout")
    public MyResult logout() {
        return sysLoginService.logout();
    }

}
