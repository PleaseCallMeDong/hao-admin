package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.modules.sys.domain.SysUserDO;
import com.example.modules.sys.dto.SysUserDTO;
import com.example.modules.sys.form.SysUserQueryPageForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: dj
 * @create: 2020-10-29 09:40
 * @description:
 */
@Repository
public interface SysUserDAO extends BaseMapper<SysUserDO> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    List<String> queryAllPerms(Long userId);

    List<String> queryAllRoles(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);
}
