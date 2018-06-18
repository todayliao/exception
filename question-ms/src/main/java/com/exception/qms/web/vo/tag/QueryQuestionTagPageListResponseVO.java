package com.exception.qms.web.vo.tag;

import com.exception.qms.web.vo.common.QuestionListItemResponseVO;
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
public class QueryQuestionTagPageListResponseVO implements Serializable {
    private Long id;
    private String name;
    private String descriptionCn;
    private Integer questionCount;
    private List<QuestionListItemResponseVO> questions;
}
