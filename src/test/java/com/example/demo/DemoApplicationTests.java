package com.example.demo;

import com.example.modules.sys.service.SysUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DemoApplicationTests {

    @Resource
    private SysUserService sysUserService;

    @Test
    void contextLoads() {
        sysUserService.removeById(2);
    }

}
