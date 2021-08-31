package com.example.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.sys.dao.mysql.SysDeptDAO;
import com.example.modules.sys.domain.SysDeptDO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: dj
 * @create: 2021-08-31 16:04
 * @description:
 **/
@Service
public class SysDeptService extends ServiceImpl<SysDeptDAO, SysDeptDO>  {

    public List<SysDeptDO> queryList(Map<String, Object> params){
        return baseMapper.queryList(params);
    }

    public List<Long> queryDeptIdList(Long parentId) {
        return baseMapper.queryDetpIdList(parentId);
    }

    public List<Long> getSubDeptIdList(Long deptId){
        //部门及子部门ID列表
        List<Long> deptIdList = new ArrayList<>();

        //获取子部门ID
        List<Long> subIdList = queryDeptIdList(deptId);
        getDeptTreeList(subIdList, deptIdList);

        return deptIdList;
    }

    /**
     * 递归
     */
    private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
        for(Long deptId : subIdList){
            List<Long> list = queryDeptIdList(deptId);
            if(list.size() > 0){
                getDeptTreeList(list, deptIdList);
            }
            deptIdList.add(deptId);
        }
    }

}
