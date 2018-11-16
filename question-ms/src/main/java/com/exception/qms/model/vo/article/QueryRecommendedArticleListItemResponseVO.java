package com.exception.qms.model.vo.article;

import com.exception.qms.model.vo.common.TagResponseVO;
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
public class QueryRecommendedArticleListItemResponseVO implements Serializable {
    private Long id;

    private String publishTime;

    private String title;
    private String cover;
    /**
     * 摘要
     */
    private String summary;


}
