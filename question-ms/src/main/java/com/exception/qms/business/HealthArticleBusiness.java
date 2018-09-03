package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.web.dto.healthArticle.request.HealthArticleReadNumIncreaseRequestDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface HealthArticleBusiness {

    BaseResponse queryHealthArticleContent(Long articleId);

    PageQueryResponse queryHealthArticleList(Integer pageIndex, Integer pageSize);

    BaseResponse increaseReadNum(Long articleId, HttpServletRequest request);

    BaseResponse queryTestArticle();

    BaseResponse queryVersion();
}
