package com.example.modules.sys.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典
 *
 * @author dj
 */
@Data
@TableName("sys_dict")
public class SysDictDO implements Serializable {

    @TableId
    private Long id;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典类型
     */
    private String type;

    /**
     * 字典码
     */
    private String code;

    /**
     * 字典值
     */
    private String value;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标记  -1：已删除  0：正常
     */
    @TableLogic
    private Integer delFlag;

}
