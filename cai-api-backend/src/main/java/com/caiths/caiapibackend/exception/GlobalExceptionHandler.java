package com.caiths.caiapibackend.exception;

import com.caiths.caiapibackend.common.BaseResponse;
import com.caiths.caiapibackend.common.ErrorCode;
import com.caiths.caiapibackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器，负责捕捉和处理应用程序中未被捕获的异常。
 * <p>
 * 该类使用 {@code @RestControllerAdvice} 注解，能够对所有控制器中的异常进行集中处理。
 * 提供针对自定义的 {@link BusinessException} 和一般的 {@link RuntimeException} 的处理方法。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常 {@link BusinessException}。
     * <p>
     * 当控制器方法抛出 {@code BusinessException} 时，此方法将被调用。
     * 它会记录错误日志并返回一个包含错误代码和错误信息的 {@link BaseResponse} 对象。
     * </p>
     *
     * @param e 抛出的 {@code BusinessException} 异常对象
     * @return 包含错误代码和错误信息的 {@link BaseResponse} 对象
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理运行时异常 {@link RuntimeException}。
     * <p>
     * 当控制器方法抛出 {@code RuntimeException} 或其子类时，此方法将被调用。
     * 它会记录错误日志并返回一个包含系统错误代码和错误信息的 {@link BaseResponse} 对象。
     * </p>
     *
     * @param e 抛出的 {@code RuntimeException} 异常对象
     * @return 包含系统错误代码和错误信息的 {@link BaseResponse} 对象
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage());
    }
}
