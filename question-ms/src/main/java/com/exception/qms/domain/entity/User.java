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
public class User implements Serializable {
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

    private String introduction;

    private Integer status;

    private String githubUrl;

    private String linkUrl;

    private String location;
}