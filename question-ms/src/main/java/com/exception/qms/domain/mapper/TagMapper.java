package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.Tag;

import java.util.List;

public interface TagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Long id);

    Tag selectTagInfoByPrimaryKey(Long tagId);

    List<Tag> queryTagsByTagName(String tagName);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKeyWithBLOBs(Tag record);

    int updateByPrimaryKey(Tag record);
}