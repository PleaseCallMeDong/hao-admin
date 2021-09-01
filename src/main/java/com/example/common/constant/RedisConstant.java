package com.example.common.constant;

/**
 * @author: dj
 * @create: 2020-10-29 14:12
 * @description: redis静态变量
 */
public class RedisConstant {

    /**
     * 验证码token
     */
    public static final String KEY_VERIFY_CODE_TOKEN = "haoAdmin:verifyCodeToken:";

    /**
     * 权限列表
     */
    public static final String KEY_QUERY_ROLE_LIST = "haoAdmin:roleList:";

    public static final String KEY_GET_USER_PERM_LIST = "haoAdmin:permList:";

    public static final String KEY_GET_USER_BY_USERNAME = "haoAdmin:getUserByUsername:";

    /**
     * 找回电话缓存
     */
    public static final String RETRIEVE_KEY = "haoAdmin:retrievekey:";

    public static String getUserByUsernameKey(String username) {
        return KEY_GET_USER_BY_USERNAME + username;
    }

    public static String getKeyVerifyCodeToken(String code) {
        return KEY_VERIFY_CODE_TOKEN + code;
    }

    public static String getRetrieveKey(String mobile) {
        return RETRIEVE_KEY + mobile;
    }

    public static String getRoleListKey(Long userId) {
        return KEY_QUERY_ROLE_LIST + userId;
    }

    public static String getPermListKey(Long userId) {
        return KEY_GET_USER_PERM_LIST + userId;
    }


}
