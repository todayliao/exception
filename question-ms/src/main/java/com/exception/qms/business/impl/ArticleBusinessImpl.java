package com.exception.qms.business.impl;

import com.exception.qms.business.ArticleBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.enums.UserQuestionContributionTypeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.ArticleService;
import com.exception.qms.service.BaiduLinkPushService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.*;
import com.exception.qms.web.form.article.ArticleForm;
import com.exception.qms.web.vo.article.ArticleDetailResponseVO;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class ArticleBusinessImpl implements ArticleBusiness {

    @Autowired
    private AliyunOSSClient aliyunOSSClient;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private BaiduLinkPushService baiduLinkPushService;
    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse commitArticle(ArticleForm articleForm, Long userId) {

        // 判断标签逻辑
        List<Long> tagIds = articleForm.getTagIds().stream()
                .filter(tagId -> tagId != null).distinct().collect(Collectors.toList());

        int size = tagIds.size();
        if (size <= 0 || size > ConstantsUtil.MAX_QUESTION_TAG_COUNT) {
            log.warn("the tagIds size over 5");
            throw new QMSException(QmsResponseCodeEnum.QUESTION_TAGS_OVER);
        }


        String content = articleForm.getContent();
        String titleImageUrl = StringUtil.getFirstImageUrlFromMarkdown(content);

        Article article = new Article();
        article.setCreateUserId(userId);
        article.setTitle(StringUtil.spacingText(articleForm.getTitle()));
        article.setTitleImage(titleImageUrl == null ? "" : titleImageUrl);
        article.setType(articleForm.getType());
        articleService.addArticle(article);

        Long articleId = article.getId();

        ArticleContent articleContent = new ArticleContent();
        articleContent.setArticleId(articleId);
        articleContent.setContent(StringUtil.spacingText(articleForm.getContent()));
        articleService.addArticleContent(articleContent);

        // batch add articleTagRel
        List<ArticleTagRel> articleTagRels = Lists.newArrayList();
        tagIds.forEach(tagId -> {
            ArticleTagRel articleTagRel = new ArticleTagRel();
            articleTagRel.setTagId(tagId);
            articleTagRel.setArticleId(articleId);
            articleTagRels.add(articleTagRel);
        });
        articleService.batchAddArticleTagRel(articleTagRels);

        // 异步 es 索引 todo

        // 异步添加记录贡献
        executorService.execute(() -> userService.addArticleContribution(articleId, userId));

        // 异步推送链接给百度，加快收录速度
        executorService.execute(() -> baiduLinkPushService.pushArticleDetailPageLink(articleId));

        return new BaseResponse().success(articleId);
    }

    @Override
    public ArticleDetailResponseVO queryArticleDetail(Long articleId) {
        Article article = articleService.queryArticleInfo(articleId);

        if (article == null) {
            log.warn("the article doesn't exist, article id : {}", articleId);
            throw new QMSException(QmsResponseCodeEnum.ARTICLE_NOT_EXIST);
        }

        ArticleDetailResponseVO articleDetailResponseVO = mapper.map(article, ArticleDetailResponseVO.class);
        // 日期格式转换
        articleDetailResponseVO.setCreateDateStr(DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE).format(article.getCreateTime()));
        articleDetailResponseVO.setCreateTimeStr(DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE_TIME).format(article.getCreateTime()));

        ArticleContent articleContent = articleService.queryArticleContent(articleId);

        articleDetailResponseVO.setContentHtml(MarkdownUtil.parse2Html(articleContent.getContent()));

        // 作者信息
        List<User> users = userService.queryUsersByUserIds(Arrays.asList(article.getCreateUserId()));
        if (!CollectionUtils.isEmpty(users)) {
            User createUser = users.get(0);
            articleDetailResponseVO.setAuthorAvatar(createUser.getAvatar());
            articleDetailResponseVO.setAuthorName(createUser.getName());
            articleDetailResponseVO.setAuthorIntroduction(createUser.getIntroduction());
        }

        // seo
        String content = articleContent.getContent();
        // description 最多显示 200 字符
        int limit = 200;
        if (content.length() > limit) {
            articleDetailResponseVO.setSeoDescription(content.substring(0, limit));
        } else {
            articleDetailResponseVO.setSeoDescription(content);
        }

        // keywords
        List<Tag> tags = articleService.queryArticleTags(articleId);
        String keywords = tags.stream().map(Tag::getName).collect(Collectors.joining(","));
        articleDetailResponseVO.setSeoKeywords(keywords);

        // tags
        List<TagResponseVO> tagResponseVOS = tags.stream().map(tag -> {
            TagResponseVO tagResponseVO = new TagResponseVO();
            tagResponseVO.setTagId(tag.getId());
            tagResponseVO.setTagName(tag.getName());
            return tagResponseVO;
        }).collect(Collectors.toList());
        articleDetailResponseVO.setTags(tagResponseVOS);

        return articleDetailResponseVO;
    }
}
