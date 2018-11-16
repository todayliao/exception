package com.exception.qms.elasticsearch;

import com.exception.qms.model.vo.common.TagResponseVO;
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
public class RecommendedArticleIndexTemplate {
    private Long id;
    private String title;
    private String summary;
    private Integer dateTime;
    private Integer beReadNum;
    private String author;
    private String gzh;
}
