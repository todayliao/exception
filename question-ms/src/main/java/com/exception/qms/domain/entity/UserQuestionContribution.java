package com.exception.qms.domain.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Data
public class UserQuestionContribution {
    private Long id;

    private Long userId;

    private Long questionId;

    private Integer type;

    private LocalDateTime createTime;

    private Boolean isDeleted;

}