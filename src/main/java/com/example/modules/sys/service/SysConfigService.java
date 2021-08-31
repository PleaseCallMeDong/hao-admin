package com.example.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.modules.sys.dao.mysql.SysConfigDAO;
import com.example.modules.sys.domain.SysConfigDO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @author: dj
 * @create: 2021-08-31 15:59
 * @description:
 **/
@Service
public class SysConfigService extends ServiceImpl<SysConfigDAO, SysConfigDO> {

//    public PageUtils queryPage(Map<String, Object> params) {
//        String paramKey = (String)params.get("paramKey");
//
//        IPage<SysConfigDO> page = this.page(
//                new Query<SysConfigDO>().getPage(params),
//                new QueryWrapper<SysConfigDO>()
//                        .like(StrUtil.isNotBlank(paramKey),"param_key", paramKey)
//                        .eq("status", 1)
//        );
//
//        return new PageUtils(page);
//    }

    public void saveConfig(SysConfigDO config) {
        this.save(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(SysConfigDO config) {
        this.updateById(config);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] ids) {
        for(Long id : ids){
            SysConfigDO config = this.getById(id);
        }

        this.removeByIds(Arrays.asList(ids));
    }

}
