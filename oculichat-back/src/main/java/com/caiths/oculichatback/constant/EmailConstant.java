package com.caiths.oculichatback.constant;

/**
 * 电子邮件相关常量接口，定义了应用程序中电子邮件功能所需的全局常量。
 * <p>
 * 该接口包含了电子邮件内容路径、缓存键、主题、标题等相关常量，用于统一管理和引用。
 * </p>
 *
 * <p><strong>主要常量：</strong></p>
 * <ul>
 *   <li>{@link #EMAIL_HTML_CONTENT_PATH} - 电子邮件 HTML 内容的路径。</li>
 *   <li>{@link #EMAIL_HTML_PAY_SUCCESS_PATH} - 电子邮件支付成功页面的路径。</li>
 *   <li>{@link #CAPTCHA_CACHE_KEY} - 验证码缓存键前缀。</li>
 *   <li>{@link #EMAIL_SUBJECT} - 电子邮件的主题。</li>
 *   <li>{@link #EMAIL_TITLE} - 电子邮件的标题（中文）。</li>
 *   <li>{@link #EMAIL_TITLE_ENGLISH} - 电子邮件的标题（英文）。</li>
 *   <li>{@link #PLATFORM_RESPONSIBLE_PERSON} - 平台负责人名称。</li>
 *   <li>{@link #PLATFORM_ADDRESS} - 平台联系地址。</li>
 *   <li>{@link #PATH_ADDRESS} - 平台地址链接。</li>
 * </ul>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface EmailConstant {

    /**
     * 电子邮件 HTML 内容文件的路径。
     * <p>
     * 该路径位于 {@code resources} 目录下，用于加载电子邮件的 HTML 模板。
     * </p>
     * <p>示例值：{@code "email.html"}</p>
     */
    String EMAIL_HTML_CONTENT_PATH = "email.html";

    /**
     * 电子邮件支付成功页面的 HTML 文件路径。
     * <p>
     * 用于加载支付成功后的电子邮件模板。
     * </p>
     * <p>示例值：{@code "pay.html"}</p>
     */
    String EMAIL_HTML_PAY_SUCCESS_PATH = "pay.html";

    /**
     * 验证码缓存键的前缀。
     * <p>
     * 用于在缓存中存储和检索验证码数据。
     * </p>
     * <p>示例值：{@code "api:captcha:"}</p>
     */
    String CAPTCHA_CACHE_KEY = "api:captcha:";

    /**
     * 电子邮件的主题。
     * <p>
     * 用于发送验证码邮件时的邮件主题。
     * </p>
     * <p>示例值：{@code "验证码邮件"}</p>
     */
    String EMAIL_SUBJECT = "验证码邮件";

    /**
     * 电子邮件的标题（中文）。
     * <p>
     * 用于电子邮件模板中的标题显示。
     * </p>
     * <p>示例值：{@code "Cai-API 接口开放平台"}</p>
     */
    String EMAIL_TITLE = "Cai-API 接口开放平台";

    /**
     * 电子邮件的标题（英文）。
     * <p>
     * 用于电子邮件模板中的英文标题显示。
     * </p>
     * <p>示例值：{@code "Qi-API Open Interface Platform"}</p>
     */
    String EMAIL_TITLE_ENGLISH = "Cai-API Open Interface Platform";

    /**
     * 平台负责人的名称。
     * <p>
     * 表示负责平台运营的团队或个人名称。
     * </p>
     * <p>示例值：{@code "在虎"}</p>
     */
    String PLATFORM_RESPONSIBLE_PERSON = "在虎";

    /**
     * 平台的联系地址。
     * <p>
     * 用于在电子邮件中提供平台的联系链接。
     * </p>
     * <p>示例值：{@code "<a href='https://api.caiths.com/'>请联系我们</a>"}</p>
     */
    String PLATFORM_ADDRESS = "<a href='https://api.caiths.com/'>请联系我们</a>";

    /**
     * 平台的地址链接。
     * <p>
     * 用于在电子邮件或其他地方引用平台的官方网站地址。
     * </p>
     * <p>示例值：{@code "'https://api.caiths.com/'"}</p>
     */
    String PATH_ADDRESS = "'https://api.caiths.com/'";
}
