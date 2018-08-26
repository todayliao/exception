package com.exception.qms.business.impl;

import com.exception.qms.business.HealthArticleBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.HealthArticleService;
import com.exception.qms.service.RedisService;
import com.exception.qms.utils.IpUtil;
import com.exception.qms.web.dto.healthArticle.request.HealthArticleReadNumIncreaseRequestDTO;
import com.exception.qms.web.dto.healthArticle.response.QueryHealthArticleContentResponseDTO;
import com.exception.qms.web.dto.healthArticle.response.QueryHealthArticleItemResponseDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private RedisService redisService;
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

    @Override
    public BaseResponse increaseReadNum(Long artileId, HttpServletRequest request) {
        log.info("increaseReadNum, the remote ip: {}", IpUtil.getIpAddr(request));

        if (artileId == null) {
            log.warn("the health article id can't null");
            throw new QMSException(QmsResponseCodeEnum.PARAM_ERROR);
        }

        String redisKey = String.format("ha_%s_%s", IpUtil.getIpAddr(request), artileId);
        boolean isExisted = redisService.exists(redisKey);

        final long expireSeconds = 1*60*60;

        if (isExisted) {
            log.warn("Can't increase the readNum of the article, the key already existed on the cache: {}", redisKey);
            // expire the key, one hour
            redisService.expire(redisKey, expireSeconds);
            return new BaseResponse().fail();
        }

        int count = healthArticleService.increaseReadNum(artileId);

        if (count > 0) {
            redisService.set(redisKey, "", expireSeconds);
            return new BaseResponse().success();
        }
        return new BaseResponse().fail();
    }

}
