package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.CourseChapter;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseChapterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CourseChapter record);

    int insertSelective(CourseChapter record);

    int addCourseChapterBatch(List<CourseChapter> courseChapters);

    CourseChapter selectByPrimaryKey(Long id);

    List<CourseChapter> findChaptersByCourseId(Long courseId);

    CourseChapter findChaptersByEnTitle(String chapterEnTitle);

    CourseChapter findChapterByChapterId(long chapterId);

    List<CourseChapter> findByChapterIds(List<Long> chapterIds);

    CourseChapter findPreChapter(@Param("courseId") Long courseId,
                                 @Param("chapterNum") Integer chapterNum);

    CourseChapter findNextChapter(@Param("courseId") Long courseId,
                                  @Param("chapterNum") Integer chapterNum);

    int updateByPrimaryKeySelective(CourseChapter record);

    int updateByPrimaryKey(CourseChapter record);

}