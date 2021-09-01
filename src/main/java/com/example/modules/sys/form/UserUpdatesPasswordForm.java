package com.example.modules.sys.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author cjl
 * @date 2021/3/9 13:37
 * @description
 */
@Data
public class UserUpdatesPasswordForm implements Serializable {

    @NotBlank(message = "原密码不能为空")
    private String password;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
