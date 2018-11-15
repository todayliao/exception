package com.exception.qms.business.impl;

import com.exception.qms.business.RecommendedArticleBusiness;
import com.exception.qms.domain.entity.RecommendedArticle;
import com.exception.qms.domain.entity.RecommendedArticleContent;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.RecommendedArticleService;
import com.exception.qms.utils.StringUtil;
import com.exception.qms.web.vo.article.QueryRecommendedArticleListItemResponseVO;
import com.exception.qms.web.vo.article.RecommendedArticleDetailResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.exception.common.PageQueryResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class RecommendArticleBusinessImpl implements RecommendedArticleBusiness {

    @Autowired
    private RecommendedArticleService recommendedArticleService;
    @Autowired
    private Mapper mapper;

    @Override
    public RecommendedArticleDetailResponseVO queryArticleDetail(Long articleId) {
        log.info("query the detail of recommended article, id ==> {}", articleId);
        RecommendedArticle recommendedArticle = recommendedArticleService.queryArticleDetail(articleId);
        if (recommendedArticle == null) {
            log.warn("the recommended article is not exited, id: {}", articleId);
            throw new QMSException(QmsResponseCodeEnum.ARTICLE_NOT_EXIST);
        }
        RecommendedArticleContent recommendedArticleContent = recommendedArticleService.queryArticleContent(articleId);

        RecommendedArticleDetailResponseVO recommendedArticleDetailResponseVO = mapper.map(recommendedArticle, RecommendedArticleDetailResponseVO.class);
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(recommendedArticle.getDateTime(),0, ZoneOffset.ofHours(8));
        String publishTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        recommendedArticleDetailResponseVO.setPublishTime(publishTime);
        recommendedArticleDetailResponseVO.setContent(recommendedArticleContent.getContent());

        // seo
        // description 最多显示 200 字符
        int limit = 200;
        String content = StringUtil.getContentFromHtml(recommendedArticleContent.getContent());
        if (content.length() > limit) {
            recommendedArticleDetailResponseVO.setSeoDescription(content.substring(0, limit) + " ...");
        } else {
            recommendedArticleDetailResponseVO.setSeoDescription(content);
        }

        return recommendedArticleDetailResponseVO;
    }

    @Override
    public PageQueryResponse<QueryRecommendedArticleListItemResponseVO> queryRecommendedArticleList(Integer pageIndex, Integer pageSize) {
        int totalCount = recommendedArticleService.queryRecommendedArticleTotalCount();

        List<QueryRecommendedArticleListItemResponseVO> queryRecommendedArticleListItemResponseVOS = null;
        if (totalCount > 0) {
            List<RecommendedArticle> recommendedArticles = recommendedArticleService.queryRecommendedArticleList(pageIndex, pageSize);

            queryRecommendedArticleListItemResponseVOS = recommendedArticles.stream()
                    .map(recommendedArticle -> {
                        QueryRecommendedArticleListItemResponseVO queryRecommendedArticleListItemResponseVO = mapper.map(recommendedArticle, QueryRecommendedArticleListItemResponseVO.class);
                        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(recommendedArticle.getDateTime(),0, ZoneOffset.ofHours(8));
                        String publishTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
                        queryRecommendedArticleListItemResponseVO.setPublishTime(publishTime);
                        return queryRecommendedArticleListItemResponseVO;
                    })
                    .collect(Collectors.toList());
        }
        return new PageQueryResponse<QueryRecommendedArticleListItemResponseVO>()
                .successPage(queryRecommendedArticleListItemResponseVOS, pageIndex, totalCount, pageSize);
    }
}
