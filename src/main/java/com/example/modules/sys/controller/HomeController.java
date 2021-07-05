package com.example.modules.sys.controller;

import com.example.common.base.MyResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: dj
 * @create: 2021-07-01 15:58
 * @description:
 **/
@RestController
@RequestMapping("sys/home")
public class HomeController {

    @GetMapping("test")
    public MyResult test() {
        return MyResult.ok();
    }

    @GetMapping("test1")
    public MyResult test1() {
        return MyResult.ok();
    }

}
