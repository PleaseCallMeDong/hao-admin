package com.example.modules.sys.dao.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.modules.sys.domain.SysCnAreaDO;
import org.springframework.stereotype.Repository;

/**
 * @author: dj
 * @create: 2021-03-15 11:21
 * @description:
 */
@Repository
public interface SysCnAreaDAO extends BaseMapper<SysCnAreaDO> {

    /**
     * 根据cityCode模糊查询出错等级是1的城市
     *
     * @param cityCode 城市编码
     * @return SysCnAreaDO
     */
    SysCnAreaDO findLevel1CityByCityCode(String cityCode);

}
