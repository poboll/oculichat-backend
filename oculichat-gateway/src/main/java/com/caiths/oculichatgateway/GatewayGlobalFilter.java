package com.caiths.oculichatgateway;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.caiths.oculichatcommon.common.ErrorCode;
import com.caiths.oculichatcommon.model.dto.RequestParamsField;
import com.caiths.oculichatcommon.model.emums.InterfaceStatusEnum;
import com.caiths.oculichatcommon.model.entity.InterfaceInfo;
import com.caiths.oculichatcommon.model.vo.UserVO;
import com.caiths.oculichatcommon.service.inner.InnerInterfaceInfoService;
import com.caiths.oculichatcommon.service.inner.InnerUserInterfaceInvokeService;
import com.caiths.oculichatcommon.service.inner.InnerUserService;
import com.caiths.oculichatgateway.exception.BusinessException;
import com.caiths.oculichatgateway.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bouncycastle.util.Strings;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.caiths.oculichatcommon.model.emums.UserAccountStatusEnum.BAN;
import static com.caiths.oculichatgateway.CacheBodyGatewayFilter.CACHE_REQUEST_BODY_OBJECT_KEY;
import static com.caiths.oculichatgateway.utils.NetUtils.getIp;
import static com.caiths.caiapisdk.utils.SignUtils.getSign;

