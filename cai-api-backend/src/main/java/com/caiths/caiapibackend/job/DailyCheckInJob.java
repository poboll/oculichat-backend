package com.caiths.caiapibackend.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.caiths.caiapibackend.model.entity.DailyCheckIn;
import com.caiths.caiapibackend.service.DailyCheckInService;
import com.caiths.caiapibackend.utils.RedissonLockUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 签到任务，负责定时清理签到记录。
 * <p>
 * 该类使用 {@code @Component} 注解，表示它是一个 Spring 组件。
 * 通过 {@code @Scheduled} 注解配置定时任务，每天午夜执行一次，以批量清除过期的签到记录。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Component
public class DailyCheckInJob {

    @Resource
    private DailyCheckInService dailyCheckInService;

    @Resource
    private RedissonLockUtil redissonLockUtil;

    /**
     * 每天午夜执行的任务，用于批量清除签到列表。
     * <p>
     * 该方法使用分布式锁确保在分布式环境中任务的互斥执行。任务分批次删除签到记录，每批次删除的数量由 {@code batchSize} 决定，
     * 直到所有签到记录被清除。
     * </p>
     *
     * <p>计划表达式: {@code cron = "0 0 0 * * *"}，表示每天凌晨12点执行一次。</p>
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void clearCheckInList() {
        redissonLockUtil.redissonDistributedLocks("clearCheckInList", () -> {
            // 每批删除的数据量
            int batchSize = 1000;
            // 是否还有数据需要删除
            boolean hasMoreData = true;

            while (hasMoreData) {
                // 分批查询数据
                List<DailyCheckIn> dataList = dailyCheckInService.list(
                        new QueryWrapper<DailyCheckIn>().last("LIMIT " + batchSize)
                );

                if (dataList.isEmpty()) {
                    // 没有数据了，退出循环
                    hasMoreData = false;
                } else {
                    // 批量删除数据
                    List<Long> idsToDelete = dataList.stream()
                            .map(DailyCheckIn::getId)
                            .collect(Collectors.toList());
                    dailyCheckInService.removeByIds(idsToDelete);
                }
            }
        });
    }
}
