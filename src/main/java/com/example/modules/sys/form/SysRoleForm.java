package com.example.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author cjl
 * @create 2020.4.8
 */
@Data
public class SysRoleForm {

    private Long roleId;

    @Size(max = 100, message = "角色名称不能超过100位")
    private String roleName;

    @Size(max = 100, message = "描述不能超过200位")
    private String remark;

    /**
     * 工程ID
     */
    @NotBlank(message = "参数错误")
    private String addressInfoId;

    private List<Long> menuIdList;

    private Integer status;

    @NotNull(message = "参数错误")
    private Integer roleType;

}
