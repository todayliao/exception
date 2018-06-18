package com.exception.qms.service;

import com.exception.qms.domain.entity.QuestionTagRel;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface TagService {

    List<Tag> queryTagsByTagName(QueryTagsByNameRequestDTO queryTagsByNameRequestDTO);

    int queryTagTotalCount();

    List<Tag> queryTagPageList(Integer pageIndex, Integer pageSize);

    Tag queryById(Long tagId);
}
