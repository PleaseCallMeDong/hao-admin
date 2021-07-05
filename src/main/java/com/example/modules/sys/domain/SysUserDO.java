package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.example.common.base.BaseTimeDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_user")
public class SysUserDO extends BaseTimeDO implements Serializable {

    /**
     * 用户ID
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private String username;

    /**
     * 密码
     */
    @TableField
    private String password;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;


    /**
     * 微信openId
     */
    private String wxOpenId;

    /**
     * 姓名
     */
    private String name;

    private Date lastLoginTime;

    @TableField(exist = false)
    private String addressInfoId;

    /**
     * 系统类型: 0:web,1:wx小程序
     */
    @TableField(exist = false)
    private Integer systemType;
}
