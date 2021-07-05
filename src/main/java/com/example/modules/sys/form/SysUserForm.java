package com.example.modules.sys.form;

import com.example.common.validate.Insert;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author cjl
 * @date 2021/5/13 13:32
 * @description
 */
@Data
public class SysUserForm {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名太长")
    private String username;

    /**
     * 密码
     */
    @Size(max = 100, message = "密码太长")
    @NotBlank(groups = Insert.class, message = "密码不能为空")
    private String password;

    /**
     * 手机号
     */
    @Size(max = 20, message = "手机号不能超过20位")
    @NotBlank(groups = Insert.class, message = "手机号不能为空")
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer status;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    /**
     * 微信openId
     */
//    private String wxOpenId;

    /**
     * 姓名
     */
    @Size(max = 10, message = "姓名不能超过10位")
    @NotBlank(groups = Insert.class, message = "姓名不能为空")
    private String name;

    private String addressInfoId;


    @NotBlank(groups = Insert.class, message = "验证码不能为空")
    private String verifyCode;

    private String verifyCodeToken;
}
