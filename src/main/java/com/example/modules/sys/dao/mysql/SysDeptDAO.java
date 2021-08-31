package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.sys.domain.SysDeptDO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: dj
 * @create: 2021-08-31 15:45
 * @description:
 **/
@Repository
public interface SysDeptDAO extends BaseMapper<SysDeptDO> {

    List<SysDeptDO> queryList(Map<String, Object> params);

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);

}
