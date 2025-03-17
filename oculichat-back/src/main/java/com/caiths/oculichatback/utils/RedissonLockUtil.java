package com.caiths.oculichatback.utils;

import com.caiths.oculichatback.common.ErrorCode;
import com.caiths.oculichatback.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redisson 分布式锁工具类。
 * <p>
 * 提供多种方法用于获取和释放 Redisson 分布式锁，以确保在分布式环境中的同步操作。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Slf4j
@Component
public class RedissonLockUtil {

    @Resource
    public RedissonClient redissonClient;

    /**
     * 使用 Redisson 分布式锁执行供应商操作。
     *
     * @param lockName     锁名称
     * @param supplier     供应商，提供要执行的操作
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     * @param <T>          返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.error("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，并记录错误日志。
     *
     * @param lockName      锁名称
     * @param supplier      供应商，提供要执行的操作
     * @param errorLogTitle 错误日志标题
     * @param errorCode     错误代码
     * @param errorMessage  错误消息
     * @param <T>           返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, String errorLogTitle, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (Exception e) {
            log.error(errorLogTitle, e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.error("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，并执行日志消息。
     *
     * @param lockName     锁名称
     * @param supplier     供应商，提供要执行的操作
     * @param logMessage   日志消息
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     * @param <T>          返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, Runnable logMessage, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (Exception e) {
            logMessage.run();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.error("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，指定等待时间和租赁时间。
     *
     * @param waitTime     等待时间
     * @param leaseTime    租赁时间
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param supplier     供应商，提供要执行的操作
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     * @param args         额外参数
     * @param <T>          返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(long waitTime, long leaseTime, TimeUnit unit, String lockName, Supplier<T> supplier, ErrorCode errorCode, String errorMessage, Object... args) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(waitTime, leaseTime, unit)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，指定时间和时间单位。
     *
     * @param time         时间
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param supplier     供应商，提供要执行的操作
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     * @param <T>          返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(long time, TimeUnit unit, String lockName, Supplier<T> supplier, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(time, unit)) {
                return supplier.get();
            }
            throw new BusinessException(errorCode.getCode(), errorMessage);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作。
     *
     * @param lockName  锁名称
     * @param supplier  供应商，提供要执行的操作
     * @param errorCode 错误代码
     * @param <T>       返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, ErrorCode errorCode) {
        return redissonDistributedLocks(lockName, supplier, errorCode, errorCode.getMessage());
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，并执行日志消息。
     *
     * @param lockName    锁名称
     * @param supplier    供应商，提供要执行的操作
     * @param logMessage  日志消息
     * @param errorCode   错误代码
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, Runnable logMessage, ErrorCode errorCode) {
        return redissonDistributedLocks(lockName, supplier, logMessage, errorCode, errorCode.getMessage());
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作。
     *
     * @param lockName     锁名称
     * @param supplier     供应商，提供要执行的操作
     * @param errorMessage 错误消息
     * @param <T>          返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, String errorMessage) {
        return redissonDistributedLocks(lockName, supplier, ErrorCode.OPERATION_ERROR, errorMessage);
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作。
     *
     * @param lockName  锁名称
     * @param supplier  供应商，提供要执行的操作
     * @param <T>       返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier) {
        return redissonDistributedLocks(lockName, supplier, ErrorCode.OPERATION_ERROR);
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，并记录错误日志标题。
     *
     * @param lockName      锁名称
     * @param errorLogTitle 错误日志标题
     * @param supplier      供应商，提供要执行的操作
     * @param <T>           返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, String errorLogTitle, Supplier<T> supplier) {
        return redissonDistributedLocks(lockName, supplier, errorLogTitle, ErrorCode.OPERATION_ERROR, ErrorCode.OPERATION_ERROR.getMessage());
    }

    /**
     * 使用 Redisson 分布式锁执行供应商操作，并执行日志消息。
     *
     * @param lockName    锁名称
     * @param logMessage  日志消息
     * @param supplier    供应商，提供要执行的操作
     * @param <T>         返回类型
     * @return 供应商执行后的结果
     * @throws BusinessException 如果获取锁失败或执行供应商操作时发生异常
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, Runnable logMessage) {
        return redissonDistributedLocks(lockName, supplier, logMessage, ErrorCode.OPERATION_ERROR);
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, String errorLogTitle, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (Exception e) {
            log.error(errorLogTitle, e.getMessage());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, Runnable logMessage, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (Exception e) {
            logMessage.run();
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName  锁名称
     * @param runnable  可运行
     * @param errorCode 错误代码
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, ErrorCode errorCode) {
        redissonDistributedLocks(lockName, runnable, errorCode, errorCode.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, String errorMessage) {
        redissonDistributedLocks(lockName, runnable, ErrorCode.OPERATION_ERROR, errorMessage);
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param runnable 可运行
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable) {
        redissonDistributedLocks(lockName, runnable, ErrorCode.OPERATION_ERROR, ErrorCode.OPERATION_ERROR.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param runnable 可运行
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, Runnable logMessage) {
        redissonDistributedLocks(lockName, runnable, logMessage, ErrorCode.OPERATION_ERROR, ErrorCode.OPERATION_ERROR.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param runnable 可运行
     */
    public void redissonDistributedLocks(String lockName, String errorLogTitle, Runnable runnable) {
        redissonDistributedLocks(lockName, runnable, errorLogTitle, ErrorCode.OPERATION_ERROR, ErrorCode.OPERATION_ERROR.getMessage());
    }

    /**
     * redisson分布式锁 可自定义 waitTime 、leaseTime、TimeUnit
     *
     * @param waitTime     等待时间
     * @param leaseTime    租赁时间
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(long waitTime, long leaseTime, TimeUnit unit, String lockName, Runnable runnable, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(waitTime, leaseTime, unit)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁 可自定义 time 、unit
     *
     * @param time         时间
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorCode    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(long time, TimeUnit unit, String lockName, Runnable runnable, ErrorCode errorCode, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(time, unit)) {
                runnable.run();
            } else {
                throw new BusinessException(errorCode.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }
}