package com.caiths.api.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 修改接口
 * 基本类型封装成为对象
 *
 * @author mdo
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}