package com.exception.qms.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Data
public class HealthArticle {
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String title;

    private String cover;

    private Integer dateTime;

    private String author;

    private String wxh;

    private Integer status;

    private Boolean isDeleted;

}