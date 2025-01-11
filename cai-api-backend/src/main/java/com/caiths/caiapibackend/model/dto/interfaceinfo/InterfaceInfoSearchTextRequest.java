package com.caiths.caiapibackend.model.dto.interfaceinfo;

import com.caiths.caiapibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 界面信息搜索文本请求数据传输对象。
 * <p>
 * 该类用于封装根据文本搜索界面信息的请求参数，包括分页信息和搜索文本。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoSearchTextRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = -6337349622479990038L;

    /**
     * 搜索文本，用于模糊查询界面信息。
     */
    private String searchText;
}
