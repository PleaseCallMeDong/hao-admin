package com.example.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.sys.dao.mysql.SysUserDAO;
import com.example.modules.sys.domain.SysUserDO;
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

//    public PageUtils queryPage(Map<String, Object> params) {
//        String username = (String)params.get("username");
//
//        IPage<SysUserDO> page = this.page(
//                new Query<SysUserDO>().getPage(params),
//                new QueryWrapper<SysUserDO>()
//                        .like(StringUtils.isNotBlank(username),"username", username)
//                        .apply(params.get(Constant.SQL_FILTER) != null, (String)params.get(Constant.SQL_FILTER))
//        );
//
//        for(SysUserDO SysUserDO : page.getRecords()){
//            SysDeptEntity sysDeptEntity = sysDeptService.getById(SysUserDO.getDeptId());
//            SysUserDO.setDeptName(sysDeptEntity.getName());
//        }
//
//        return new PageUtils(page);
//    }

    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserDO user) {
        user.setCreateTime(new Date());
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

    public boolean updatePassword(Long userId, String password, String newPassword) {
        SysUserDO sysUser = new SysUserDO();
        sysUser.setPassword(SecureUtil.md5(newPassword));
        return this.update(sysUser,
                new QueryWrapper<SysUserDO>().eq("user_id", userId).eq("password", password));
    }

    public SysUserDO getByUsernameAndPassword(String username, String password) {
        LambdaQueryWrapper<SysUserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUserDO::getUsername, username);
        queryWrapper.eq(SysUserDO::getPassword, password);
        queryWrapper.last("limit 1");
        return this.getOne(queryWrapper);
    }


}
