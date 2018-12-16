package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.*;
import com.exception.qms.domain.mapper.*;
import com.exception.qms.service.CourseService;
import com.exception.qms.service.UserService;
import com.exception.qms.utils.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public Course findTitleById(Long courseId) {
        return courseMapper.findTitleById(courseId);
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
    public Course findTitleByEnTitle(String enTitle) {
        return courseMapper.findTitleByEnTitle(enTitle);
    }

    @Override
    public int addCourseRecord(Course course) {
        return courseMapper.insert(course);
    }

    @Override
    public int addCourseChapterBatch(List<CourseChapter> courseChapters) {
        return courseChapterMapper.addCourseChapterBatch(courseChapters);
    }
}
