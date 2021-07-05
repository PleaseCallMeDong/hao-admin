package com.example.modules.shiro.service;

import com.example.common.constant.RedisConstant;
import com.example.common.utils.JWTUtil;
import com.example.modules.sys.domain.SysUserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: dj
 * @create: 2020-03-04 10:10
 * @description:
 */
@Slf4j
@Service
public class TokenRedisCacheService {

    @Value("${jwtTimeOut}")
    private Long jwtTimeOut;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 保存用户缓存
     *
     * @param user user
     * @return token
     */
    public String setUser2Redis(SysUserDO user, String addressInfoId) {
        String token;
        try {
            String redisKey = "";
            if (user.getSystemType().equals(1)) {
                redisKey = RedisConstant.getWxAppKey(user.getUsername());
            } else if (user.getSystemType().equals(0)) {
                redisKey = RedisConstant.getWebKey(user.getUsername());
            }
            token = JWTUtil.sign(user.getUsername(), user.getPassword(), addressInfoId, user.getSystemType());
            if (null == token) {
                return null;
            }
            stringRedisTemplate.opsForValue().set(redisKey, token, jwtTimeOut, TimeUnit.HOURS);
        } catch (Exception e) {
            log.warn("token生成和保存失败", e);
            return null;
        }
        return token;
    }

    /**
     * 从redis中获取token
     *
     * @param username   登录名
     * @param systemType
     * @return token
     */
    public String getToken4RedisByUserName(String username, Integer systemType) {
        String token = null;
        if (systemType.equals(0)) {
            token = stringRedisTemplate.opsForValue().get(RedisConstant.getWebKey(username));
        } else {
            token = stringRedisTemplate.opsForValue().get(RedisConstant.getWxAppKey(username));
        }
        return token;
    }

    /**
     * 退出,删除key
     *
     * @param redisKey redisKey
     */
    public void delKey(String redisKey) {
        stringRedisTemplate.delete(redisKey);
    }

    /**
     * 设置验证码token到redis
     *
     * @param text text
     * @return verifyCodeToken
     */
    public String setVerifyCodeToken2Redis(String text) {
        UUID uuid = UUID.randomUUID();
        String verifyCodeToken = RedisConstant.getKeyVerifyCodeToken(uuid.toString());
        stringRedisTemplate.opsForValue().set(verifyCodeToken, text, 5, TimeUnit.MINUTES);
        return uuid.toString();
    }

    /**
     * 获取验证码token
     *
     * @param code code
     * @return 验证码
     */
    public String getVerifyCodeToken4Redis(String code) {
        String verifyCodeToken = RedisConstant.getKeyVerifyCodeToken(code);
        return stringRedisTemplate.opsForValue().get(verifyCodeToken);
    }

    public String getMobileKeyCache(String mobile) {
        String key = RedisConstant.getRetrieveKey(mobile);
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setMobileKeyCache(String mobile) {
        String key = RedisConstant.getRetrieveKey(mobile);
        stringRedisTemplate.opsForValue().set(key, mobile, 1, TimeUnit.MINUTES);
    }

    public void setUpdatePasswordCache(String verifyCodeToken, HashMap<Object, Object> map) {
        String key = RedisConstant.getRetrieveKey(verifyCodeToken);
        stringRedisTemplate.opsForHash().putAll(key, map);
        stringRedisTemplate.expire(key, 10L, TimeUnit.MINUTES);
    }

    public Map getUpdatePasswordCache(String verifyCodeToken) {
        String key = RedisConstant.getRetrieveKey(verifyCodeToken);
        return stringRedisTemplate.opsForHash().entries(key);
    }
}
