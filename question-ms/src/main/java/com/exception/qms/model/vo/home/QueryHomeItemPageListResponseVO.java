package com.exception.qms.model.vo.home;

import com.exception.qms.model.vo.common.TagResponseVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class QueryHomeItemPageListResponseVO implements Serializable {
    private Long id;
    private String titleCn;
    private Long createUserId;
    private String createUserName;
    private String createUserAvatar;
    private String createUserIntroduction;

    /**
     * 题图
     */
    private String titleImage;

    /**
     * item 内容快照
     */
    private String shortContent;

    /**
     * 类型 0：问题 1：文章
     */
    private Integer type;

    private LocalDateTime createTime;

    /**
     * 用来分页的时间
     */
    private String limitTime;

    /**
     * 时间差
     */
    @Deprecated
    private String beforeTimeStr;
    @Deprecated
    private List<TagResponseVO> tags;
}
