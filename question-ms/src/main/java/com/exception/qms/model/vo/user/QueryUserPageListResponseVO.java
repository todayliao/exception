package com.exception.qms.model.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class QueryUserPageListResponseVO implements Serializable {
    private Long userId;
    private String userName;
    /**
     * 用户自我介绍
     */
    private String userIntroduction;
    private String userAvatar;
}
