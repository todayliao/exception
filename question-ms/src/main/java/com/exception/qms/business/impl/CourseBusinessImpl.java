package com.exception.qms.business.impl;

import com.exception.qms.business.CourseBusiness;
import com.exception.qms.domain.entity.*;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.model.vo.course.CourseChapterResponseVO;
import com.exception.qms.model.vo.course.QueryCourseContentResponseVO;
import com.exception.qms.model.vo.course.QueryCoursePageListResponseVO;
import com.exception.qms.service.CourseService;
import com.exception.qms.utils.MarkdownUtil;
import com.google.common.base.Objects;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.exception.common.PageQueryResponse;

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
public class CourseBusinessImpl implements CourseBusiness {

    @Autowired
    private CourseService courseService;
    @Autowired
    private Mapper mapper;

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
    public QueryCourseContentResponseVO queryCourseContent(Long courseId, Integer chapter) {
        Course course = courseService.findTitleById(courseId);

        if (course == null) {
            log.warn("the course not exited, courseId: {}", courseId);
            throw new QMSException(QmsResponseCodeEnum.PARAM_ERROR);
        }

        List<CourseChapter> courseChapters = courseService.findChaptersByCourseId(courseId);

        // 获取对应章节的 chapterId
        List<CourseChapter> tmpList = courseChapters.stream()
                .filter(p -> Objects.equal(p.getChapterNum(), chapter)).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(tmpList)) {
            log.warn("the chapter num of the course not exited, courseId: {}, chapterNum: {}", courseId, chapter);
            throw new QMSException(QmsResponseCodeEnum.PARAM_ERROR);
        }

        Long chapterId = tmpList.get(0).getId();
        String chapterTitle = tmpList.get(0).getTitle();

        // 组合目录数据
        List<CourseChapterResponseVO> courseChapterResponseVOS = courseChapters.stream()
                .map(p -> {
                    CourseChapterResponseVO courseChapterResponseVO = mapper.map(p, CourseChapterResponseVO.class);
                    courseChapterResponseVO.setIsSelected(false);
                    if (Objects.equal(chapterId, p.getId())) {
                        courseChapterResponseVO.setIsSelected(true);
                    }
                    return courseChapterResponseVO;
                }).collect(Collectors.toList());

        CourseChapterContent chapterContent = courseService.findContentByChaperId(chapterId);

        if (chapterContent == null) {
            log.warn("the chapter content not exited, courseId: {}, chapterId: {}", courseId, chapterId);
            throw new QMSException(QmsResponseCodeEnum.ARTICLE_NOT_EXIST);
        }

        QueryCourseContentResponseVO queryCourseContentResponseVO = mapper.map(chapterContent, QueryCourseContentResponseVO.class);
        queryCourseContentResponseVO.setId(courseId);
        queryCourseContentResponseVO.setChapters(courseChapterResponseVOS);
        queryCourseContentResponseVO.setTitle(course.getTitle());
        queryCourseContentResponseVO.setContentHtml(MarkdownUtil.parse2Html(chapterContent.getContent()));
        queryCourseContentResponseVO.setChapterTitle(chapterTitle);

        return queryCourseContentResponseVO;
    }
}
