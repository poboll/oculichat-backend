package com.caiths.api.service;

import com.caiths.caiapicommon.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;

@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void invokeInterfaceCount() {
        boolean b = userInterfaceInfoService.invokeInterfaceCount(1L,1L);
        Assertions.assertTrue(b);
    }
}