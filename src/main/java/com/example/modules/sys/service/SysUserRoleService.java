package com.example.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.sys.dao.mysql.SysUserRoleDAO;
import com.example.modules.sys.domain.SysRoleDO;
import com.example.modules.sys.domain.SysUserRoleDO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: dj
 * @create: 2021-01-11 09:03
 * @description: 用户和权限
 */
@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleDAO, SysUserRoleDO> {

    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.remove(new QueryWrapper<SysUserRoleDO>().eq("user_id", userId));

        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        for (Long roleId : roleIdList) {
            SysUserRoleDO sysUserRole = new SysUserRoleDO();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            this.save(sysUserRole);
        }

    }

    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

}
