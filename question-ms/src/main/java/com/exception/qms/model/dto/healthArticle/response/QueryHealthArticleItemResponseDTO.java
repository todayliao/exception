package com.exception.qms.model.dto.healthArticle.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/8/25
 * @time 下午4:21
 * @discription
 **/
@Data
public class QueryHealthArticleItemResponseDTO implements Serializable {
    private Long id;
    private String title;
    private String cover;
    private Integer dateTime;
}
