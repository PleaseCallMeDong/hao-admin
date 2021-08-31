package com.example.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.sys.dao.mysql.SysRoleMenuDAO;
import com.example.modules.sys.domain.SysRoleMenuDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: dj
 * @create: 2021-08-31 15:57
 * @description: 权限和菜单
 **/
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuDAO, SysRoleMenuDO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});

        if (menuIdList.size() == 0) {
            return;
        }

        //保存角色与菜单关系
        for (Long menuId : menuIdList) {
            SysRoleMenuDO sysRoleMenu = new SysRoleMenuDO();
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenu.setRoleId(roleId);

            this.save(sysRoleMenu);
        }
    }

    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

}
