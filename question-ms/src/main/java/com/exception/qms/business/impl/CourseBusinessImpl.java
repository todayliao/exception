package com.exception.qms.business.impl;

import com.alibaba.fastjson.JSONObject;
import com.exception.qms.business.CourseBusiness;
import com.exception.qms.domain.entity.Course;
import com.exception.qms.domain.entity.CourseChapter;
import com.exception.qms.domain.entity.CourseChapterContent;
import com.exception.qms.domain.entity.User;
import com.exception.qms.exception.ResourceNotFoundException;
import com.exception.qms.model.form.course.EditCourseChapterForm;
import com.exception.qms.model.form.course.PublishCourseForm;
import com.exception.qms.model.vo.course.*;
import com.exception.qms.service.BaiduLinkPushService;
import com.exception.qms.service.CourseService;
import com.exception.qms.utils.MarkdownUtil;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;
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
public class CourseBusinessImpl implements CourseBusiness {

    @Autowired
    private CourseService courseService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private BaiduLinkPushService baiduLinkPushService;

    @Override
    public PageQueryResponse queryCoursePageList(Integer pageIndex, Integer pageSize) {
        int totalCount = courseService.findCourseListTotalCount();

        List<QueryCoursePageListResponseVO> queryCoursePageListResponseVOS = null;
        if (totalCount > 0) {
            List<Course> courses = courseService.findCourseList(pageIndex, pageSize);

            queryCoursePageListResponseVOS = courses.stream().map(p -> {
                QueryCoursePageListResponseVO queryCoursePageListResponseVO = mapper.map(p, QueryCoursePageListResponseVO.class);
                queryCoursePageListResponseVO.setCreateTime(p.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                return queryCoursePageListResponseVO;
            }).collect(Collectors.toList());
        }

        return new PageQueryResponse()
                .successPage(queryCoursePageListResponseVOS, pageIndex, totalCount, pageSize);
    }

    @Override
    public QueryCourseContentResponseVO queryCourseContent(String courseEnTitle, String chapterEnTitle) {
        // 查询该教程是否存在
        Course course = courseService.findByEnTitle(courseEnTitle);

        if (course == null) {
            log.error("the resource not found, courseEnTile: {}", courseEnTitle);
            throw new ResourceNotFoundException();
        }

        Long courseId = course.getId();

        if (course == null) {
            log.warn("the course not exited, courseId: {}", courseId);
            throw new ResourceNotFoundException();
        }

        List<CourseChapter> courseChapters = courseService.findChaptersByCourseId(courseId);

        CourseChapter courseChapter = courseChapters.get(0);
        // 获取对应章节的 chapterId, 默认第一章节
        String chapterTitle = courseChapter.getTitle();
        Long chapterId = null;
        Integer chapterNum = null;
        // chapterId 为空的情况，设置为第一章节 enTitle
        if (StringUtils.isBlank(chapterEnTitle)) {
            chapterId = courseChapter.getId();
            chapterEnTitle = courseChapter.getEnTitle();
            chapterNum = courseChapter.getChapterNum();
        }
        // 如果 chapterEnTitle 不为空
        else {
            String finalChapterEnTitle = chapterEnTitle;
            List<CourseChapter> tmpList = courseChapters.stream()
                    .filter(p -> Objects.equal(p.getEnTitle(), finalChapterEnTitle)).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(tmpList)) {
                log.warn("the chapter of the course not exited, courseId: {}, chapterId: {}", courseId, chapterId);
                throw new ResourceNotFoundException();
            }
            chapterTitle = tmpList.get(0).getTitle();
            chapterId = tmpList.get(0).getId();
            chapterNum = tmpList.get(0).getChapterNum();
        }

        // 组合目录数据
        Long finalChapterId = chapterId;
        List<CourseChapterResponseVO> courseChapterResponseVOS = courseChapters.stream()
                .map(p -> {
                    CourseChapterResponseVO courseChapterResponseVO = mapper.map(p, CourseChapterResponseVO.class);
                    courseChapterResponseVO.setIsSelected(false);
                    if (Objects.equal(finalChapterId, p.getId())) {
                        courseChapterResponseVO.setIsSelected(true);
                    }
                    return courseChapterResponseVO;
                }).collect(Collectors.toList());

        CourseChapterContent chapterContent = courseService.findContentByChaperId(chapterId);

        QueryCourseContentResponseVO queryCourseContentResponseVO = null;
        if (chapterContent != null) {
            queryCourseContentResponseVO = mapper.map(chapterContent, QueryCourseContentResponseVO.class);
            queryCourseContentResponseVO.setContentHtml(MarkdownUtil.parse2Html(chapterContent.getContent()));
            queryCourseContentResponseVO.setChapterSEOKeywords(chapterContent.getSeoKeywords());
            // description 最多显示 120 字符
            int limit = 300;
            if (chapterContent.getContent().length() > limit) {
                queryCourseContentResponseVO.setChapterSEODescription(chapterContent.getContent().substring(0, limit));
            } else {
                queryCourseContentResponseVO.setChapterSEODescription(chapterContent.getContent());
            }
        } else {
            queryCourseContentResponseVO = new QueryCourseContentResponseVO();
            queryCourseContentResponseVO.setContentHtml("// TODO 正在努力憋稿中, 内容会尽快上线与您见面 ...");
            queryCourseContentResponseVO.setChapterSEODescription(course.getTitle());
            queryCourseContentResponseVO.setChapterSEOKeywords(course.getTitle());
        }
        queryCourseContentResponseVO.setId(courseId);
        queryCourseContentResponseVO.setTitle(course.getTitle());
        queryCourseContentResponseVO.setEnTitle(course.getEnTitle());
        queryCourseContentResponseVO.setChapterId(chapterId);
        queryCourseContentResponseVO.setChapterTitle(chapterTitle);
        queryCourseContentResponseVO.setChapterEnTitle(chapterEnTitle);
        queryCourseContentResponseVO.setChapters(courseChapterResponseVOS);

        // 上下章节
        // 上
        if (!Objects.equal(chapterNum, 1)) {
            CourseChapter preChapter = courseService.findPreChapter(courseId, chapterNum);
            if (preChapter != null) {
                ChapterPageResponseVO preChapterVO = new ChapterPageResponseVO();
                preChapterVO.setCourseEnTitle(courseEnTitle);
                preChapterVO.setChapterEnTitle(preChapter.getEnTitle());
                preChapterVO.setChapterTitle(preChapter.getTitle());
                queryCourseContentResponseVO.setPreChapter(preChapterVO);
            }
        }
        // 下
        CourseChapter nextChapter = courseService.findNextChapter(courseId, chapterNum);
        if (nextChapter != null) {
            ChapterPageResponseVO nextChapterVO = new ChapterPageResponseVO();
            nextChapterVO.setCourseEnTitle(courseEnTitle);
            nextChapterVO.setChapterEnTitle(nextChapter.getEnTitle());
            nextChapterVO.setChapterTitle(nextChapter.getTitle());
            queryCourseContentResponseVO.setNextChapter(nextChapterVO);
        }
        return queryCourseContentResponseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse publishCourse(PublishCourseForm publishCourseDTO, User user) {
        Course course = mapper.map(publishCourseDTO, Course.class);
        Long userId = user.getId();
        course.setCreateUserId(userId);

        // 先存 course
        courseService.addCourseRecord(course);
        Long courseId = course.getId();

        // 再存 course chapter
        String chapterJson = publishCourseDTO.getChapterJson();
        List<JSONObject> chapters = JSONObject.parseObject(chapterJson, List.class);

        List<CourseChapter> courseChapters = chapters.stream().map(p -> {
            CourseChapter courseChapter = new CourseChapter();
            courseChapter.setCourseId(courseId);
            courseChapter.setTitle(p.getString("chapterTitle"));
            courseChapter.setChapterNum(p.getInteger("chapterNum"));
            courseChapter.setCreateUserId(userId);
            return courseChapter;
        }).collect(Collectors.toList());

        courseService.addCourseChapterBatch(courseChapters);
        return new BaseResponse().success();
    }

    @Override
    public EditCourseChapterResponseVO showEditChapterPage(String courseEnTitle, String chapterEnTitle) {
//        CourseChapter courseChapter = courseService.findChapterByChapterId(chapterId);
        CourseChapter courseChapter = courseService.findChapterByEnTitle(chapterEnTitle);
        EditCourseChapterResponseVO editCourseChapterResponseVO = null;
        if (courseChapter != null) {
            Long chapterId = courseChapter.getId();
            editCourseChapterResponseVO = mapper.map(courseChapter, EditCourseChapterResponseVO.class);
            CourseChapterContent chapterContent = courseService.findContentByChaperId(chapterId);
            editCourseChapterResponseVO.setSeoKeywords(chapterContent == null ? null : chapterContent.getSeoKeywords());
            editCourseChapterResponseVO.setContent(chapterContent == null ? null : chapterContent.getContent());
        }
        return editCourseChapterResponseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse editChapter(EditCourseChapterForm editCourseChapterForm, User user) {
        // 先更新章节表
        CourseChapter courseChapter = mapper.map(editCourseChapterForm, CourseChapter.class);
        courseService.updateCourseChapter(courseChapter);

        Long chapterId = editCourseChapterForm.getId();
        // 在判断是否是插入章节内容表，还是更新
        CourseChapterContent courseChapterContent = courseService.findContentByChaperId(chapterId);

        if (courseChapterContent == null) {
            // 生成章节content记录
            CourseChapterContent tmp = mapper.map(editCourseChapterForm, CourseChapterContent.class);
            tmp.setChapterId(chapterId);
            courseService.addCourseChapterContent(tmp);
            // 异步推送百度新增内容
            executorService.submit(() -> baiduLinkPushService.pushCourseChapterPageLink(chapterId));
        } else {
            // 更新章节content记录
            String seoKeywords = editCourseChapterForm.getSeoKeywords();
            String content = editCourseChapterForm.getContent();
            courseService.updateCourseChapterContent(seoKeywords, content, chapterId);
            // todo 异步推送百度更新内容
        }

        return new BaseResponse().success();
    }

    @Override
    public boolean pushToBaidu(String courseEnTitle, String chapterEnTitle) {
        return baiduLinkPushService.pushCourseChapterPageLink(courseEnTitle, chapterEnTitle);
    }
}
