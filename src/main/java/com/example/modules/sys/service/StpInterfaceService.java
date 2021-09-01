package com.example.modules.sys.service;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.common.constant.RedisConstant;
import com.example.common.constant.SysConstant;
import com.example.modules.sys.domain.SysMenuDO;
import com.example.modules.sys.domain.SysRoleDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: dj
 * @create: 2021-08-31 15:20
 * @description:
 **/
@Slf4j
@Service
public class StpInterfaceService implements StpInterface {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        List<String> permsList = null;
        long userId = Long.parseLong((String) loginId);
        String key = RedisConstant.getPermListKey(userId);
        String data = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(data)) {
            try {
                permsList = JSONObject.parseArray(data, String.class);
            } catch (Exception e) {
                log.warn("{}数据错误:{}", key, data);
            }
        }
        if (null == permsList || 0 == permsList.size()) {
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
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(permsList), 10, TimeUnit.MINUTES);
        }
        return permsList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = null;
        long userId = Long.parseLong((String) loginId);
        String key = RedisConstant.getRoleListKey(userId);
        String data = stringRedisTemplate.opsForValue().get(key);
        if (StrUtil.isNotBlank(data)) {
            try {
                roleList = JSONObject.parseArray(data, String.class);
            } catch (Exception e) {
                log.warn("{}数据错误:{}", key, data);
            }
        }

        if (null == roleList || 0 == roleList.size()) {
            if (userId == SysConstant.SUPER_ADMIN) {
                List<SysRoleDO> sysRoleList = sysRoleService.list(null);
                roleList = new ArrayList<>(sysRoleList.size());
                for (SysRoleDO role : sysRoleList) {
                    roleList.add(role.getRoleName());
                }
            } else {
                roleList = sysUserService.getBaseMapper().queryAllRoles(userId);
            }
        }
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(roleList), 10, TimeUnit.MINUTES);
        return roleList;
    }
}
