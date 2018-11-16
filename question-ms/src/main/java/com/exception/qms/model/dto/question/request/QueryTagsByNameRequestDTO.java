package com.exception.qms.model.dto.question.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午7:59
 * @discription
 **/
@Data
public class QueryTagsByNameRequestDTO implements Serializable {
    @NotBlank
    private String tagName;
}
