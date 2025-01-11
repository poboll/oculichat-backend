package com.caiths.caiapibackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caiths.caiapibackend.model.entity.DailyCheckIn;

/**
 * 每日签到映射器接口，继承自 MyBatis-Plus 的 BaseMapper。
 * <p>
 * 提供对 {@link DailyCheckIn} 实体的基本数据库操作方法，如插入、更新、删除和查询等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface DailyCheckInMapper extends BaseMapper<DailyCheckIn> {

}
