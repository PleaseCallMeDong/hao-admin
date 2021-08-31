package com.example.modules.sys.service;

import cn.dev33.satoken.stp.StpInterface;
import com.example.common.constant.SysConstant;
import com.example.modules.sys.domain.SysMenuDO;
import com.example.modules.sys.domain.SysRoleDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author: dj
 * @create: 2021-08-31 15:20
 * @description:
 **/
@Service
public class StpInterfaceService implements StpInterface {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        Long userId = (Long) loginId;
        List<String> permsList;
        //系统管理员，拥有最高权限
        if (userId == SysConstant.SUPER_ADMIN) {
            List<SysMenuDO> menuList = sysMenuService.list(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuDO menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserService.getBaseMapper().queryAllPerms(userId);
        }
        return permsList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Long userId = (Long) loginId;
        List<String> roleList = new ArrayList<>();
        if (userId == SysConstant.SUPER_ADMIN) {
            List<SysRoleDO> sysRoleList = sysRoleService.list(null);
            for (SysRoleDO role : sysRoleList) {
                roleList.add(role.getRoleName());
            }
        } else {
            roleList = sysUserService.getBaseMapper().queryAllRoles(userId);
        }
        return roleList;
    }
}
