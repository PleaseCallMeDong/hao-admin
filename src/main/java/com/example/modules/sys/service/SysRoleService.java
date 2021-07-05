package com.example.modules.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.MyException;
import com.example.modules.sys.dao.mysql.SysRoleDAO;
import com.example.modules.sys.dao.mysql.SysRoleMenuDAO;
import com.example.modules.sys.dao.mysql.SysUserRoleDAO;
import com.example.modules.sys.domain.SysRoleDO;
import com.example.modules.sys.domain.SysRoleMenuDO;
import com.example.modules.sys.domain.SysUserRoleDO;
import com.example.modules.sys.dto.SysMenuDTO;
import com.example.modules.sys.dto.SysRoleDTO;
import com.example.modules.sys.form.SysRoleForm;
import com.example.modules.sys.form.SysRoleQueryPageForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2021/3/11 14:47
 * @description
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleDAO, SysRoleDO> {

    @Resource
    private SysRoleMenuDAO sysRoleMenuDAO;
    @Resource
    private SysUserRoleDAO sysUserRoleDAO;
    @Resource
    private SysMenuService sysMenuService;

    public Page<SysRoleDTO> queryPage(SysRoleQueryPageForm form) {
        QueryWrapper<SysRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address_info_id", form.getAddressInfoId());
        queryWrapper.eq(form.getStatus() != null, "status", form.getStatus());
        queryWrapper.like(StrUtil.isNotBlank(form.getRoleName()), "role_name", form.getRoleName());
        queryWrapper.eq(SecurityUtils.getSubject().isPermitted("sys:role:editAdmin"), "role_type", 1);
        queryWrapper.orderByDesc("role_type");
        Page<SysRoleDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()), queryWrapper);
        Page<SysRoleDTO> rpage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        rpage.setRecords(page.getRecords().stream().map(SysRoleDTO::new).collect(Collectors.toList()));
        return rpage;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long roleId) {
        SysRoleDO sysRoleDO = this.getById(roleId);
        if (sysRoleDO != null && sysRoleDO.getRoleType() == 1) {
            return;
        }
        this.removeById(roleId);
        sysRoleMenuDAO.delete(new QueryWrapper<SysRoleMenuDO>().eq("role_id", roleId));
        sysUserRoleDAO.delete(new QueryWrapper<SysUserRoleDO>().eq("role_id", roleId));
    }

    @Transactional(rollbackFor = Exception.class)
    public SysRoleDO insertOrUpdate(SysRoleForm form) {
        SysRoleDO sysRoleDO = new SysRoleDO();
        BeanUtil.copyProperties(form, sysRoleDO);
        if (sysRoleDO.getRoleId() == null) {
            //新增
            SysRoleDO one = baseMapper.selectOne(new QueryWrapper<SysRoleDO>().eq("role_name", sysRoleDO.getRoleName()).eq("address_info_id", form.getAddressInfoId()));
            if (one != null) {
                throw new MyException("角色名称重复");
            }

            sysRoleDO.setRoleCode(sysRoleDO.getRoleName());
        } else {
            SysRoleDO role = this.getById(sysRoleDO.getRoleId());
            if (!role.getRoleType().equals(sysRoleDO.getRoleType())) {
                throw new MyException("管理员角色类型无法修改");
            }
        }

        if (sysRoleDO.getRoleType() == 1) {
            SecurityUtils.getSubject().checkPermission("sys:role:editAdmin");
            SysRoleDO addressDefaultRole = getAddressDefaultRole(form.getAddressInfoId());
            if (addressDefaultRole != null) {
                if (sysRoleDO.getRoleId() == null) {
                    throw new MyException("管理员角色只能存在一个");
                } else if (!sysRoleDO.getRoleId().equals(addressDefaultRole.getRoleId())) {
                    throw new MyException("管理员角色只能存在一个");
                }
            }
        }

        this.saveOrUpdate(sysRoleDO);

        //分配菜单
        if (CollectionUtil.isNotEmpty(form.getMenuIdList())) {
            //重置菜单角色
            sysRoleMenuDAO.delete(new QueryWrapper<SysRoleMenuDO>().eq("role_id", sysRoleDO.getRoleId()));
            for (Long menuId : form.getMenuIdList()) {
                SysRoleMenuDO sysRoleMenuDO = new SysRoleMenuDO();
                sysRoleMenuDO.setRoleId(sysRoleDO.getRoleId());
                sysRoleMenuDO.setMenuId(menuId);
                sysRoleMenuDAO.insert(sysRoleMenuDO);
            }
            if (sysRoleDO.getRoleType() == 1) {
                //重新修改工地下所有的角色菜单权限
                sysRoleMenuDAO.updateAddressMenu(sysRoleDO.getRoleId(), sysRoleDO.getAddressInfoId());
            }
        }
        return sysRoleDO;
    }

    public SysRoleDTO info(Long roleId) {
        SysRoleDO sysRoleDO = this.getById(roleId);
        SysRoleDTO sysRoleDTO = new SysRoleDTO(sysRoleDO);

        //循环获取所有最后一级菜单id
        List<SysMenuDTO> menuList = sysMenuService.sysMenuList();
        Set<Long> menuChildIdList = new HashSet<>();
        this.getChildMenuIdList(menuChildIdList, menuList);
        List<Long> menuIdList = new ArrayList<>();

        List<SysRoleMenuDO> sysRoleMenuList = sysRoleMenuDAO.selectList(new QueryWrapper<SysRoleMenuDO>().eq("role_id", roleId));
        for (SysRoleMenuDO sysRoleMenuDO : sysRoleMenuList) {
            if (menuChildIdList.contains(sysRoleMenuDO.getMenuId())) {
                menuIdList.add(sysRoleMenuDO.getMenuId());
            }
        }
        sysRoleDTO.setMenuIdList(menuIdList);
        return sysRoleDTO;
    }

    private void getChildMenuIdList(Set<Long> menuIdList, List<SysMenuDTO> menuList) {
        for (SysMenuDTO sysMenuDTO : menuList) {
            List<?> list = sysMenuDTO.getList();
            if (CollectionUtil.isEmpty(list)) {
                menuIdList.add(sysMenuDTO.getMenuId());
            } else {
                this.getChildMenuIdList(menuIdList, (List<SysMenuDTO>) sysMenuDTO.getList());
            }
        }
    }

    public SysRoleDO getAddressDefaultRole(String addressInfoId) {
        QueryWrapper<SysRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address_info_id", addressInfoId);
        queryWrapper.eq("role_type", 1);
        return this.getOne(queryWrapper);
    }

}
