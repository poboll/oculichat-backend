package com.caiths.oculichatback.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiths.oculichatback.mapper.DailyCheckInMapper;
import com.caiths.oculichatback.model.entity.DailyCheckIn;
import com.caiths.oculichatback.service.DailyCheckInService;
import org.springframework.stereotype.Service;

/**
 * DailyCheckInServiceImpl 是每日签到服务的实现类。
 * <p>
 * 该类负责处理每日用户签到相关的业务逻辑，
 * 包括用户签到记录的创建、查询等操作。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Service
public class DailyCheckInServiceImpl extends ServiceImpl<DailyCheckInMapper, DailyCheckIn>
        implements DailyCheckInService {

    // 该类继承了 ServiceImpl 并实现了 DailyCheckInService 接口，
    // 可以在此处添加更多的业务逻辑方法，如用户签到、查询签到记录等。

}
