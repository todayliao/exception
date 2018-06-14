package com.exception.qms.business.impl;

import com.exception.qms.business.SEOBusiness;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.enhancement.UserAnswerContributionStatistics;
import com.exception.qms.domain.enhancement.UserQuestionContributionStatistics;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.entity.UserAnswerContribution;
import com.exception.qms.domain.entity.UserQuestionContribution;
import com.exception.qms.service.BaiduLinkPushService;
import com.exception.qms.service.QuestionService;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.service.UserService;
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
    private BaiduLinkPushService baiduLinkPushService;

    @Override
    public BaseResponse pushAllQuestion() {
        List<Question> questions = questionService.queryAllQuestions();
        questions.parallelStream().forEach(question -> {
            baiduLinkPushService.pushQuestionDetailPageLink(question.getId());
        });
        return new BaseResponse().success();
    }

    @Override
    public String createSiteMapXmlContent() {
        String baseUrl = String.format("https://%s", domain);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        WebSitemapGenerator wsg = null;
        try {
            wsg = new WebSitemapGenerator(baseUrl);
            // home page
            WebSitemapUrl url = new WebSitemapUrl.Options(baseUrl + "/home")
                    .lastMod(dateTimeFormatter.format(LocalDateTime.now())).priority(1.0).changeFreq(ChangeFreq.DAILY).build();
            wsg.addUrl(url);

            // question detail pages
            List<Question> questions = questionService.queryAllQuestions();

            for (Question question : questions) {
                WebSitemapUrl tmpUrl = new WebSitemapUrl.Options(baseUrl + "/question/" + question.getId())
                        .lastMod(dateTimeFormatter.format(question.getUpdateTime())).priority(0.9).changeFreq(ChangeFreq.DAILY).build();
                wsg.addUrl(tmpUrl);
            }
        } catch (Exception e) {
            log.error("create sitemap xml error: ", e);
        }
        return String.join("", wsg.writeAsStrings());
    }
}
