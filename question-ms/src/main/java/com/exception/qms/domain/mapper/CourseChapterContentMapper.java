package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.CourseChapterContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseChapterContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CourseChapterContent record);

    int insertSelective(CourseChapterContent record);

    int addCourseChapterContent(CourseChapterContent courseChapterContent);

    CourseChapterContent selectByPrimaryKey(Long id);

    CourseChapterContent findContentByChaperId(@Param("chapterId") Long courseId);

    List<CourseChapterContent> findAllChapterId();

    int updateByPrimaryKeySelective(CourseChapterContent record);

    int updateByPrimaryKeyWithBLOBs(CourseChapterContent record);

    int updateByPrimaryKey(CourseChapterContent record);

    int updateCourseChapterContent(@Param("seoKeywords") String seoKeywords,
                                   @Param("content") String content,
                                   @Param("chapterId") Long chapterId);

}