package com.exception.qms.model.vo.course;

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
public class QueryCourseContentResponseVO implements Serializable {
    private Long id;
    private String title;
    private String contentHtml;
    private String chapterTitle;
    private List<CourseChapterResponseVO> chapters;
}
