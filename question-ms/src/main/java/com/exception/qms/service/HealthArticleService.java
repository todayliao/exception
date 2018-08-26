package com.exception.qms.service;

import com.exception.qms.domain.entity.*;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface HealthArticleService {

    HealthArticleContent queryHealthArticleContent(Long articleId);

    HealthArticle queryHealthArticle(Long articleId);

    int queryHealthArticleListCount();

    List<HealthArticle> queryHealthArticleList(Integer pageIndex, Integer pageSize);
}
