package com.example.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.example.common.annotation.SysLog;
import com.example.common.base.MyResult;
import com.example.common.validate.Insert;
import com.example.modules.sys.dto.SysUserLoginDTO;
import com.example.modules.sys.form.LoginForm;
import com.example.modules.sys.form.SysUserForm;
import com.example.modules.sys.service.SysLoginService;
import com.example.modules.sys.service.WxService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author cjl
 * @date 2021/5/11 10:56
 * @description
 */
@RestController
@RequestMapping("sys")
public class SysLoginController {

    @Resource
    private SysLoginService sysLoginService;

    @Resource
    private WxService wxService;

    @SysLog("登录")
    @PostMapping("login")
    public MyResult login(@RequestBody @Validated LoginForm loginForm) {
        SysUserLoginDTO login = sysLoginService.login(loginForm);
        return MyResult.info(login);
    }


    @RequestMapping("checkOut/{addressInfoId}")
    @RequiresAuthentication
    public MyResult checkOut(@PathVariable String addressInfoId) {
        SysUserLoginDTO loginDTO = sysLoginService.checkOut(addressInfoId);
        return MyResult.info(loginDTO);
    }

    @GetMapping("getVerificationCode")
    public MyResult getPicVerificationCode(@RequestParam(required = false) String mobile) {
        if (StrUtil.isNotBlank(mobile)) {
            return sysLoginService.getSmsVerificationCode(mobile);
        } else {
            return sysLoginService.getPicVerificationCode();
        }
    }

    @GetMapping("getRegisterVerificationCode")
    public MyResult getRegisterVerificationCode(@RequestParam String mobile) {
        return sysLoginService.getRegisterVerificationCode(mobile);
    }

    @GetMapping("logout")
    @RequiresAuthentication
    public MyResult logout() {
        return sysLoginService.webLogout();
    }

    @PostMapping("confirmVerifyCode")
    public MyResult confirmVerifyCode(@RequestParam String verifyCode, @RequestParam String verifyCodeToken) {
        return sysLoginService.confirmUpdatePasswordVerifyCode(verifyCode, verifyCodeToken);
    }

    @PostMapping("resetPassword")
    public MyResult resetPassword(@RequestParam String verifyCodeToken, @RequestParam String newPassword) {
        return sysLoginService.updatePasswordBySms(verifyCodeToken, newPassword);
    }

    @PostMapping("register")
    public MyResult register(@Validated(Insert.class) @RequestBody SysUserForm sysUserForm) {
        sysLoginService.register(sysUserForm);
        return MyResult.ok();
    }

    @PostMapping("wxLogout")
    @RequiresAuthentication
    public MyResult wxLogout() {
        return wxService.logout();
    }


    /**
     * 获取微信openId
     * 若该openId已绑定用户,则直接登录
     * 若没有返回openId
     *
     * @param code
     * @return
     */
    @PostMapping("getWxOpenId")
    public MyResult getOpenId(@RequestParam String code) {
        return wxService.getWxOpenId(code);
    }

    @PostMapping("getWxAppQrCode")
    @RequiresAuthentication
    public MyResult getWxAppQrCode() throws Exception {
        return wxService.getQrCode();
    }
}
