package com.example.modules.sys.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.common.base.MyResult;
import com.example.modules.sys.domain.SysUserDO;
import com.example.modules.sys.form.LoginForm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author: dj
 * @create: 2021-09-01 08:57
 * @description:
 **/
@Service
public class SysLoginService {

    @Resource
    private SysUserService sysUserService;

    public MyResult login(LoginForm form) {
        String username = form.getUsername();
        String password = SecureUtil.md5(form.getPassword());
        SysUserDO sysUser = sysUserService.getByUsernameAndPassword(username, password);
        if (null == sysUser) {
            return MyResult.error("账号密码错误");
        } else {
            int status = sysUser.getStatus();
            if (0 == status) {
                return MyResult.error("账号被禁用");
            } else {
                StpUtil.login(sysUser.getUserId());
                return MyResult.info(StpUtil.getTokenInfo());
            }
        }
    }

    public MyResult logout() {
        StpUtil.logout();
        return MyResult.ok("退出成功");
    }

}
