package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Course record);

    int insertSelective(Course record);

    Course selectByPrimaryKey(Long id);

    int findCourseListTotalCount();

    List<Course> findCourseList(@Param("start") int start,
                                @Param("pageSize") int pageSize);

    Course findTitleById(Long courseId);

    Course findTitleByEnTitle(String enTitle);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKey(Course record);
}