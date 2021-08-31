package com.example.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.base.MyResult;
import com.example.modules.sys.dao.mysql.SysDictDAO;
import com.example.modules.sys.domain.SysDictDO;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: dj
 * @create: 2021-08-31 16:09
 * @description:
 **/
@Service
public class SysDictService extends ServiceImpl<SysDictDAO, SysDictDO> {

    public MyResult queryPage(Map<String, Object> params) {
        String name = (String) params.get("name");
        Page<SysDictDO> page = this.page(
                new Page<>(1, 2),
                new QueryWrapper<SysDictDO>()
                        .like(StrUtil.isNotBlank(name), "name", name)
        );
        return MyResult.page(page);
    }

}
