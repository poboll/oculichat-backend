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
 * 这是一个 API 示例接口类。
 * 使用 REST 控制器注解，定义了 API 路径和各个方法。
 * 包含 GET 和 POST 方法用于获取和提交数据。
 * 提供了校验签名和处理中文字符的方法。
 *
 * @author poboll
 * @since 1.0 (2024年10月22日)
 */
@RestController
@RequestMapping("/name")
public class ServiceController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 通过 GET 方法获取用户输入的名字。
     * @param name 用户输入的名字
     * @return 返回一个字符串，包含用户的名字。
     */
    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    /**
     * 通过 POST 方法获取用户输入的名字。
     * @param name 用户输入的名字
     * @return 返回一个字符串，包含用户的名字。
     */
    @PostMapping("/post")
    public String getNameByPost(String name) {
        return "POST 你的名字是" + name;
    }

    /**
     * 通过 POST 方法提交用户详细信息，并进行签名验证。
     * @param user 用户对象，通过请求体接收
     * @param request HttpServletRequest 对象，用于获取请求头信息
     * @return 返回处理结果，可能是错误信息或成功信息
     * @throws UnsupportedEncodingException 当字符编码不支持时抛出异常
     */
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

        // 如果请求中没有用户名，则从数据库查询
        if (StrUtil.isBlank(user.getUsername())) {
            user.setUsername(dbUser.getUsername());  // 从数据库中获取用户名
        }

        // TODO 调用次数 + 1 invokeCount
        return "POST请求成功 JSON中你的名字是：" + user.getUsername();
    }
}
