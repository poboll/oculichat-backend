package com.caiths.caiapiinterface.controller;

import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.caiths.caiapiinterface.model.User;
import com.caiths.caiapiinterface.repository.UserRepository;
import com.caiths.caiapiinterface.util.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author poboll
 * @Date 2024年10月22日 19:42
 * @Version 1.0
 * @Description API示例接口
 */
@RestController
@RequestMapping("/name")
public class ServiceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/")
    public String getNameByPost(String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) throws UnsupportedEncodingException {

        String accessKey = request.getHeader("accessKey");
        // 防止中文乱码
        String body = URLDecoder.decode(request.getHeader("body"), StandardCharsets.UTF_8.name());
        String sign = request.getHeader("sign");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");

        // 判断是否有空字段
        if (StrUtil.hasBlank(accessKey, body, sign, nonce, timestamp)) {
            return "无权限：请求参数缺失";
        }

        // 从数据库查找用户
        User dbUser = userRepository.findByAccessKey(accessKey);
        if (dbUser == null) {
            System.out.println("传入的 accessKey: " + accessKey);
            return "无权限：无效的accessKey，传入的accessKey为：" + accessKey;
        }

        // 校验accessKey和secretKey是否匹配
        String providedSecretKey = dbUser.getSecretKey();
        if (StrUtil.isBlank(providedSecretKey)) {
            return "无权限：无效的secretKey";
        }

        // 生成服务器端的签名
        String serverSign = SignUtil.genSign(body, providedSecretKey);
        if (!StrUtil.equals(sign, serverSign)) {
            return "无权限：签名不正确";
        }

        // 时间戳是否为数字
        if (!NumberUtil.isNumber(timestamp)) {
            return "无权限：时间戳格式不正确";
        }

        // 五分钟内的请求有效
        if (System.currentTimeMillis() - Long.parseLong(timestamp) > 5 * 60 * 1000) {
            return "无权限：请求已超时";
        }

        return "POST请求成功 JSON中你的名字是：" + user.getUsername();
    }

}
