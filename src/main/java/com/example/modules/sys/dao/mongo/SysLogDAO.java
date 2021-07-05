package com.example.modules.sys.dao.mongo;

import com.example.modules.sys.domain.SysLogDO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: dj
 * @create: 2020-03-02 16:26
 * @description:
 */
@Repository
public interface SysLogDAO extends MongoRepository<SysLogDO, String> {
}
