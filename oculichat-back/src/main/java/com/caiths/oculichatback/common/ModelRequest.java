package com.caiths.oculichatback.common;

import lombok.Data;

/**
 * 请求参数封装类，用于接收前端传入的眼底图像（左右眼 Base64 编码）、年龄、性别、TTA 等参数。
 *
 * 注意：用户上传的是左右眼水平拼接600*300的眼底图像，
 * 前端将左眼和右眼图像分别转换为 Base64 编码，并以 left_image_base64 和 right_image_base64 字段传递。
 */
@Data
public class ModelRequest {
    private String left_image_base64;
    private String right_image_base64;
    private int age;
    private int sex;
    private boolean use_tta;
    private int tta_times;
    private int target_size;
}

