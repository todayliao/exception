package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface HealthArticleBusiness {

    BaseResponse queryHealthArticleContent(Long articleId);

    PageQueryResponse queryHealthArticleList(Integer pageIndex, Integer pageSize);
}
