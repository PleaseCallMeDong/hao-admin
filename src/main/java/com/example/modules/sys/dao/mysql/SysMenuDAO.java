package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.sys.domain.SysMenuDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: dj
 * @create: 2020-10-29 14:20
 * @description:
 */
@Repository
public interface SysMenuDAO extends BaseMapper<SysMenuDO> {

    /**
     * 根据父菜单，查询子菜单
     * @param parentId 父菜单ID
     */
    List<SysMenuDO> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<SysMenuDO> queryNotButtonList();

}
