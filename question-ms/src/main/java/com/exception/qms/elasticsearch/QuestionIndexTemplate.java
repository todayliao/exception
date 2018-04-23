package com.exception.qms.elasticsearch;

import com.exception.qms.domain.entity.Tag;
import com.exception.qms.web.vo.common.TagResponseVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/17
 * @time 下午8:22
 * @discription
 **/
@Data
public class QuestionIndexTemplate {
    private Long questionId;
    private String title;
    private String desc;
    private LocalDateTime createTime;
    private Integer viewNum;
    private Integer voteUp;
    private Integer voteDown;
    private Long createUserId;
    private String createUserName;
    private Integer type;
    private List<TagResponseVO> tags;
}
