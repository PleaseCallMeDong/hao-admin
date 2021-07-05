package com.example.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.modules.sys.dao.mysql.SysUserRoleDAO;
import com.example.modules.sys.domain.SysRoleDO;
import com.example.modules.sys.domain.SysUserRoleDO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: dj
 * @create: 2021-01-11 09:03
 * @description:
 */
@Service
public class SysUserRoleService extends ServiceImpl<SysUserRoleDAO, SysUserRoleDO> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void saveOrUpdate(Long userId, List<Long> roleIdList, String addressInfoId) {

        //先删除用户与角色关系
        this.delete(new ArrayList<Long>() {{
            add(userId);
        }}, addressInfoId);

        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        List<SysUserRoleDO> list = new ArrayList<>();
        for (Long roleId : roleIdList) {
            SysUserRoleDO sysUserRole = new SysUserRoleDO();
            sysUserRole.setUserId(userId);
            sysUserRole.setRoleId(roleId);
            sysUserRole.setAddressInfoId(addressInfoId);
            list.add(sysUserRole);
        }
        this.saveBatch(list);
    }

    public List<Long> queryRoleIdList(Long userId, String addressInfoId) {
        return this.queryRoleList(userId, addressInfoId).stream().map(SysRoleDO::getRoleId).collect(Collectors.toList());
    }

    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

    public void delete(List<Long> userId, String addressInfoId) {
        this.remove(new QueryWrapper<SysUserRoleDO>().in("user_id", userId).eq("address_info_id", addressInfoId));
    }

    public List<SysRoleDO> queryRoleList(Long userId) {
        return baseMapper.queryRoleList(userId, null);
    }

    public List<SysRoleDO> queryRoleList(Long userId, String addressInfoId) {
        String kye = RedisConstant.queryRoleListKey(userId, addressInfoId);
        String value = stringRedisTemplate.opsForValue().get(kye);
        List<SysRoleDO> list;
        if (StrUtil.isBlank(value)) {
            list = baseMapper.queryRoleList(userId, addressInfoId);
            if (null != list && list.size() > 0) {
                String dtoStr = JSON.toJSONString(list);
                stringRedisTemplate.opsForValue().set(kye, dtoStr, 1, TimeUnit.MINUTES);
            }
        } else {
            list = JSONObject.parseArray(value, SysRoleDO.class);
        }
        return list;
    }
}
