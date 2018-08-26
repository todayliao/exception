package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.service.ArticleService;
import com.exception.qms.service.HealthArticleService;
import com.exception.qms.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class HealthArticleServiceImpl implements HealthArticleService {

    @Autowired
    private HealthArticleMapper healthArticleMapper;
    @Autowired
    private HealthArticleContentMapper healthArticleContentMapper;

    @Override
    public HealthArticleContent queryHealthArticleContent(Long articleId) {
        return healthArticleContentMapper.selectByArticleId(articleId);
    }

    @Override
    public HealthArticle queryHealthArticle(Long articleId) {
        return healthArticleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    public int queryHealthArticleListCount() {
        return healthArticleMapper.queryHealthArticleListCount();
    }

    @Override
    public List<HealthArticle> queryHealthArticleList(Integer pageIndex, Integer pageSize) {
        return healthArticleMapper.queryHealthArticleList(PageUtil.calculateLimitSelectSqlStart(pageIndex, pageSize), pageSize);
    }
}
