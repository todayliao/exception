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
public class UserArticleContribution {
    private Long id;

    private Long userId;

    private Long articleId;

    private LocalDateTime createTime;

    private Boolean isDeleted;

}