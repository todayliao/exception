package com.exception.qms.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Data
public class QuestionTagRel implements Serializable {
    private Long id;

    private Long questionId;

    private Long tagId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}