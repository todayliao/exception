package com.exception.qms.model.vo.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/25
 * @time 下午3:38
 * @discription
 **/
@Data
public class TagResponseVO implements Serializable {
    private Long tagId;
    private String tagName;
    private String descriptionCn;
}
