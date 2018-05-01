package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.AnswerVoteUserRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnswerVoteUserRelMapper {
    int deleteByPrimaryKey(Long id);

    int deleteVoteDownRecord(@Param("answerId") long answerId, @Param("userId") long userId, @Param("operationType") int operationType);

    int insert(AnswerVoteUserRel record);

    int insertSelective(AnswerVoteUserRel record);

    AnswerVoteUserRel selectByPrimaryKey(Long id);

    List<AnswerVoteUserRel> queryByAnswerIdsAndVoteUserId(@Param("answerIds") List<Long> answerIds,
                                                          @Param("userId") long userId,
                                                          @Param("operationType") int operationType);

    int updateByPrimaryKeySelective(AnswerVoteUserRel record);

    int updateByPrimaryKey(AnswerVoteUserRel record);
}