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
    // 左眼图像的 Base64 编码
    private String left_image_base64;

    // 右眼图像的 Base64 编码
    private String right_image_base64;

    // 患者年龄
    private int age;

    // 患者性别（通常 0 表示女性，1 表示男性）
    private int sex;

    // 是否使用测试时增强 (Test Time Augmentation)
    private boolean use_tta;

    // TTA 次数
    private int tta_times;

    // 目标图像尺寸
    private int target_size;

    // 患者年龄别名（兼容其他系统）
    private Object patient_age;

    // 是否返回详细的可解释性和可视化结果
    private boolean include_explanations = true;

    // 是否生成增强视图
    private boolean generate_enhanced_views = true;

    // 是否生成特征重要性数据
    private boolean include_feature_importance = true;

    public Object getPatient_age() {
        return patient_age;
    }

    public Object getPatient_gender() {
        return patient_age;
    }

    public void setPatient_age(Object patientAge) {
        this.patient_age = patientAge;
    }

    /**
     * 获取实际使用的年龄值
     * 如果 patient_age 存在且不为 null，优先使用 patient_age，否则使用 age
     */
    public int getEffectiveAge() {
        if (patient_age != null) {
            if (patient_age instanceof Number) {
                return ((Number) patient_age).intValue();
            } else if (patient_age instanceof String) {
                try {
                    return Integer.parseInt((String) patient_age);
                } catch (NumberFormatException e) {
                    // 解析失败，回退到使用 age
                }
            }
        }
        return age;
    }
}
