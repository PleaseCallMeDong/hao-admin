package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: dj
 * @create: 2021-08-31 15:51
 * @description:
 **/
@Data
@TableName("sys_role_dept")
public class SysRoleDeptDO implements Serializable {

    @TableId
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;

}
