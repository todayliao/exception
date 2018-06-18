package com.exception.qms.service;

import com.exception.qms.domain.entity.QuestionTagRel;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.exception.qms.web.vo.home.HomeHotTagResponseVO;

import java.util.List;
import java.util.Map;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/25
 * @time 下午2:52
 * @discription
 **/
public interface QuestionTagService {

    Map<Long, List<TagResponseVO>> queryTagInfoByQuestionIds(List<Long> questionIds);


//    List<TagResponseVO> queryTagInfoByQuestionId(Long questionId);
    List<HomeHotTagResponseVO> queryHotTags();

    List<Tag> queryByTagId(Long tagId);

    void batchAddQuestionTagRel(List<QuestionTagRel> questionTagRels);
}
