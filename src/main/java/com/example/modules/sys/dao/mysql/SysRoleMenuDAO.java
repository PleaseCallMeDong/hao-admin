package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.sys.domain.SysRoleMenuDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author: dj
 * @create: 2021-01-16 14:02
 * @description:
 */
@Repository
public interface SysRoleMenuDAO extends BaseMapper<SysRoleMenuDO> {

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
