package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.service.ArticleService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleContentMapper articleContentMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;
    @Autowired
    private TagMapper tagMapper;

    @Override
    public int addArticle(Article article) {
        return articleMapper.insert(article);
    }

    @Override
    public int addArticleContent(ArticleContent articleContent) {
        return articleContentMapper.insert(articleContent);
    }

    @Override
    public void batchAddArticleTagRel(List<ArticleTagRel> articleTagRels) {
        articleTagRelMapper.batchAddArticleTagRel(articleTagRels);
    }

    @Override
    public Article queryArticleInfo(Long articleId) {
        return articleMapper.selectByPrimaryKey(articleId);
    }

    @Override
    public ArticleContent queryArticleContent(Long articleId) {
        return articleContentMapper.selectByArticleId(articleId);
    }

    @Override
    public List<Article> queryHomeArticleList(int limit, String limitTime) {
        return articleMapper.queryHomeArticleList(limit, limitTime);
    }

    @Override
    public List<ArticleContent> queryArticleContents(List<Long> articleIds) {
        return articleContentMapper.queryArticleContents(articleIds);
    }

    @Override
    public List<Tag> queryArticleTags(Long articleId) {
        List<ArticleTagRel> articleTagRels = articleTagRelMapper.queryArticleTags(articleId);
        return articleTagRels.stream().map(articleTagRel -> {
            Long tagId = articleTagRel.getTagId();
            return tagMapper.selectByPrimaryKey(tagId);
        }).collect(Collectors.toList());
    }

    @Override
    public List<Article> queryAll() {
        return articleMapper.queryAll();
    }
}
