package com.exception.qms.service;

import com.exception.qms.domain.entity.Course;
import com.exception.qms.domain.entity.CourseChapter;
import com.exception.qms.domain.entity.CourseChapterContent;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface CourseService {

    int findCourseListTotalCount();

    List<Course> findCourseList(Integer pageIndex, Integer pageSize);

    Course findTitleById(Long courseId);

    List<CourseChapter> findChaptersByCourseId(Long courseId);

    CourseChapterContent findContentByChaperId(Long chapterId);

    Course findTitleByEnTitle(String enTitle);

    int addCourseRecord(Course course);

    int addCourseChapterBatch(List<CourseChapter> courseChapters);
}
