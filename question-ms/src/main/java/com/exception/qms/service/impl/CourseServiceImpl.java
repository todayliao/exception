package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.service.CourseService;
import com.exception.qms.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseChapterMapper courseChapterMapper;
    @Autowired
    private CourseChapterContentMapper courseChapterContentMapper;

    @Override
    public int findCourseListTotalCount() {
        return courseMapper.findCourseListTotalCount();
    }

    @Override
    public List<Course> findCourseList(Integer pageIndex, Integer pageSize) {
        return courseMapper.findCourseList(PageUtil.calculateLimitSelectSqlStart(pageIndex, pageSize), pageSize);
    }

    @Override
    public Course findCourseById(Long courseId) {
        return courseMapper.findCourseById(courseId);
    }

    @Override
    public List<CourseChapter> findChaptersByCourseId(Long courseId) {
        return courseChapterMapper.findChaptersByCourseId(courseId);
    }

    @Override
    public CourseChapterContent findContentByChaperId(Long chapterId) {
        return courseChapterContentMapper.findContentByChaperId(chapterId);
    }

    @Override
    public Course findByEnTitle(String enTitle) {
        return courseMapper.findByEnTitle(enTitle);
    }

    @Override
    public CourseChapter findChapterByEnTitle(String chapterEnTitle) {
        return courseChapterMapper.findChaptersByEnTitle(chapterEnTitle);
    }

    @Override
    public CourseChapter findChapterByChapterId(long chapterId) {
        return courseChapterMapper.findChapterByChapterId(chapterId);
    }

    @Override
    public int addCourseRecord(Course course) {
        return courseMapper.insert(course);
    }

    @Override
    public int addCourseChapterBatch(List<CourseChapter> courseChapters) {
        return courseChapterMapper.addCourseChapterBatch(courseChapters);
    }

    @Override
    public int addCourseChapterContent(CourseChapterContent courseChapterContent) {
        return courseChapterContentMapper.addCourseChapterContent(courseChapterContent);
    }

    @Override
    public int updateCourseChapter(CourseChapter courseChapter) {
        return courseChapterMapper.updateByPrimaryKeySelective(courseChapter);
    }

    @Override
    public int updateCourseChapterContent(String seoKeywords, String content, Long chapterId) {
        return courseChapterContentMapper.updateCourseChapterContent(seoKeywords, content, chapterId);
    }
}
