package com.example.common.base;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: dj
 * @create: 2021-01-11 14:26
 * @description:
 */
@Data
public class BaseTimeDO implements Serializable {

    /**
     * 创建时间
     */
    @JSONField(serialize = false)
    private Date createTime;

    /**
     * 修改时间
     */
    @JSONField(serialize = false)
    private Date updateTime;

}
