package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: dj
 * @create: 2021-03-15 11:00
 * @description:
 */
@Data
@TableName("sys_cn_area")
public class SysCnAreaDO implements Serializable {

    @TableId
    private Integer id;

    private Integer level;

    private Long parentCode;

    private Long areaCode;

    private Integer zipCode;

    private Integer cityCode;

    private String name;

    private String shortName;

    private String mergerName;

    private String pinyin;

    private Double lng;

    private Double lat;

    @TableField(exist = false)
    private SysCnAreaDO childSysCnAreaDO;
}
