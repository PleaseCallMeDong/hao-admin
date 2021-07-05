package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * (SysUserAddress)表实体类
 *
 * @author makejava
 * @since 2021-05-24 08:45:46
 */
@Data
@TableName("sys_user_address")
public class SysUserAddressDO {

    //id
    @TableId(type = IdType.AUTO)
    private Long id;

    //用户id
    @TableField
    private Long userId;

    //地址id
    @TableField
    private String addressInfoId;

    private Integer userType;

    private Date associateTime;
}
