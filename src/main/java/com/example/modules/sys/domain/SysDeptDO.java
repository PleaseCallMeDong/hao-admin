package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: dj
 * @create: 2021-08-31 15:44
 * @description:
 **/
@Data
@TableName("sys_dept")
public class SysDeptDO implements Serializable {

    /**
     * 部门ID
     */
    @TableId
    private Long deptId;

    /**
     * 上级部门ID，一级部门为0
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 上级部门名称
     */
    @TableField(exist=false)
    private String parentName;

    private Integer orderNum;

    @TableLogic
    private Integer delFlag;

    /**
     * ztree属性
     */
    @TableField(exist=false)
    private Boolean open;

    @TableField(exist=false)
    private List<?> list;

}
