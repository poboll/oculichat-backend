package com.caiths.caiapiinterface.controller;

import com.caiths.caiapiinterface.model.User;
import org.springframework.web.bind.annotation.*;

/**
 * @author poboll
 * @Date 2024年10月22日 19:42
 * @Version 1.0
 * @Description API示例接口
 */
@RestController
@RequestMapping("/name")
public class ServiceController {
    @GetMapping("/")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/")
    public String getNameByPost(String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user) {
        return "POST 用户名字是" + user.getUsername();
    }
}
