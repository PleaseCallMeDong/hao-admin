package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.sys.domain.SysRoleDO;
import com.example.modules.sys.domain.SysUserRoleDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: dj
 * @create: 2021-01-11 09:03
 * @description:
 */
@Repository
public interface SysUserRoleDAO extends BaseMapper<SysUserRoleDO> {

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    List<SysRoleDO> queryRoleList(@Param("userId") Long userId, @Param("addressInfoId") String addressInfoId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

}
