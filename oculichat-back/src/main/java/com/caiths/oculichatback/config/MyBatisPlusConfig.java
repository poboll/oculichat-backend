package com.caiths.oculichatback.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置类，用于集成和配置 MyBatis Plus 框架。
 * <p>
 * 该配置类主要负责以下功能：
 * <ul>
 *   <li>扫描指定包路径下的 Mapper 接口，便于 MyBatis Plus 自动识别和实现。</li>
 *   <li>配置 MyBatis Plus 的拦截器，包括分页插件，以增强 MyBatis 的功能。</li>
 * </ul>
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>自动扫描指定包路径下的 Mapper 接口。</li>
 *   <li>配置 MyBatis Plus 拦截器，支持分页查询等高级功能。</li>
 * </ul>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * mybatis-plus:
 *   mapper-locations: classpath:/mapper/*.xml
 *   type-aliases-package: com.caiths.oculichatback.entity
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
@MapperScan("com.caiths.oculichatback.mapper")
public class MyBatisPlusConfig {

    /**
     * 配置 MyBatis Plus 拦截器。
     * <p>
     * 该方法创建并配置了 {@link MybatisPlusInterceptor} Bean，主要包括分页插件，
     * 以支持 MySQL 数据库的分页查询功能。通过添加 {@link PaginationInnerInterceptor}，
     * 可以自动处理分页参数，并优化 SQL 查询性能。
     * </p>
     *
     * <p><strong>功能描述：</strong></p>
     * <ul>
     *   <li>创建 MyBatis Plus 拦截器实例。</li>
     *   <li>添加分页插件，支持 MySQL 数据库类型。</li>
     *   <li>扩展性：可以在此方法中添加更多的 MyBatis Plus 拦截器以满足项目需求。</li>
     * </ul>
     *
     * @return 配置完成的 {@link MybatisPlusInterceptor} 实例
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加 MySQL 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
