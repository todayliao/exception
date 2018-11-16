package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.QuestionTagRel;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.domain.mapper.QuestionTagRelMapper;
import com.exception.qms.domain.mapper.TagMapper;
import com.exception.qms.service.TagService;
import com.exception.qms.utils.PageUtil;
import com.exception.qms.model.dto.question.request.QueryTagsByNameRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> queryTagsByTagName(QueryTagsByNameRequestDTO queryTagsByNameRequestDTO) {
        if (queryTagsByNameRequestDTO == null || StringUtils.isBlank(queryTagsByNameRequestDTO.getTagName())) {
            return null;
        }
        return tagMapper.queryTagsByTagName(queryTagsByNameRequestDTO.getTagName());
    }

    @Override
    public int queryTagTotalCount() {
        return tagMapper.queryTagTotalCount();
    }

    @Override
    public List<Tag> queryTagPageList(Integer pageIndex, Integer pageSize) {
        return tagMapper.queryTagPageList(PageUtil.calculateLimitSelectSqlStart(pageIndex, pageSize), pageSize);
    }

    @Override
    public Tag queryById(Long tagId) {
        return tagMapper.selectByPrimaryKey(tagId);
    }

    @Override
    public int updateTagQuestionCount(Tag tag) {
        return tagMapper.updateQuestionCountByTagId(tag.getId(), tag.getQuestionCount());
    }

}
