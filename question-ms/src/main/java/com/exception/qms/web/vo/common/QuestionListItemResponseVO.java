package com.exception.qms.web.vo.common;

import com.exception.qms.web.vo.common.TagResponseVO;
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
public class QuestionListItemResponseVO implements Serializable {
    private Long id;
    private String titleCn;
    private Long createUserId;
    private String createUserName;
    private String createUserAvatar;
    /**
     * 时间差
     */
    private String beforeTimeStr;
    private List<TagResponseVO> tags;
}
