package com.example.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import com.example.modules.sys.domain.SysUserDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @date 2021/5/13 13:29
 * @description
 */
@Data
@NoArgsConstructor
public class SysUserDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private String status;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    /**
     * 微信openId
     */
    private String wxOpenId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 关联时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date associateTime;
    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;

    private Integer userType;

    public SysUserDTO(SysUserDO sysUserDO) {
        BeanUtil.copyProperties(sysUserDO, this);
    }
}
