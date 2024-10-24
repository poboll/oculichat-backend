package com.caiths.caiapiinterface;

import com.caiths.caiapisdk.client.CaiApiClient;
import com.caiths.caiapisdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@SpringBootTest
class CaiApiInterfaceApplicationTests {

    @Resource
    private CaiApiClient caiApiClient;

    @Resource
    private CaiApiConfig caiApiConfig;  // 注入配置类

    @Test
    void contextLoads() throws UnsupportedEncodingException {
        // 打印从 application.yml 中获取的 accessKey 和 secretKey
        System.out.println("本地配置的 accessKey: " + caiApiConfig.getAccessKey());
        System.out.println("本地配置的 secretKey: " + caiApiConfig.getSecretKey());

        String mdo = caiApiClient.getNameByGet("在虎");
        User user = new User();
        user.setUsername("哭哭");
        String userNameByPost = caiApiClient.getUserNameByPost(user);
        System.out.println(mdo);
        System.out.println(userNameByPost);
    }
}
