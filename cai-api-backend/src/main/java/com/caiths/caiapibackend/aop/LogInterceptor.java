package com.caiths.caiapibackend.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 请求响应日志的 AOP 拦截器。
 * <p>
 * 该切面会拦截 Controller 层的方法调用，记录请求的路径、IP 地址、参数以及响应时间，方便开发人员调试和排查问题。
 * </p>
 *
 * <p>日志格式：</p>
 * <ul>
 *     <li><b>Request Start:</b> 包含请求的唯一 ID、路径、IP 地址及参数。</li>
 *     <li><b>Request End:</b> 包含请求的唯一 ID 和耗时（毫秒）。</li>
 * </ul>
 *
 * <p>适用范围：</p>
 * <ul>
 *     <li>拦截所有位于 `com.caiths.caiapibackend.controller` 包中的方法。</li>
 * </ul>
 *
 * 使用示例：
 * <pre>{@code
 * @RestController
 * public class TestController {
 *     @GetMapping("/test")
 *     public String testMethod(String param) {
 *         return "success";
 *     }
 * }
 * }</pre>
 * <p>请求和响应日志将自动记录，无需额外配置。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Aspect
@Component
@Slf4j
public class LogInterceptor {

    /**
     * 环绕通知，用于记录请求和响应日志。
     * <p>
     * 拦截 `com.caiths.caiapibackend.controller` 包下的所有方法，记录请求的唯一 ID、路径、IP 地址、参数，以及执行时间。
     * </p>
     *
     * @param point 切点，表示被拦截的方法
     * @return 被拦截方法的返回值
     * @throws Throwable 如果目标方法抛出异常，则抛出该异常
     */
    @Around("execution(* com.caiths.caiapibackend.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 计时器开始计时
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 获取当前请求的 HttpServletRequest 对象
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 生成请求的唯一 ID
        String requestId = UUID.randomUUID().toString();
        // 获取请求路径
        String url = httpServletRequest.getRequestURI();
        // 获取请求参数
        Object[] args = point.getArgs();
        String reqParam = "[" + StringUtils.join(args, ", ") + "]";

        // 输出请求开始的日志
        log.info("Request start, id: {}, path: {}, ip: {}, params: {}", requestId, url,
                httpServletRequest.getRemoteHost(), reqParam);

        // 执行目标方法
        Object result = point.proceed();

        // 计时器停止计时
        stopWatch.stop();
        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        // 输出请求结束的日志
        log.info("Request end, id: {}, cost: {}ms", requestId, totalTimeMillis);

        return result;
    }
}
