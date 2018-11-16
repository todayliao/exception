package com.exception.qms.model.vo.article;

import com.exception.qms.model.vo.common.TagResponseVO;
import com.exception.qms.model.vo.home.AnswerResponseVO;
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

    private Long authorId;
    private String authorAvatar;
    private String authorName;
    private String authorIntroduction;

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
