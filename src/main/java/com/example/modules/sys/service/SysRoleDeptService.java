package com.example.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.sys.dao.mysql.SysRoleDeptDAO;
import com.example.modules.sys.domain.SysRoleDeptDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: dj
 * @create: 2021-08-31 16:35
 * @description:
 **/
@Service
public class SysRoleDeptService extends ServiceImpl<SysRoleDeptDAO, SysRoleDeptDO> {

    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        //先删除角色与部门关系
        deleteBatch(new Long[]{roleId});

        if(deptIdList.size() == 0){
            return ;
        }

        //保存角色与菜单关系
        for(Long deptId : deptIdList){
            SysRoleDeptDO sysRoleDept = new SysRoleDeptDO();
            sysRoleDept.setDeptId(deptId);
            sysRoleDept.setRoleId(roleId);

            this.save(sysRoleDept);
        }
    }

    public List<Long> queryDeptIdList(Long[] roleIds) {
        return baseMapper.queryDeptIdList(roleIds);
    }

    public int deleteBatch(Long[] roleIds){
        return baseMapper.deleteBatch(roleIds);
    }

}
