package com.exception.qms.service;

import com.exception.qms.domain.entity.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface ArticleService {

    int addArticle(Article article);

    int addArticleContent(ArticleContent articleContent);

    void batchAddArticleTagRel(List<ArticleTagRel> articleTagRels);

    Article queryArticleInfo(Long articleId);

    ArticleContent queryArticleContent(Long articleId);

    List<Article> queryHomeArticleList(int limit, String limitTime);

    List<ArticleContent> queryArticleContents(List<Long> articleIds);
}
