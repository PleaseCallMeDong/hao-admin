package com.example.common.constant;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.util.StrUtil;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author: dj
 * @create: 2020-10-29 14:12
 * @description: 系统静态变量
 */
public class SysConstant {

    public final static String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter BASE_TIME_DTF = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN);

    /**
     * 设备报警字典类型
     */
    public static final String DEVICE_WARN_DICT_TYPE = "deviceWarn";

    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;

    /**
     * 管理员角色
     */
    public static final String ADMIN = "admin";

    /**
     * 微信app用户角色
     */
    public static final String WX_APP_USER = "wxAppUser";

    /**
     * 岗位类型
     */
    public static final String DICT_TYPE_WORK_TYPE = "workType";

    /**
     * kafka的topic
     */
    public static final String KAFKA_TOPIC_D1_DATA = "sms_d1";

    /**
     * 设备类型
     */
    public static final String DICT_TYPE_DEVICE_TYPE = "deviceType";


    /**
     * 设备过期时间
     */
    public static final String DICT_TYPE_DEVICE_EXPIRE_TIME = "deviceExpireTime";

    /**
     * 设备对应的岗位能进的地方
     */
    public static Map<Long, Set<String>> ADDRESS_WORK_AREA = new HashMap<>();

    /**
     * 指示牌设备对应的工地
     */
    public static Map<Long, String> DEVICE_ADDRESS = new HashMap<>();

    /**
     * 菜单类型
     */
    public enum MenuType {

        /**
         * 目录
         */
        CATALOG(0),

        /**
         * 菜单
         */
        MENU(1),

        /**
         * 按钮
         */
        BUTTON(2),

        /**
         * api
         */
        API(3);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    /**
     * 正在使用的设备-判断设备属于哪个工地
     * 设备id
     * 工地
     */
    public static Map<Long, String> deviceAddressInfoMap = new HashMap<>();

    public static void addDeviceAddressInfoMap(Long deviceSysId, String addressInfoId) {
        SysConstant.deviceAddressInfoMap.put(deviceSysId, addressInfoId);
    }

    public static boolean deviceIsOk(Long deviceSysId) {
        boolean flag = false;
        String addressInfoId = deviceAddressInfoMap.get(deviceSysId);
        if (StrUtil.isNotBlank(addressInfoId)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 设备时间的map
     * key 设备唯一标识
     * value 设备最新时间
     */
    public static Map<Long, Long> deviceLastTimeMap = new HashMap<>();


    /**
     * 安全帽设备类型
     */
    public static final Long DEVICE_SAFETY_HAT_TYPE = 1793L;

    /**
     * 指示牌设备类型
     */
    public static final Long DEVICE_INDICATOR_TYPE = 1794L;

    /**
     * 考勤统计缓存
     */
    public static TimedCache<String, Long> hrCountTimedCache = CacheUtil.newTimedCache(1000 * 30);

}
