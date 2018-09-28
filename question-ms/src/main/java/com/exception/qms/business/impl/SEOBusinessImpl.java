package com.exception.qms.business.impl;

import com.exception.qms.business.SEOBusiness;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.enhancement.UserAnswerContributionStatistics;
import com.exception.qms.domain.enhancement.UserQuestionContributionStatistics;
import com.exception.qms.domain.entity.*;
import com.exception.qms.service.*;
import com.exception.qms.utils.ConstantsUtil;
import com.exception.qms.utils.TimeUtil;
import com.exception.qms.web.dto.user.response.QueryContributionDataItemDTO;
import com.exception.qms.web.dto.user.response.QueryContributionDataResponseDTO;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.user.QueryUserDetailQuestionItemResponseVO;
import com.exception.qms.web.vo.user.QueryUserDetailResponseVO;
import com.exception.qms.web.vo.user.QueryUserPageListResponseVO;
import com.google.common.collect.Lists;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.W3CDateFormat;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class SEOBusinessImpl implements SEOBusiness {

    @Value("${domain}")
    private String domain;

    @Autowired
    private QuestionService questionService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private BaiduLinkPushService baiduLinkPushService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RecommendedArticleService recommendedArticleService;

    @Override
    public BaseResponse pushAllQuestion() {
        List<Question> questions = questionService.queryAllQuestions();
        questions.parallelStream().forEach(question -> baiduLinkPushService.pushQuestionDetailPageLink(question.getId()));
        return new BaseResponse().success();
    }

    @Override
    public BaseResponse pushAllRecommendedArticle() {
        List<RecommendedArticle> recommendedArticles = recommendedArticleService.queryAllArticles();
        recommendedArticles.parallelStream().forEach(recommendedArticle -> baiduLinkPushService.pushRecommendedArticleDetailPageLink(recommendedArticle.getId()));
        return new BaseResponse().success();
    }

    @Override
    public String createSiteMapXmlContent() {
        String baseUrl = String.format("https://%s", domain);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(ConstantsUtil.FORMATTER_DATE);

        WebSitemapGenerator wsg = null;
        try {
            wsg = new WebSitemapGenerator(baseUrl);
            // home page
            WebSitemapUrl url = new WebSitemapUrl.Options(baseUrl + "/home")
                    .lastMod(dateTimeFormatter.format(LocalDateTime.now()))
                    .priority(1.0)
                    .changeFreq(ChangeFreq.ALWAYS)
                    .build();
            wsg.addUrl(url);

            // recommended article list page
            WebSitemapUrl recommendedArtcileUrl = new WebSitemapUrl.Options(baseUrl + "/recommended/article")
                    .lastMod(dateTimeFormatter.format(LocalDateTime.now()))
                    .priority(0.9)
                    .changeFreq(ChangeFreq.ALWAYS)
                    .build();
            wsg.addUrl(recommendedArtcileUrl);

            // step1：question detail pages
            List<Question> questions = questionService.queryAllQuestions();
            List<Long> questionIds = questions.stream().map(Question::getId).collect(Collectors.toList());

            List<Answer> answers = answerService.queryByQuestionIds(questionIds);
            Map<Long, Answer> map = answers.stream().collect(Collectors.toMap(Answer::getQuestionId, answer -> answer));

            for (Question question : questions) {
                Long questionId = question.getId();
                LocalDateTime answerLatestUpdateTime = map.get(questionId).getUpdateTime();
                LocalDateTime questionLatestUpdateTime = question.getUpdateTime();
                LocalDateTime updateTime =
                        questionLatestUpdateTime.isBefore(answerLatestUpdateTime) ? answerLatestUpdateTime : questionLatestUpdateTime;

                WebSitemapUrl tmpUrl = new WebSitemapUrl.Options(baseUrl + "/question/" + questionId)
                        .lastMod(dateTimeFormatter.format(updateTime))
                        .priority(0.9)
                        .changeFreq(ChangeFreq.DAILY)
                        .build();
                wsg.addUrl(tmpUrl);
            }

            // step2: recommended article
            List<RecommendedArticle> recommendedArticles = recommendedArticleService.queryAllArticles();
            for (RecommendedArticle recommendedArticle : recommendedArticles) {
                Long recommendedArticleId = recommendedArticle.getId();
                LocalDateTime updateTime = recommendedArticle.getUpdateTime();

                WebSitemapUrl tmpUrl = new WebSitemapUrl.Options(baseUrl + "/recommended/article/" + recommendedArticleId)
                        .lastMod(dateTimeFormatter.format(updateTime))
                        .priority(0.9)
                        .changeFreq(ChangeFreq.DAILY)
                        .build();
                wsg.addUrl(tmpUrl);
            }

            // step3: articles
            List<Article> articles = articleService.queryAll();
            for (Article article : articles) {
                Long articleId = article.getId();
                LocalDateTime updateTime = article.getUpdateTime();

                WebSitemapUrl tmpUrl = new WebSitemapUrl.Options(baseUrl + "/article/" + articleId)
                        .lastMod(dateTimeFormatter.format(updateTime))
                        .priority(0.9)
                        .changeFreq(ChangeFreq.DAILY)
                        .build();
                wsg.addUrl(tmpUrl);
            }
        } catch (Exception e) {
            log.error("create sitemap xml error: ", e);
        }
        return String.join("", wsg.writeAsStrings());
    }
}
