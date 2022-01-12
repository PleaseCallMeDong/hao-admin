package com.example.modules.sys.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.base.MyResult;
import com.example.modules.sys.dao.mysql.SysUserDAO;
import com.example.modules.sys.domain.SysDeptDO;
import com.example.modules.sys.domain.SysUserDO;
import com.example.modules.sys.form.SysUserQueryPageForm;
import com.example.modules.sys.form.UserUpdatesPasswordForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author: dj
 * @create: 2020-10-29 09:42
 * @description:
 */
@Service
public class SysUserService extends ServiceImpl<SysUserDAO, SysUserDO> {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysDeptService sysDeptService;

    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    public MyResult queryPage(SysUserQueryPageForm form) {
        String username = form.getUsername();
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        Page<SysUserDO> page = this.page(new Page<>(form.getCurrent(), form.getSize()), queryWrapper);
        for (SysUserDO sysUserDO : page.getRecords()) {
            SysDeptDO sysDept = sysDeptService.getById(sysUserDO.getDeptId());
            sysUserDO.setDeptName(sysDept.getName());
        }
        return MyResult.page(page);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserDO user) {
        user.setPassword(SecureUtil.md5(user.getPassword()));
        this.save(user);
        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysUserDO user) {
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            SysUserDO sysUser = this.getById(user.getUserId());
            sysUser.setPassword(user.getPassword());
        }
        this.updateById(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    public MyResult updatePassword(UserUpdatesPasswordForm form) {
        String password = form.getPassword();
        String newPassword = form.getNewPassword();
        Long uerId = StpUtil.getLoginIdAsLong();
        SysUserDO sysUser = this.getById(uerId);
        if (!sysUser.getPassword().equalsIgnoreCase(password)) {
            return MyResult.error("老密码错误,修改密码失败");
        }
        sysUser.setPassword(SecureUtil.md5(newPassword));
        this.updateById(sysUser);
        return MyResult.ok("密码修改成功");
    }

    public SysUserDO getByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<SysUserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDO::getUsername, username);
        queryWrapper.eq(SysUserDO::getPassword, password);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }

}
