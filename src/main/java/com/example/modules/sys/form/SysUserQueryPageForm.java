package com.example.modules.sys.form;

import com.example.common.base.BasePageForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author: dj
 * @create: 2021-01-09 15:41
 * @description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserQueryPageForm extends BasePageForm {

    private List<Long> userId;

    private String username;

    private String mobile;

}
