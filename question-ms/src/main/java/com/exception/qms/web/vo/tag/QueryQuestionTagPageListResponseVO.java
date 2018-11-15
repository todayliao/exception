package com.exception.qms.web.vo.tag;

import lombok.Data;
import site.exception.common.PageQueryResponse;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class QueryQuestionTagPageListResponseVO<T> extends PageQueryResponse<T> implements Serializable {
    private Long id;
    private String name;
    private String descriptionCn;
    private Integer questionCount;
}
