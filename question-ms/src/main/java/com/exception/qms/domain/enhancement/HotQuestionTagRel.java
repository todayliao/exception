package com.exception.qms.domain.enhancement;

import com.exception.qms.domain.entity.QuestionTagRel;
import lombok.Data;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/26
 * @time 下午1:50
 * @discription
 **/
@Data
public class HotQuestionTagRel extends QuestionTagRel {
    private Integer questionCount;
}
