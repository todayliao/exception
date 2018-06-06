package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.BaiduQuestionLinkPush;

public interface BaiduQuestionLinkPushMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaiduQuestionLinkPush record);

    int insertSelective(BaiduQuestionLinkPush record);

    BaiduQuestionLinkPush selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaiduQuestionLinkPush record);

    int updateByPrimaryKey(BaiduQuestionLinkPush record);
}