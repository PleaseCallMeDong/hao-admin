package com.example.modules.sys.dto;

import cn.hutool.core.bean.BeanUtil;
import com.example.modules.sys.domain.SysRoleDO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.8
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleDTO {
    private Long roleId;

    private String roleName;

    private String remark;

    /**
     * 部门ID
     */
    private String addressInfoId;

    private List<Long> menuIdList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;


    /**
     * 状态 0启动 1禁用
     */
    private Integer status;

    private Integer roleType;

    public SysRoleDTO(SysRoleDO sysRoleDO) {
        BeanUtil.copyProperties(sysRoleDO, this);
    }
}
