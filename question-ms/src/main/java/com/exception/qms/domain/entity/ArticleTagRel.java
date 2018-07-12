package com.exception.qms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Data
public class ArticleTagRel {
    private Long id;

    private Long articleId;

    private Long tagId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}