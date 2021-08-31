package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: dj
 * @create: 2021-08-31 15:41
 * @description:
 **/
@Data
@TableName("sys_config")
public class SysConfigDO implements Serializable {

    @TableId
    private Long id;

    private String paramKey;

    private String paramValue;

    private String remark;

}
