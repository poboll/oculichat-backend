package com.caiths.caiapiinterface.util;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

import java.util.Map;

/**
 * @author poboll
 * @Date 2024年10月22日 15:31
 * @Version 1.0
 * @Description 签名工具
 */
public class SignUtil {

    /**
     * 生成签名
     * @param body
     * @param secretKey
     * @return
     */
    public static String genSign(String body, String secretKey) {

        // Hutool摘要加密
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        // 将密钥放入内容中进行单向加密
        String content = body.toString() + "." + secretKey;
        return md5.digestHex(content);
    }
}
