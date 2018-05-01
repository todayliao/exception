package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.QuestionVoteUserRel;
import org.apache.ibatis.annotations.Param;

public interface QuestionVoteUserRelMapper {
    int deleteByPrimaryKey(Long id);

    int deleteVoteDownRecord(@Param("questionId") long questionId, @Param("userId") long userId, @Param("operationType") int operationType);

    int insert(QuestionVoteUserRel record);

    int insertSelective(QuestionVoteUserRel record);

    QuestionVoteUserRel selectByPrimaryKey(Long id);

    int queryCurrentUserVoteUpCount(@Param("userId") long userId,
                                    @Param("questionId") long questionId,
                                    @Param("operationType") int operationType);

    int updateByPrimaryKeySelective(QuestionVoteUserRel record);

    int updateByPrimaryKey(QuestionVoteUserRel record);
}