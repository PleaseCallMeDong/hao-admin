package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.example.common.base.BaseTimeDO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

    private Date lastLoginTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
     * 角色ID列表
     */
    @TableField(exist=false)
    private List<Long> roleIdList;


    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门名称
     */
    @TableField(exist=false)
    private String deptName;

}
