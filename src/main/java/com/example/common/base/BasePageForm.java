package com.example.common.base;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author cjl
 * @create 2020/3/10
 */
@Data
public class BasePageForm {

    @Min(value = 1, message = "当前页必须大于0")
    private Integer current = 1;

    @Max(value = 999, message = "每页大小不能超过999")
    private Integer size = 10;

}
