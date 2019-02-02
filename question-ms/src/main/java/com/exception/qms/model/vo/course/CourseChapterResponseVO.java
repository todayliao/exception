package com.exception.qms.model.vo.course;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class CourseChapterResponseVO implements Serializable {
    private Long id;
    private String title;
    private Integer chapterNum;
    private Boolean isSelected;
    /**
     * 是否是二级章节
     */
    private Boolean isSecondary;
    private Boolean isHeader;
    private String enTitle;
}
