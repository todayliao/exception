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
public class CourseChapter {
    private Long id;

    private Long courseId;

    private String title;

    private Integer chapterNum;

    private Long createUserId;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isDeleted;

    private String enTitle;

}