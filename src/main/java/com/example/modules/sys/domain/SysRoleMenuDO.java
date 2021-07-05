package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: dj
 * @create: 2020-10-29 09:50
 * @description: 角色与菜单对应关系
 */
@Data
@TableName("sys_role_menu")
public class SysRoleMenuDO implements Serializable {

    @TableId
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;

}
