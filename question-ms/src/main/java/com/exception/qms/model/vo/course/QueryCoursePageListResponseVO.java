package com.exception.qms.model.vo.course;

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
public class QueryCoursePageListResponseVO implements Serializable {
    private Long id;
    private String title;
    private String titleImage;
    private String summary;
    private Integer type;
    private String createTime;
}
