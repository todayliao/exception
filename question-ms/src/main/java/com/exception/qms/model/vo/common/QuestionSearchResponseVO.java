package com.exception.qms.model.vo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/25
 * @time 下午3:38
 * @discription
 **/
@Data
public class QuestionSearchResponseVO implements Serializable {
    private Long questionId;
    private String titleCn;
    private String descriptionCn;
    private List<TagResponseVO> tags;
    private Long createUserId;
    private String createUserName;
    private String createTime;

}
