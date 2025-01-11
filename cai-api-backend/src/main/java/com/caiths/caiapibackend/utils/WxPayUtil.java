package com.caiths.caiapibackend.utils;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付签名工具类。
 * <p>
 * 提供方法用于获取微信支付回调请求中的签名相关信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public class WxPayUtil {

    /**
     * 获取微信支付回调请求头中的签名信息。
     *
     * @param request HTTP 请求对象
     * @return {@link SignatureHeader} 包含签名相关信息的对象
     */
    public static SignatureHeader getRequestHeader(HttpServletRequest request) {
        // 获取通知签名
        String signature = request.getHeader("Wechatpay-Signature");
        String nonce = request.getHeader("Wechatpay-Nonce");
        String serial = request.getHeader("Wechatpay-Serial");
        String timestamp = request.getHeader("Wechatpay-Timestamp");
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setSignature(signature);
        signatureHeader.setNonce(nonce);
        signatureHeader.setSerial(serial);
        signatureHeader.setTimeStamp(timestamp);
        return signatureHeader;
    }
}
