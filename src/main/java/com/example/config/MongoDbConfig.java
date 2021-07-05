package com.example.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: dj
 * @create: 2020-09-01 13:56
 * @description:
 */
@Configuration
public class MongoDbConfig {

    @Resource
    private MongoTemplate mongoTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {

//        mongoTemplate.indexOps(DeviceCollectionDataDO.class).ensureIndex(new Index().on("createTime",
//                Sort.Direction.ASC).expire(62, TimeUnit.DAYS));

    }

}
