package com.example.modules.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.base.MyResult;
import com.example.common.constant.SysConstant;
import com.example.common.exception.MyException;
import com.example.modules.sys.dao.mysql.SysRoleDAO;
import com.example.modules.sys.dao.mysql.SysRoleMenuDAO;
import com.example.modules.sys.dao.mysql.SysUserRoleDAO;
import com.example.modules.sys.domain.*;
import com.example.modules.sys.dto.SysMenuDTO;
import com.example.modules.sys.dto.SysRoleDTO;
import com.example.modules.sys.form.SysRoleForm;
import com.example.modules.sys.form.SysRoleQueryPageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author cjl
 * @date 2021/3/11 14:47
 * @description
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleDAO, SysRoleDO> {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @Resource
    private SysRoleDeptService sysRoleDeptService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysDeptService sysDeptService;

    public MyResult queryPage(SysRoleQueryPageForm form) {
        String roleName = form.getRoleName();
        LambdaQueryWrapper<SysRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(roleName), SysRoleDO::getRoleName, roleName);
        Page<SysRoleDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()), queryWrapper);
        for (SysRoleDO SysRoleDO : page.getRecords()) {
            SysDeptDO sysDept = sysDeptService.getById(SysRoleDO.getDeptId());
            if (sysDept != null) {
                SysRoleDO.setDeptName(sysDept.getName());
            }
        }
        return MyResult.page(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleDO role) {
        role.setCreateTime(new Date());
        this.save(role);

        //???????????????????????????
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

        //???????????????????????????
        sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysRoleDO role) {
        this.updateById(role);

        //???????????????????????????
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

        //???????????????????????????
        sysRoleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //????????????
        this.removeByIds(Arrays.asList(roleIds));

        //???????????????????????????
        sysRoleMenuService.deleteBatch(roleIds);

        //???????????????????????????
        sysRoleDeptService.deleteBatch(roleIds);

        //???????????????????????????
        sysUserRoleService.deleteBatch(roleIds);
    }


}
