package com.caiths.caiapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日签到表实体类。
 * <p>
 * 该类映射到数据库中的 `daily_check_in` 表，用于记录用户的每日签到信息，包括签到人、描述、增加的积分以及时间戳等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "daily_check_in")
@Data
public class DailyCheckIn implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 签到人的用户ID，用于关联用户表中的用户。
     */
    private Long userId;

    /**
     * 签到描述，记录签到的具体内容或备注。
     */
    private String description;

    /**
     * 签到增加的积分数量，用于激励用户持续签到。
     */
    private Integer addPoints;

    /**
     * 签到记录的创建时间，自动生成。
     */
    private Date createTime;

    /**
     * 签到记录的更新时间，自动更新。
     */
    private Date updateTime;
}
