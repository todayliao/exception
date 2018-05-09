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
public class AnswerEditHistory {
    private Long id;

    private Long answerId;

    private Long createUserId;

    private LocalDateTime createTime;

    private Boolean isDeleted;

    private String descriptionCn;
}