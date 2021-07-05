package com.example.modules.sys.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.exception.MyException;
import com.example.modules.sys.dao.mysql.SysUserAddressDAO;
import com.example.modules.sys.domain.SysUserAddressDO;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (SysUserAddress)表服务接口
 *
 * @author makejava
 * @since 2021-05-24 08:45:47
 */
@Service
public class SysUserAddressService extends ServiceImpl<SysUserAddressDAO, SysUserAddressDO> {

    public void associate(String addressInfoId, Long userId, Integer userType) {
        SysUserAddressDO sysUserAddressDO = new SysUserAddressDO();
        sysUserAddressDO.setUserId(userId);
        sysUserAddressDO.setAddressInfoId(addressInfoId);
        sysUserAddressDO.setUserType(userType);
        SysUserAddressDO one = this.getOne(new QueryWrapper<SysUserAddressDO>().eq("user_id", userId).eq("address_info_id", addressInfoId));
        if (one != null) {
            throw new MyException("用户已关联");
        }
        this.save(sysUserAddressDO);
    }

    public void unAssociate(String addressInfoId, List<Long> userIdList) {
        QueryWrapper<SysUserAddressDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address_info_id", addressInfoId);
        //不能删除管理员
        queryWrapper.eq(!SecurityUtils.getSubject().isPermitted("sys:user:editAdmin"),"user_type", 0);
        queryWrapper.in("user_id", userIdList);
        this.remove(queryWrapper);
    }
}