/**
 * 全局网关过滤器
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Component
@Slf4j
public class GatewayGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 请求白名单
     */
    private final static List<String> WHITE_HOST_LIST = Arrays.asList("127.0.0.1", "101.43.61.87");
    /**
     * 五分钟过期时间
     */
    private static final long FIVE_MINUTES = 5L * 60;
    @Resource
    private RedissonLockUtil redissonLockUtil;
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerUserInterfaceInvokeService interfaceInvokeService;
    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 进入过滤器的初始日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("=== [GatewayGlobalFilter] 开始执行全局过滤 ===");
        log.info("请求唯一ID：{}", request.getId());
        log.info("请求方法：{}", request.getMethod());
        log.info("请求路径：{}", request.getPath());
        log.info("本地地址：{}", request.getLocalAddress());
        log.info("请求远程地址：{}", request.getRemoteAddress());
        log.info("接口请求IP：{}", getIp(request));
        log.info("完整URL：{}", request.getURI());

        // 2. 继续执行参数校验逻辑
        return verifyParameters(exchange, chain);
    }

    /**
     * 验证参数
     *
     * @param exchange 交换
     * @param chain    链条
     * @return {@link Mono}<{@link Void}>
     */
    private Mono<Void> verifyParameters(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // ============== 1. 请求白名单（如需启用可取消注释） ==============
        // if (!WHITE_HOST_LIST.contains(getIp(request))) {
        //     log.warn("请求IP不在白名单内，IP = {}", getIp(request));
        //     throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        // }

        HttpHeaders headers = request.getHeaders();
        String body = headers.getFirst("body");
        String accessKey = headers.getFirst("accessKey");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");

        // 2.1. 记录请求头参数
        log.info("请求头参数：body={}, accessKey={}, timestamp={}, sign={}",
                body, accessKey, timestamp, sign);

        // 2.2. 请求头中参数必须完整
        if (StringUtils.isAnyBlank(body, sign, accessKey, timestamp)) {
            log.error("请求头中缺少必要参数。body={}, sign={}, accessKey={}, timestamp={}", body, sign, accessKey, timestamp);
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "请求头参数不完整");
        }

        // 2.3. 防重发 - 校验时间戳是否在五分钟内
        long currentTime = System.currentTimeMillis() / 1000;
        assert timestamp != null;
        long reqTime = Long.parseLong(timestamp);
        if (currentTime - reqTime >= FIVE_MINUTES) {
            log.warn("请求时间戳已过期，当前时间 = {}, 请求头中的时间戳 = {}", currentTime, reqTime);
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "会话已过期, 请重试！");
        }

        try {
            // 2.4. 获取用户信息
            UserVO user = innerUserService.getInvokeUserByAccessKey(accessKey);
            if (user == null) {
                log.error("根据 accessKey={} 未查询到对应的用户", accessKey);
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请正确配置接口凭证");
            }
            log.info("查询到的用户信息：userId={}, userAccount={}, status={}, balance={}",
                    user.getId(), user.getUserAccount(), user.getStatus(), user.getBalance());

            // 2.5. 校验accessKey是否匹配
            if (!user.getAccessKey().equals(accessKey)) {
                log.error("accessKey校验失败，用户 accessKey 与 请求头中的 accessKey 不匹配");
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "请先获取请求密钥");
            }

            // 2.6. 检查用户状态
            if (user.getStatus().equals(BAN.getValue())) {
                log.error("用户已封禁，userAccount={}", user.getUserAccount());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该账号已封禁");
            }

            // 2.7. 校验签名
            String serverSign = getSign(body, user.getSecretKey());
            if (!serverSign.equals(sign)) {
                log.error("签名校验失败，服务端签名={}, 前端签名={}", serverSign, sign);
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "非法请求");
            }

            // 2.8. 校验余额
            if (user.getBalance() <= 0) {
                log.error("用户余额不足，userAccount={}", user.getUserAccount());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "余额不足，请先充值。");
            }

            // 2.9. 获取接口信息
            String method = Objects.requireNonNull(request.getMethod()).toString();
            String uri = request.getURI().toString().trim();
            log.info("请求的接口 URI={}, Method={}", uri, method);
            if (StringUtils.isAnyBlank(uri, method)) {
                log.error("URI或Method为空，无法进行匹配");
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }

            // 2.10. 根据uri和method定位到具体的接口
            InterfaceInfo interfaceInfo = interfaceInfoService.getInterfaceInfo(uri, method);
            if (interfaceInfo == null) {
                log.error("接口不存在, URI={}, Method={}", uri, method);
                throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
            }

            // 2.11. 检查接口状态
            if (interfaceInfo.getStatus() == InterfaceStatusEnum.AUDITING.getValue()) {
                log.warn("接口审核中，不可用, interfaceId={}", interfaceInfo.getId());
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口审核中");
            }
            if (interfaceInfo.getStatus() == InterfaceStatusEnum.OFFLINE.getValue()) {
                log.warn("接口已下线, interfaceId={}", interfaceInfo.getId());
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未开启");
            }

            // 2.12. 校验请求参数是否完整
            String requestParams = interfaceInfo.getRequestParams();
            List<RequestParamsField> requiredList = new Gson()
                    .fromJson(requestParams, new TypeToken<List<RequestParamsField>>() {}.getType());
            MultiValueMap<String, String> queryParams = request.getQueryParams();

            if ("POST".equals(method)) {
                Object cacheBody = exchange.getAttribute(CACHE_REQUEST_BODY_OBJECT_KEY);
                String requestBody = getPostRequestBody((Flux<DataBuffer>) cacheBody);
                log.info("POST请求体：{}", requestBody);

                // 2.12.1. 根据接口需求校验必填参数
                if (StringUtils.isNotBlank(requestParams)) {
                    Map<String, Object> requestBodyMap = new Gson()
                            .fromJson(requestBody, new TypeToken<HashMap<String, Object>>() {}.getType());
                    for (RequestParamsField field : requiredList) {
                        if ("是".equals(field.getRequired())) {
                            if (!requestBodyMap.containsKey(field.getFieldName()) ||
                                    StringUtils.isBlank(String.valueOf(requestBodyMap.get(field.getFieldName())))) {
                                log.error("参数校验失败, POST必填字段={} 缺失或为空。", field.getFieldName());
                                throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,
                                        "请求参数有误，" + field.getFieldName() + "为必选项，详细参数请参考API文档：https://api-docs.caiths.com/");
                            }
                        }
                    }
                }
            } else if ("GET".equals(method)) {
                log.info("GET请求参数：{}", queryParams);
                // 2.12.2. 根据接口需求校验必填参数
                if (StringUtils.isNotBlank(requestParams)) {
                    for (RequestParamsField field : requiredList) {
                        if ("是".equals(field.getRequired())) {
                            if (!queryParams.containsKey(field.getFieldName()) ||
                                    StringUtils.isBlank(queryParams.getFirst(field.getFieldName()))) {
                                log.error("参数校验失败, GET必填字段={} 缺失或为空。", field.getFieldName());
                                throw new BusinessException(ErrorCode.FORBIDDEN_ERROR,
                                        "请求参数有误，" + field.getFieldName() + "为必选项，详细参数请参考API文档：https://api-docs.caiths.com/");
                            }
                        }
                    }
                }
            }

            log.info("=== [GatewayGlobalFilter] 参数校验通过，开始处理响应 ===");
            // 3. 执行响应处理
            return handleResponse(exchange, chain, user, interfaceInfo);

        } catch (Exception e) {
            log.error("verifyParameters 出现异常: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, e.getMessage());
        }
    }

    /**
     * 获取POST请求体
     *
     * @param body Flux<DataBuffer>
     * @return {@link String}
     */
    private String getPostRequestBody(Flux<DataBuffer> body) {
        // 由于body是流的形式，这里需要转化处理
        AtomicReference<String> getBody = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            getBody.set(Strings.fromUTF8ByteArray(bytes));
        });
        return getBody.get();
    }

    /**
     * 处理响应
     *
     * @param exchange      交换
     * @param chain         链条
     * @param user          用户信息
     * @param interfaceInfo 接口信息
     * @return {@link Mono}<{@link Void}>
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,
                                     UserVO user, InterfaceInfo interfaceInfo) {
        ServerHttpResponse originalResponse = exchange.getResponse();
        // 缓存数据的工厂
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        // 拿到响应码
        HttpStatus statusCode = originalResponse.getStatusCode();
        log.info("=== [handleResponse] 响应状态码：{} ===", statusCode);

        // 如果是200，再做增强处理，否则直接返回
        if (statusCode == HttpStatus.OK) {
            // 装饰，增强能力
            ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                // 当调用完转发的接口后才会执行
                @Override
                public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                    if (body instanceof Flux) {
                        Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                        return super.writeWith(
                                fluxBody.map(dataBuffer -> {
                                    // 扣减用户可调用次数或余额
                                    String lockKey = ("gateway_" + user.getUserAccount()).intern();
                                    redissonLockUtil.redissonDistributedLocks(lockKey, () -> {
                                        boolean invokeResult = interfaceInvokeService.invoke(
                                                interfaceInfo.getId(), user.getId(), interfaceInfo.getReduceScore());
                                        if (!invokeResult) {
                                            log.error("接口调用扣费失败, userId={}, interfaceId={}",
                                                    user.getId(), interfaceInfo.getId());
                                            throw new BusinessException(ErrorCode.OPERATION_ERROR, "接口调用失败");
                                        }
                                    }, "接口调用失败");

                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    // 释放buffer
                                    DataBufferUtils.release(dataBuffer);
                                    String responseData = new String(content, StandardCharsets.UTF_8);
                                    // 打印日志
                                    log.info("响应结果：{}", responseData);

                                    // 返回新的DataBuffer给下游
                                    return bufferFactory.wrap(content);
                                })
                        );
                    } else {
                        // 如果不是正常流，则打印警告并直接返回
                        log.warn("[handleResponse] 响应体类型异常，不是Flux类型。statusCode={}", getStatusCode());
                    }
                    return super.writeWith(body);
                }
            };
            // 设置 response 对象为装饰过的
            return chain.filter(exchange.mutate().response(decoratedResponse).build());
        }
        // 非200状态，直接返回，不做额外处理
        log.warn("[handleResponse] 非200状态，降级或异常处理逻辑。statusCode={}", statusCode);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 设置优先级，越小优先级越高
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
