package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.BaiduArticleLinkPush;

public interface BaiduArticleLinkPushMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BaiduArticleLinkPush record);

    int insertSelective(BaiduArticleLinkPush record);

    BaiduArticleLinkPush selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaiduArticleLinkPush record);

    int updateByPrimaryKey(BaiduArticleLinkPush record);
}