package com.exception.qms.web.vo.article;

import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.home.AnswerResponseVO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/26
 * @time 上午11:12
 * @discription
 **/
@Data
public class ArticleDetailResponseVO implements Serializable {
    private Long id;

    private LocalDateTime createTime;
    private String createDateStr;
    private String createTimeStr;

    private Long createUserId;
    private String createUserAvatar;
    private String createUserName;

    private Integer voteUp;

    private String title;

    /**
     * 题图
     */
    private String titleImage;

    /**
     * meta seo 字段
     */
    private String seoDescription;
    private String seoKeywords;

    private String contentHtml;

    private Integer viewNum;

    private List<TagResponseVO> tags;



}
