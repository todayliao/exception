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
public class AnswerVoteUserRel {
    private Long id;

    private Long answerId;

    private Long voteUserId;

    private Integer voteOperationType;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}