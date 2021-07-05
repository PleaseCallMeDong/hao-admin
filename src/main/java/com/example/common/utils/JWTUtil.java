package com.example.common.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.jwt.JWT;
import com.example.modules.sys.domain.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;

/**
 * @author: dj
 * @create: 2020-03-04 08:48
 * @description:
 */
@Slf4j
public class JWTUtil {

    /**
     * 校验 token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 密钥
            byte[] key = secret.getBytes();
            // 默认验证HS265的算法
            JWT.of(token).setKey(key).verify();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            JWT jwt = JWT.of(token);
            return (String) jwt.getPayload("username");
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getSystemType(String token) {
        try {
            JWT jwt = JWT.of(token);
            return (Integer) jwt.getPayload("systemType");
        } catch (Exception e) {
            return null;
        }
    }

    public static String getAddressInfoId(String token) {
        try {
            JWT jwt = JWT.of(token);
            return (String) jwt.getPayload("addressInfo");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 生成 token
     *
     * @param username    用户名
     * @param secret      用户的密码
     * @param addressInfo 小区
     * @param systemType  系统类型
     * @return token
     */
    public static String sign(String username, String secret, String addressInfo, Integer systemType) {
        try {
            byte[] key = secret.getBytes();
            return JWT.create()
                    .setPayload("username", username.toLowerCase())
                    .setPayload("addressInfo", addressInfo)
                    .setPayload("systemType", systemType)
                    .setKey(key)
                    .sign();
        } catch (Exception e) {
            log.error("生成token错误", e);
            return null;
        }

    }

    /**
     * 登录的用户信息
     *
     * @return 用户信息
     */
    public static SysUserDO getSysUser() {
        return (SysUserDO) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 测试过期为5分钟
     */
    public static void main(String[] args) {

        String sign = sign("admin", "e10adc3949ba59abbe56e057f20f883e", "123", 0);
        log.info("测试生成一个token:");
        log.info(sign);
        System.out.println(getUsername(sign));

        String password = SecureUtil.md5("123456");
        log.info(password);
        boolean base = verify(sign, "admin", "e10adc3949ba59abbe56e057f20f883e");
        log.info("结果:{}", base);

    }

}
