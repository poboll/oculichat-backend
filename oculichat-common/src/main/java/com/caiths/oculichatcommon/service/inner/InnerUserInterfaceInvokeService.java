package com.caiths.oculichatcommon.service.inner;

/**
 * 用户接口调用服务，负责记录和管理用户对接口的调用。
 *
 * <p>该服务用于跟踪用户对各个接口的调用次数及其状态。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface InnerUserInterfaceInvokeService {

    /**
     * 调用接口并记录调用信息。
     *
     * <p>此方法用于记录用户对指定接口的调用，并根据调用情况减少用户的积分。</p>
     *
     * @param interfaceInfoId 接口信息的唯一标识
     * @param userId          用户的唯一标识
     * @param reduceScore     调用接口后减少的积分数量
     * @return {@code true} 如果调用成功并成功记录，{@code false} 否则
     */
    boolean invoke(Long interfaceInfoId, Long userId, Integer reduceScore);
}
