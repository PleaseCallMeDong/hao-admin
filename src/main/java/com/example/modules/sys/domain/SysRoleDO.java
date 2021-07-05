package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dj
 * @create: 2020-10-29 09:43
 * @description: 角色
 */
@Data
@TableName("sys_role")
public class SysRoleDO implements Serializable {

    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门ID
     */
    private String addressInfoId;

    private Date createTime;

    /**
     * 状态 0启动 1禁用
     */
    private Integer status;

    private Integer roleType;
}
