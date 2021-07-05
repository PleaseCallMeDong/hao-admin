package com.example.common.constant;

/**
 * @author: dj
 * @create: 2020-10-29 14:12
 * @description: redis静态变量
 */
public class RedisConstant {

    /**
     * wxAppToken
     */
    public static final String KEY_WX_APP_TOKEN = "sitemisManageV2:wxAppToken:";

    /**
     * webToken
     */
    public static final String KEY_WEB_TOKEN = "sitemisManageV2:webToken:";

    /**
     * 验证码token
     */
    public static final String KEY_VERIFY_CODE_TOKEN = "sitemisManageV2:verifyCodeToken:";

    /**
     * 权限列表
     */
    public static final String KEY_QUERY_ROLE_LIST = "sitemisManageV2:queryRoleList:";

    public static final String KEY_GET_USER_BY_USERNAME = "sitemisManageV2:getUserByUsername:";

    public static final String KEY_GET_USER_PERM_LIST = "sitemisManageV2:getUserPermList:";

    /**
     * 找回电话缓存
     */
    public static final String RETRIEVE_KEY = "sitemisManageV2:retrievekey:";

    public static String getUserByUsernameKey(String username) {
        return KEY_GET_USER_BY_USERNAME + username;
    }

    public static String getWxAppKey(String username) {
        return KEY_WX_APP_TOKEN + username;
    }

    public static String getWebKey(String username) {
        return KEY_WEB_TOKEN + username;
    }

    public static String getKeyVerifyCodeToken(String code) {
        return KEY_VERIFY_CODE_TOKEN + code;
    }

    public static String getRetrieveKey(String mobile) {
        return RETRIEVE_KEY + mobile;
    }

    public static String queryRoleListKey(Long userId, String addressInfoId) {
        return KEY_QUERY_ROLE_LIST + userId + ":" + addressInfoId;
    }

    public static String getUserPermListKey(Long userId, String addressInfoId) {
        return KEY_GET_USER_PERM_LIST + userId + ":" + addressInfoId;
    }


}
