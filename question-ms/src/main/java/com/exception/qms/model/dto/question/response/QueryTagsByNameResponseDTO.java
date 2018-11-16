package com.exception.qms.model.dto.question.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午7:59
 * @discription
 **/
@Data
public class QueryTagsByNameResponseDTO implements Serializable {
    private Long tagId;
    private String tagName;
}
