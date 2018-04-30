package com.exception.qms.web.dto.question.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/28
 * @time 下午5:07
 * @discription
 **/
@Data
public class ChangeQuestionVoteUpRequestDTO implements Serializable {
    @NotNull
    private Long questionId;
}
