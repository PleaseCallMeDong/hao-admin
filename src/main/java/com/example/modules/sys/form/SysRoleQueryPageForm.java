package com.example.modules.sys.form;

import com.example.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @author cjl
 * @date 2021/3/11 15:01
 * @description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysRoleQueryPageForm extends BasePageForm {
    private String roleName;

    @NotBlank(message = "参数错误")
    private String addressInfoId;

    private Integer status;
}
