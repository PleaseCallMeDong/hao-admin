package com.example.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.SysConstant;
import com.example.modules.sys.dao.mysql.SysMenuDAO;
import com.example.modules.sys.domain.SysMenuDO;
import com.example.modules.sys.domain.SysRoleMenuDO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author: dj
 * @create: 2020-10-29 14:02
 * @description:
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuDAO, SysMenuDO> {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    public List<SysMenuDO> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuDO> menuList = queryListParentId(parentId);
        if(menuIdList == null){
            return menuList;
        }

        List<SysMenuDO> userMenuList = new ArrayList<>();
        for(SysMenuDO menu : menuList){
            if(menuIdList.contains(menu.getMenuId())){
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    public List<SysMenuDO> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    public List<SysMenuDO> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    public List<SysMenuDO> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if(userId == SysConstant.SUPER_ADMIN){
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }

    public void delete(Long menuId){
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenuDO>().eq("menu_id", menuId));
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuDO> getAllMenuList(List<Long> menuIdList){
        //查询根菜单列表
        List<SysMenuDO> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuDO> getMenuTreeList(List<SysMenuDO> menuList, List<Long> menuIdList){
        List<SysMenuDO> subMenuList = new ArrayList<>();

        for(SysMenuDO entity : menuList){
            //目录
            if(entity.getType() == SysConstant.MenuType.CATALOG.getValue()){
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }
}
