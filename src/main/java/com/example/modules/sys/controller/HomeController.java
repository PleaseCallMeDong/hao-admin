package com.example.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.common.base.MyResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("test2")
    public MyResult test2(@RequestBody Object o) {
        String data = JSONObject.toJSONString(o);
        return MyResult.ok(data);
    }

}
