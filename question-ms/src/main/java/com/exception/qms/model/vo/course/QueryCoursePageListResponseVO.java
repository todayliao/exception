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
public class QueryCoursePageListResponseVO implements Serializable {
    private String enTitle;
    private String title;
    private String titleImage;
    private String summary;
    private Integer type;
    private String createTime;
}
