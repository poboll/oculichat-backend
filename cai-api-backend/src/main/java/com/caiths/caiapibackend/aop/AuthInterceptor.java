package com.caiths.caiapibackend.aop;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.caiths.caiapibackend.annotation.AuthCheck;
import com.caiths.caiapibackend.common.ErrorCode;
import com.caiths.caiapibackend.exception.BusinessException;
import com.caiths.caiapibackend.model.vo.UserVO;
import com.caiths.caiapibackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限校验的切面类，通过 AOP 拦截标注了 {@link AuthCheck} 注解的方法，进行权限验证。
 * <p>
 * 此切面会在目标方法执行前进行以下校验：
 * <ul>
 *     <li>检查当前用户是否拥有 {@link AuthCheck#anyRole()} 中任意一个角色。</li>
 *     <li>如果设置了 {@link AuthCheck#mustRole()}，则检查当前用户是否拥有该角色。</li>
 * </ul>
 * 如果校验不通过，则抛出 {@link BusinessException} 异常。
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * @AuthCheck(anyRole = {"ADMIN", "USER"}, mustRole = "ADMIN")
 * public void someProtectedMethod() {
 *     // 方法实现
 * }
 * }</pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Aspect
@Component
public class AuthInterceptor {

    /**
     * 用户服务，用于获取当前登录用户的信息。
     */
    @Resource
    private UserService userService;

    /**
     * 环绕通知，用于拦截并处理带有 {@link AuthCheck} 注解的方法。
     * <p>
     * 该方法会在目标方法执行前进行权限校验，校验通过后继续执行目标方法，否则抛出异常。
     * </p>
     *
     * @param joinPoint 连接点，表示被拦截的方法
     * @param authCheck 权限校验注解，包含所需的角色信息
     * @return 目标方法的返回结果
     * @throws Throwable 如果目标方法执行过程中抛出异常，或权限校验不通过时抛出 {@link BusinessException}
     */
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        // 获取注解中定义的任意角色，过滤掉空字符串
        List<String> anyRole = Arrays.stream(authCheck.anyRole())
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());

        // 获取注解中定义的必须角色
        String mustRole = authCheck.mustRole();

        // 获取当前请求的 HttpServletRequest 对象
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 获取当前登录用户的信息
        UserVO user = userService.getLoginUser(request);

        // 如果定义了任意角色，检查用户是否拥有其中任意一个
        if (CollectionUtils.isNotEmpty(anyRole)) {
            String userRole = user.getUserRole();
            if (!anyRole.contains(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户没有足够的权限访问此资源。");
            }
        }

        // 如果定义了必须角色，检查用户是否拥有该角色
        if (StringUtils.isNotBlank(mustRole)) {
            String userRole = user.getUserRole();
            if (!mustRole.equals(userRole)) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户没有足够的权限访问此资源。");
            }
        }

        // 权限校验通过，继续执行目标方法
        return joinPoint.proceed();
    }
}
