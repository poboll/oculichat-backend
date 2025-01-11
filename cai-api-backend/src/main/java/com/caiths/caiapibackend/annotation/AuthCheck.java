package com.caiths.caiapibackend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于权限校验。
 * <p>
 * 可用于校验用户是否具有指定的角色或权限。支持以下两种校验方式：
 * <ul>
 *   <li>用户拥有任意一个指定角色（{@code anyRole}）。</li>
 *   <li>用户必须拥有指定的角色（{@code mustRole}）。</li>
 * </ul>
 * </p>
 *
 * <p>适用场景：</p>
 * <ul>
 *   <li>控制器方法级别的权限校验。</li>
 *   <li>细粒度的功能权限控制。</li>
 * </ul>
 *
 * @author poboll
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {

    /**
     * 定义用户需要满足的任意角色。
     * <p>
     * 当设置此参数时，用户只需满足任意一个指定的角色，即通过校验。
     * </p>
     *
     * @return 需要校验的角色数组（{@link String[]}），默认为空字符串数组。
     */
    String[] anyRole() default "";

    /**
     * 定义用户必须满足的角色。
     * <p>
     * 当设置此参数时，用户必须同时满足此处指定的角色，才能通过校验。
     * </p>
     *
     * @return 需要校验的角色（{@link String}），默认为空字符串。
     */
    String mustRole() default "";

}
