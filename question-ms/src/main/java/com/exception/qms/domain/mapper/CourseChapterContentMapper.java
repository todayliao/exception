package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.CourseChapterContent;
import org.apache.ibatis.annotations.Param;

public interface CourseChapterContentMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CourseChapterContent record);

    int insertSelective(CourseChapterContent record);

    CourseChapterContent selectByPrimaryKey(Long id);

    CourseChapterContent findContentByChaperId(@Param("chapterId") Long courseId);

    int updateByPrimaryKeySelective(CourseChapterContent record);

    int updateByPrimaryKeyWithBLOBs(CourseChapterContent record);

    int updateByPrimaryKey(CourseChapterContent record);
}