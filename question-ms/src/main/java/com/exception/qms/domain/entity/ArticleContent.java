package com.exception.qms.domain.entity;

import lombok.Data;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Data
public class ArticleContent {
    private Long id;

    private Long articleId;

    private Boolean isDeleted;

    private String content;
}