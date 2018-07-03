package com.exception.qms.domain.mapper;

import com.exception.qms.domain.enhancement.HotQuestionTagRel;
import com.exception.qms.domain.entity.QuestionTagRel;
import com.exception.qms.domain.entity.Tag;

import java.util.List;

public interface QuestionTagRelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(QuestionTagRel record);

    void batchAddQuestionTagRel(List<QuestionTagRel> questionTagRels);

    int insertSelective(QuestionTagRel record);

    QuestionTagRel selectByPrimaryKey(Long id);

    List<QuestionTagRel> queryTagsByQuestionIds(List<Long> questionIds);

    List<Long> selectTagIdByQuestionId(Long questionId);

    List<QuestionTagRel> queryByTagId(Long tagId);

    List<HotQuestionTagRel> selectHotTags();

    List<Tag> tagQuestionCountStatistics();

    int updateByPrimaryKeySelective(QuestionTagRel record);

    int updateByPrimaryKey(QuestionTagRel record);
}