package com.exception.qms.domain.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Data
public class Tag implements Serializable {
    private Long id;

    private String name;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUserId;

    private Integer questionCount;

    private Boolean isDeleted;

    private String descriptionCn;
}