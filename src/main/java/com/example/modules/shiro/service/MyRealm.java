package com.example.modules.shiro.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.common.utils.JWTUtil;
import com.example.modules.sys.domain.SysRoleDO;
import com.example.modules.sys.domain.SysUserDO;
import com.example.modules.sys.service.SysMenuService;
import com.example.modules.sys.service.SysUserRoleService;
import com.example.modules.sys.service.SysUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义实现 ShiroRealm，包含认证和授权两大模块
 *
 * @author MrBird
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private TokenRedisCacheService tokenRedisCacheService;

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 授权模块，获取用户角色和权限
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     *
     * @param token token
     * @return AuthorizationInfo 权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        SysUserDO user = (SysUserDO) token.getPrimaryPrincipal();
        if (null == user) {
            return null;
        }
        Long userId = user.getUserId();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        Set<String> permsSet = sysMenuService.getUserPermList(userId, user.getAddressInfoId());

        Set<String> roleList = new HashSet<>();
        for (SysRoleDO roleDO : sysUserRoleService.queryRoleList(userId, user.getAddressInfoId())) {
            roleList.add(roleDO.getRoleName());
        }

        // 获取用户角色集
        simpleAuthorizationInfo.setRoles(roleList);

        // 获取用户权限集
        simpleAuthorizationInfo.setStringPermissions(permsSet);
        return simpleAuthorizationInfo;

    }

    /**
     * 用户认证
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
        String token = (String) authenticationToken.getCredentials();
        String username = JWTUtil.getUsername(token);
        String addressInfoId = JWTUtil.getAddressInfoId(token);
        Integer systemType = JWTUtil.getSystemType(token);
        if(systemType==null){
            throw new UnknownAccountException("token错误");
        }
        //判断redis是否存在该token
        String redisToken = tokenRedisCacheService.getToken4RedisByUserName(username, systemType);

        if (!token.equalsIgnoreCase(redisToken)) {
            throw new UnknownAccountException("token错误");
        }

        if (StringUtils.isBlank(username)) {
            throw new UnknownAccountException("用户名或密码错误");
        }

        // 通过用户名查询用户信息
        SysUserDO user = sysUserService.getUserByUsername(username);

        if (user == null) {
            throw new IncorrectCredentialsException("用户名或密码错误");
        }

        if (!JWTUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("token校验不通过");
        }
        user.setAddressInfoId(addressInfoId);
        user.setSystemType(systemType);
        return new SimpleAuthenticationInfo(user, token, "MyRealm");

    }
}
