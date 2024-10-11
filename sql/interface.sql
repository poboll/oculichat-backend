-- 接口信息
create table if not exists caiapi.`interface_info`
(
    `id`             bigint                             not null auto_increment comment '主键' primary key,
    `name`           varchar(256)                       not null comment '名称',
    `description`    varchar(256)                       null comment '描述',
    `url`            varchar(512)                       not null comment '接口地址',
    `requestHeader`  text                               null comment '请求头',
    `responseHeader` text                               null comment '响应头',
    `status`         int      default 0                 not null comment '接口状态(0-关闭，1-开启)',
    `method`         varchar(256)                       not null comment '请求类型',
    `userId`         bigint                             not null comment '创建人',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`       tinyint  default 0                 not null comment '是否删除(0-未删，1-已删)'
    ) comment '接口信息';

-- 随机数据
INSERT INTO caiapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`, `createTime`, `updateTime`, `isDelete`)
VALUES
    ('User Service', '用户服务接口', 'https://api.caiths.com/user', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'GET', 1001, NOW(), NOW(), 0),
    ('Order Service', '订单服务接口', 'https://api.caiths.com/order', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'POST', 1002, NOW(), NOW(), 0),
    ('Product Service', '商品服务接口', 'https://api.caiths.com/product', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'PUT', 1003, NOW(), NOW(), 0),
    ('Payment Service', '支付服务接口', 'https://api.caiths.com/payment', 'Authorization: Bearer <token>', 'Content-Type: application/json', 0, 'DELETE', 1004, NOW(), NOW(), 0),
    ('Shipping Service', '物流服务接口', 'https://api.caiths.com/shipping', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'PATCH', 1005, NOW(), NOW(), 0),
    ('Inventory Service', '库存管理接口', 'https://api.caiths.com/inventory', 'Authorization: Bearer <token>', 'Content-Type: application/json', 0, 'GET', 1001, NOW(), NOW(), 0),
    ('Analytics Service', '数据分析接口', 'https://api.caiths.com/analytics', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'POST', 1002, NOW(), NOW(), 0),
    ('Notification Service', '通知服务接口', 'https://api.caiths.com/notification', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'PUT', 1003, NOW(), NOW(), 0),
    ('Logging Service', '日志服务接口', 'https://api.caiths.com/logging', 'Authorization: Bearer <token>', 'Content-Type: application/json', 0, 'DELETE', 1004, NOW(), NOW(), 0),
    ('Feedback Service', '反馈服务接口', 'https://api.caiths.com/feedback', 'Authorization: Bearer <token>', 'Content-Type: application/json', 1, 'PATCH', 1005, NOW(), NOW(), 0);