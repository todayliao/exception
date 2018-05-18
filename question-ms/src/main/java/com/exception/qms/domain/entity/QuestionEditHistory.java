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
public class QuestionEditHistory {
    private Long id;

    private Long questionId;

    private Long createUserId;

    private String titleCn;

    private LocalDateTime createTime;

    private LocalDateTime questionCreateTime;

    private Boolean isDeleted;

    private String descriptionCn;
}