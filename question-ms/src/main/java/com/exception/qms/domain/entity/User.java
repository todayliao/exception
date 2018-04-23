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
public class User {
    private Long id;

    private String name;

    private String password;

    private String email;

    private String phone;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean isDeleted;

    private LocalDateTime lastLoginTime;

    private String avatar;

    private Integer status;
}