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
public class Course {
    private Long id;

    private String title;

    private String titleImage;

    private String summary;

    private Integer type;

    private Long createUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isDeleted;

    private String enTitle;
}