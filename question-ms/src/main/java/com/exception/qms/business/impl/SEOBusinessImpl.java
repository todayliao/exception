package com.exception.qms.business.impl;

import com.exception.qms.business.SEOBusiness;
import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.CourseChapterContentMapper;
import com.exception.qms.domain.mapper.CourseChapterMapper;
import com.exception.qms.domain.mapper.CourseMapper;
import com.exception.qms.service.AnswerService;
import com.exception.qms.service.ArticleService;
import com.exception.qms.service.BaiduLinkPushService;
import com.exception.qms.service.QuestionService;
import com.exception.qms.utils.ConstantsUtil;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapGenerator;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.exception.common.BaseResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
    private CourseChapterContentMapper courseChapterContentMapper;
    @Autowired
    private CourseChapterMapper courseChapterMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public BaseResponse pushAllQuestion() {
        List<Question> questions = questionService.queryAllQuestions();
        questions.parallelStream().forEach(question -> baiduLinkPushService.pushQuestionDetailPageLink(question.getId()));
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

            // course home page
            WebSitemapUrl courseHomeUrl = new WebSitemapUrl.Options(baseUrl + "/course")
                    .lastMod(dateTimeFormatter.format(LocalDateTime.now()))
                    .priority(0.9)
                    .changeFreq(ChangeFreq.ALWAYS)
                    .build();
            wsg.addUrl(courseHomeUrl);

            // course
            // 1.查找出已经有内容的 chapterId
            List<CourseChapterContent> chapterContents = courseChapterContentMapper.findAllChapterId();
            List<Long> chapterIds = chapterContents.stream().map(CourseChapterContent::getChapterId).collect(Collectors.toList());
            // 2.查找出对应的 courseId
            List<CourseChapter> courseChapters = courseChapterMapper.findByChapterIds(chapterIds);
            Map<Long, CourseChapter> chapterIdCourseChapterMap = courseChapters.stream()
                    .collect(Collectors.toMap(CourseChapter::getId, p -> p));

            List<Long> courseIds = courseChapters.stream().map(CourseChapter::getCourseId)
                    .distinct().collect(Collectors.toList());

            List<Course> courses = courseMapper.findEnTitlesByIds(courseIds);
            Map<Long, String> courseIdCourseMap = courses.stream()
                    .collect(Collectors.toMap(Course::getId, Course::getEnTitle));

            for (CourseChapterContent courseChapterContent : chapterContents) {
                long chapterId = courseChapterContent.getChapterId();
                CourseChapter courseChapter = chapterIdCourseChapterMap.get(chapterId);
                long courseId = courseChapter.getCourseId();
                String chapterEnTitle = courseChapter.getEnTitle();
                LocalDateTime latestUpdateTime = courseChapterContent.getUpdateTime()
                        .isBefore(courseChapter.getUpdateTime()) ?
                        courseChapterContent.getUpdateTime() : courseChapter.getUpdateTime();

                WebSitemapUrl tmpUrl = new WebSitemapUrl.Options(baseUrl + "/"
                        + courseIdCourseMap.get(courseId) + "/" + chapterEnTitle)
                        .lastMod(dateTimeFormatter.format(latestUpdateTime))
                        .priority(0.9)
                        .changeFreq(ChangeFreq.DAILY)
                        .build();
                wsg.addUrl(tmpUrl);
            }

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
                        .priority(0.8)
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
                        .priority(0.8)
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
