package com.exception.qms.web.vo.home;

import com.exception.qms.domain.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class AnswerResponseVO implements Serializable {
    private Long id;

    private LocalDateTime createTime;

    private Long createUserId;

    private Long questionId;

    private Integer voteUp;

    private Integer voteDown;

    private Boolean isAccepted;

    private String descriptionCnHtml;

    private User user;
}
