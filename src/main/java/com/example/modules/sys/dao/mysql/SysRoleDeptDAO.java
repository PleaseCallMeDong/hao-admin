package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.sys.domain.SysRoleDeptDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: dj
 * @create: 2021-08-31 15:53
 * @description:
 **/
@Repository
public interface SysRoleDeptDAO extends BaseMapper<SysRoleDeptDO> {

    /**
     * 根据角色ID，获取部门ID列表
     */
    List<Long> queryDeptIdList(Long[] roleIds);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

}
