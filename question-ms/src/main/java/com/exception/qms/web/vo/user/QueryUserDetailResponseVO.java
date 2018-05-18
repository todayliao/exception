package com.exception.qms.web.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class QueryUserDetailResponseVO implements Serializable {
    private Long userId;
    private String userName;
    /**
     * 用户自我介绍
     */
    private String userIntroduction;
    private String userAvatar;

    /**
     * 用户提交的问题总数
     */
    private Integer totalQuestionCount;
    private List<QueryUserDetailQuestionItemResponseVO> questions;

}
