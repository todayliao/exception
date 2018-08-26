package com.exception.qms.business.impl;

import com.exception.qms.business.HealthArticleBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.HealthArticleService;
import com.exception.qms.web.dto.healthArticle.response.QueryHealthArticleContentResponseDTO;
import com.exception.qms.web.dto.healthArticle.response.QueryHealthArticleItemResponseDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class HealthArticleBusinessImpl implements HealthArticleBusiness {

    @Autowired
    private HealthArticleService healthArticleService;
    @Autowired
    private Mapper mapper;

    @Override
    public BaseResponse queryHealthArticleContent(Long articleId) {
        HealthArticle healthArticle = healthArticleService.queryHealthArticle(articleId);

        if (healthArticle == null) {
            log.warn("the health article is not exited, id: {}", articleId);
            throw new QMSException(QmsResponseCodeEnum.ARTICLE_NOT_EXIST);
        }

        QueryHealthArticleContentResponseDTO queryHealthArticleContentResponseDTO = mapper.map(healthArticle, QueryHealthArticleContentResponseDTO.class);

        HealthArticleContent healthArticleContent = healthArticleService.queryHealthArticleContent(articleId);
        queryHealthArticleContentResponseDTO.setContent(healthArticleContent.getContent());
        return new BaseResponse().success(queryHealthArticleContentResponseDTO);
    }

    @Override
    public PageQueryResponse queryHealthArticleList(Integer pageIndex, Integer pageSize) {
        int totalCount = healthArticleService.queryHealthArticleListCount();

        List<QueryHealthArticleItemResponseDTO> queryHealthArticleItemResponseDTOList = null;
        if (totalCount > 0) {
            List<HealthArticle> healthArticles = healthArticleService.queryHealthArticleList(pageIndex, pageSize);
            queryHealthArticleItemResponseDTOList = healthArticles.stream()
                    .map(healthArticle -> mapper.map(healthArticle, QueryHealthArticleItemResponseDTO.class))
                    .collect(Collectors.toList());
        }
        return new PageQueryResponse().successPage(queryHealthArticleItemResponseDTOList, pageIndex, totalCount, pageSize);
    }
}
