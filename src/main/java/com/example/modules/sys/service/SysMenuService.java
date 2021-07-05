package com.example.modules.sys.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.RedisConstant;
import com.example.common.constant.SysConstant;
import com.example.common.exception.MyException;
import com.example.common.utils.JWTUtil;
import com.example.modules.sys.dao.mysql.SysMenuDAO;
import com.example.modules.sys.dao.mysql.SysRoleMenuDAO;
import com.example.modules.sys.domain.SysMenuDO;
import com.example.modules.sys.domain.SysRoleDO;
import com.example.modules.sys.domain.SysRoleMenuDO;
import com.example.modules.sys.domain.SysUserDO;
import com.example.modules.sys.dto.SysMenuDTO;
import com.example.modules.sys.form.SysMenuForm;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: dj
 * @create: 2020-10-29 14:02
 * @description:
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuDAO, SysMenuDO> {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleMenuDAO sysRoleMenuDAO;

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 菜单列表
     *
     * @return
     */
    public List<SysMenuDTO> sysMenuList() {
        SysUserDO user = JWTUtil.getSysUser();
        Long userId = user.getUserId();
        String addressInfoId = user.getAddressInfoId();
        if (userId == null || userId == SysConstant.SUPER_ADMIN) {
            //管理员权限账户获取到的菜单列表
            return getMenuTreeList(getMenuIdList(null, null), false, true);
        } else {
            //其他用户获取到的菜单列表
            SysRoleDO defaultRole = sysRoleService.getAddressDefaultRole(addressInfoId);
            return getMenuTreeList(sysRoleMenuDAO.queryMenuIdList(defaultRole.getRoleId()), false, true);
        }
    }

    /**
     * 获取用户的菜单
     *
     * @return
     */
    public List<SysMenuDTO> getUserMenuList(Long userId, String addressInfoId) {
        return getMenuTreeList(getMenuIdList(userId, addressInfoId), true, false);
    }

    private Set<Long> getMenuIdList(Long userId, String addressInfoId) {
        List<Long> menuIdList;
        if (userId == null || userId == SysConstant.SUPER_ADMIN) {
            menuIdList = this.list().stream().map(SysMenuDO::getMenuId).collect(Collectors.toList());
        }
        //用户菜单列表
        else {
            menuIdList = sysUserService.queryUserMenuId(userId, addressInfoId);
        }
        return new HashSet<>(menuIdList);
    }

    private List<SysMenuDTO> getMenuTreeList(Set<Long> menuIdList, boolean ignoreButton, boolean ignoreInvisible) {
        QueryWrapper<SysMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("order_num is null", "order_num");
        queryWrapper.eq(ignoreInvisible, "invisible", "0");
        queryWrapper.lt(ignoreButton, "type", SysConstant.MenuType.BUTTON.getValue());
        List<SysMenuDTO> menuList = this.list(queryWrapper).stream().map(SysMenuDTO::new).collect(Collectors.toList());
        List<SysMenuDTO> iniList = menuList.stream().filter(sysMenuDTO -> sysMenuDTO.getParentId() == 0 && menuIdList.contains(sysMenuDTO.getMenuId())).collect(Collectors.toList());
        Map<Long, List<SysMenuDTO>> map = new HashMap<>();
        for (SysMenuDTO sysMenuDTO : menuList) {
            if (menuIdList != null && !menuIdList.contains(sysMenuDTO.getMenuId())) {
                continue;
            }
            List<SysMenuDTO> childList = map.computeIfAbsent(sysMenuDTO.getParentId(), k -> new ArrayList<>());
            childList.add(sysMenuDTO);
        }
        this.recursion(iniList, map);
        return iniList;
    }

    /**
     * 递归查询
     *
     * @param iniList 最上级菜单
     * @param map     key: 菜单父级id,value: 菜单list
     * @return
     */
    private void recursion(List<SysMenuDTO> iniList, Map<Long, List<SysMenuDTO>> map) {
        for (SysMenuDTO sysMenuDTO : iniList) {
            List<SysMenuDTO> list = map.computeIfAbsent(sysMenuDTO.getMenuId(), k -> null);
            if (list != null && CollectionUtil.isNotEmpty(list)) {
                sysMenuDTO.setList(list);
                recursion(list, map);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long menuId) {
        List<SysMenuDO> childList = this.list(new QueryWrapper<SysMenuDO>().eq("parent_id", menuId));
        if (CollectionUtil.isNotEmpty(childList)) {
            throw new MyException("请先删除子菜单");
        }
        this.removeById(menuId);
        sysRoleMenuDAO.delete(new QueryWrapper<SysRoleMenuDO>().eq("menu_id", menuId));
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdate(SysMenuForm form) {
        SysMenuDO sysMenuDO = new SysMenuDO();
        BeanUtil.copyProperties(form, sysMenuDO);
        if (null != form.getParentList()) {
            sysMenuDO.setParentArray(JSON.toJSONString(form.getParentList()));
        }
        this.saveOrUpdate(sysMenuDO);
    }

    public SysMenuDTO getMenuSelectTree() {
        //查询列表数据
        List<SysMenuDTO> sysMenuDOList = this.getMenuTreeList(getMenuIdList(null, null), true, false);
        //添加顶级菜单
        SysMenuDTO root = new SysMenuDTO();
        root.setMenuId(0L);
        root.setName("首页");
        root.setParentId(-1L);
        root.setList(sysMenuDOList);
        return root;
    }

    public Set<String> getUserPermList(Long userId, String addressInfoId) {
        String kye = RedisConstant.getUserPermListKey(userId, addressInfoId);
        String value = stringRedisTemplate.opsForValue().get(kye);
        Set<String> permsSet;
        if (StrUtil.isBlank(value)) {
            permsSet = new HashSet<>();
            List<String> permsList;
            if (userId == SysConstant.SUPER_ADMIN) {
                permsList = baseMapper.selectList(new QueryWrapper<SysMenuDO>().isNotNull("perms")).stream().map(SysMenuDO::getPerms).collect(Collectors.toList());
            } else {
                permsList = sysUserService.getBaseMapper().queryAllPerms(userId, addressInfoId);
            }
            for (String perms : permsList) {
                if (StringUtils.isBlank(perms)) {
                    continue;
                }
                permsSet.addAll(Arrays.asList(perms.trim().split(",")));
            }
            String dtoStr = JSON.toJSONString(permsSet);
            stringRedisTemplate.opsForValue().set(kye, dtoStr, 1, TimeUnit.MINUTES);
        } else {
            permsSet = new HashSet<>(JSON.parseArray(value, String.class));
        }
        return permsSet;
    }
}
