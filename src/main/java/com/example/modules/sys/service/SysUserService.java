package com.example.modules.sys.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.exception.MyException;
import com.example.common.utils.JWTUtil;
import com.example.common.utils.MyStrUtil;
import com.example.modules.shiro.service.TokenRedisCacheService;
import com.example.modules.sys.dao.mysql.SysUserDAO;
import com.example.modules.sys.domain.SysUserAddressDO;
import com.example.modules.sys.domain.SysUserDO;
import com.example.modules.sys.dto.SysUserDTO;
import com.example.modules.sys.form.SysUserForm;
import com.example.modules.sys.form.SysUserQueryPageForm;
import com.example.modules.sys.form.UserPasswordForm;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: dj
 * @create: 2020-10-29 09:42
 * @description:
 */
@Service
public class SysUserService extends ServiceImpl<SysUserDAO, SysUserDO> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysUserAddressService sysUserAddressService;

    @Resource
    private TokenRedisCacheService tokenRedisCacheService;

    /**
     * 查询用户列表
     *
     * @param form SysUserQueryPageForm
     * @return Page
     */
    public Page<SysUserDTO> queryPage(SysUserQueryPageForm form) {
        Page<SysUserDTO> page = new Page<>(form.getCurrent(), form.getSize());
        if (SecurityUtils.getSubject().isPermitted("sys:user:editAdmin")) {
            //只展示管理员的信息
            form.setUserType(1);
        }
        page = baseMapper.userPage(page, form);
        page.getRecords().forEach(sysUserDTO -> {

            sysUserDTO.setMobile(MyStrUtil.anonymousPhone(sysUserDTO.getMobile()));
            sysUserDTO.setUsername(MyStrUtil.anonymousUserName(sysUserDTO.getUsername()));
        });
        return page;
    }

    /**
     * 关联用户
     *
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    public void associate(SysUserForm form) {
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", form.getName());
        queryWrapper.eq(StrUtil.isNotBlank(form.getUsername()), "username", form.getUsername());
        queryWrapper.eq(StrUtil.isNotBlank(form.getMobile()), "mobile", form.getMobile());
        SysUserDO one = this.getOne(queryWrapper);
        if (one == null) {
            throw new MyException("找不到该用户");
        } else {
            int userType = 0;
            if (SecurityUtils.getSubject().isPermitted("sys:user:editAdmin")) {
                userType = 1;
            }
            bindAddress(one.getUserId(), form.getAddressInfoId(), userType);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void unAssociate(SysUserQueryPageForm form) {
        List<Long> userIdList = form.getUserId();
        String addressInfoId = form.getAddressInfoId();
        if (CollectionUtil.isNotEmpty(userIdList)) {
            List<SysUserDO> users = this.list(new QueryWrapper<SysUserDO>().in("user_id", userIdList));
            sysUserAddressService.unAssociate(addressInfoId, users.stream().map(SysUserDO::getUserId).collect(Collectors.toList()));
            //解除角色
            sysUserRoleService.delete(users.stream().map(SysUserDO::getUserId).collect(Collectors.toList()), addressInfoId);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void updatePassWord(UserPasswordForm passwordForm, Long userId,boolean encrypt) {
        SysUserDO sysUserDO = baseMapper.selectById(userId == null ? JWTUtil.getSysUser().getUserId() : userId);
        String password = encrypt?SecureUtil.md5(passwordForm.getPassword()):passwordForm.getPassword();
        if (password.equals(sysUserDO.getPassword())) {
            String newPassword = SecureUtil.md5(passwordForm.getNewPassword());
            sysUserDO.setPassword(newPassword);
            baseMapper.updateById(sysUserDO);
            String userByUsernameKey = RedisConstant.getUserByUsernameKey(sysUserDO.getUsername());
            stringRedisTemplate.delete(userByUsernameKey);
        } else {
            throw new MyException("原密码不正确");
        }
    }

    /**
     * 根据用户名查询账户
     *
     * @param username 用户名
     * @return 用户
     */
    public SysUserDO getUserByUsername(String username) {
        String key = RedisConstant.getUserByUsernameKey(username);
        String value = stringRedisTemplate.opsForValue().get(key);
        SysUserDO sysUserDO;
        if (StrUtil.isBlank(value)) {
            sysUserDO = this.getOne(new QueryWrapper<SysUserDO>().eq("username", username));
            String dtoStr = JSON.toJSONString(sysUserDO);
            stringRedisTemplate.opsForValue().set(key, dtoStr, 1, TimeUnit.DAYS);
        } else {
            sysUserDO = JSONObject.parseObject(value, SysUserDO.class);
        }
        return sysUserDO;
    }


    public SysUserDTO info(Long userId, String addressInfoId) {
        SysUserDO user = getById(userId);
        SysUserDTO sysUserDTO = new SysUserDTO(user);
        if (null != user) {
            //获取用户所属的角色列表
            List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId, addressInfoId);
            sysUserDTO.setRoleIdList(roleIdList);
        }
        return sysUserDTO;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(SysUserForm sysUserForm) {
        Long userId = sysUserForm.getUserId();
        List<Long> roleIdList = sysUserForm.getRoleIdList();
        String addressInfoId = sysUserForm.getAddressInfoId();
        if (userId == null || addressInfoId == null) {
            throw new MyException("参数错误");
        }
        sysUserRoleService.saveOrUpdate(userId, roleIdList, addressInfoId);
    }

    public SysUserDO getByWxOpenId(String wxOpenId) {
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("wx_open_id", wxOpenId);
        SysUserDO sysUserDO = baseMapper.selectOne(queryWrapper);
        return sysUserDO;
    }

    @Transactional(rollbackFor = Exception.class)
    public SysUserDO insert(SysUserForm form) {
        SysUserDO sysUserDO = new SysUserDO();
        sysUserDO.setUserId(form.getUserId());
        sysUserDO.setName(form.getName());
        sysUserDO.setMobile(form.getMobile());
        sysUserDO.setStatus(1);
        if (sysUserDO.getUserId() == null) {
            if (StrUtil.isBlank(form.getPassword())) {
                throw new MyException("密码不能为空");
            }
            sysUserDO.setUsername(form.getUsername());
            sysUserDO.setPassword(SecureUtil.md5(form.getPassword()));
            QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", sysUserDO.getUsername());
            SysUserDO one = this.getOne(queryWrapper);
            if (one != null) {
                throw new MyException("用户名已被使用");
            }
        }
        this.save(sysUserDO);
        return sysUserDO;
    }


    public List<Long> queryUserMenuId(Long userId, String addressInfoId) {
        return baseMapper.queryUserMenuId(userId, addressInfoId);
    }


    public SysUserDO getUserByMobile(String mobile) {
        if (StrUtil.isBlank(mobile)) {
            throw new MyException("手机号为空");
        }
        QueryWrapper<SysUserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        return this.getOne(queryWrapper);
    }

    public SysUserDO getAddressDefaultUser(String addressInfoId) {
        QueryWrapper<SysUserAddressDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address_info_id", addressInfoId);
        queryWrapper.eq("user_type", 1);
        SysUserAddressDO one = sysUserAddressService.getOne(queryWrapper);
        if (one != null) {
            return this.getById(one.getUserId());
        } else {
            return null;
        }
    }

    public void bindAddress(Long userId, String addressInfoId, Integer userType) {
        sysUserAddressService.associate(addressInfoId, userId, userType);
    }


}
