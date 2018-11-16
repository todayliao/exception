package com.exception.qms.service.impl;

import com.exception.qms.domain.enhancement.HotQuestionTagRel;
import com.exception.qms.domain.entity.QuestionTagRel;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.domain.mapper.QuestionTagRelMapper;
import com.exception.qms.domain.mapper.TagMapper;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.model.vo.common.TagResponseVO;
import com.exception.qms.model.vo.home.HomeHotTagResponseVO;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/25
 * @time 下午2:57
 * @discription
 **/
@Service
@Slf4j
public class QuestionTagServiceImpl implements QuestionTagService {

    @Autowired
    private QuestionTagRelMapper questionTagRelMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private Mapper mapper;

    @Override
    public Map<Long, List<TagResponseVO>> queryTagInfoByQuestionIds(List<Long> questionIds) {
        Map<Long, List<TagResponseVO>> questionIdTagsMap = null;
        if (!CollectionUtils.isEmpty(questionIds)) {
            questionIdTagsMap = Maps.newHashMap();

            // get quetionTagRel records batch and convert to map
            List<QuestionTagRel> questionTagRels = questionTagRelMapper.queryTagsByQuestionIds(questionIds);
            Map<Long, List<QuestionTagRel>> questionIdQuesTagRelMap = questionTagRels.stream().collect(Collectors.groupingBy(QuestionTagRel::getQuestionId));

            if (!CollectionUtils.isEmpty(questionIdQuesTagRelMap)) {
                for (Long questionId : questionIds) {
                    List<QuestionTagRel> tmp = questionIdQuesTagRelMap.get(questionId);
                    // 查询 tag 信息
                    List<TagResponseVO> tagResponseVOS = tmp.stream()
                            .map(questionTagRel -> getTagDTOByTagId(questionTagRel.getTagId()))
                            .collect(Collectors.toList());
                    questionIdTagsMap.put(questionId, tagResponseVOS);
                }
            }
        }
        return questionIdTagsMap;
    }

    /**
     * 通过 tagId 得到 TagResponseVO
     *
     * @param tagId
     * @return
     */
    private TagResponseVO getTagDTOByTagId(Long tagId) {
        Tag tag = tagMapper.selectTagInfoByPrimaryKey(tagId);
        TagResponseVO tagResponseVO = null;
        if (tag != null) {
            tagResponseVO = new TagResponseVO();
            tagResponseVO.setTagId(tag.getId());
            tagResponseVO.setTagName(tag.getName());
        }
        return tagResponseVO;
    }

//    @Override
//    public List<TagResponseVO> queryTagInfoByQuestionId(Long questionId) {
//        List<Long> tagIds = questionTagRelMapper.selectTagIdByQuestionId(questionId);
//        List<TagResponseVO> tagVOS = tagIds.stream().map(this::getTagDTOByTagId).collect(Collectors.toList());
//        return tagVOS;
//    }

    @Override
    public List<HomeHotTagResponseVO> queryHotTags() {
        List<HotQuestionTagRel> hotQuestionTagRels = questionTagRelMapper.selectHotTags();

        List<HomeHotTagResponseVO> homeHotTagResponseVOS = hotQuestionTagRels.stream().map(hotQuestionTagRel -> {
            HomeHotTagResponseVO homeHotTagResponseDTO = new HomeHotTagResponseVO();
            // 查询 tag 名称
            Tag tag = tagMapper.selectTagInfoByPrimaryKey(hotQuestionTagRel.getTagId());
            if (tag != null) {
                homeHotTagResponseDTO.setQuestionCount(hotQuestionTagRel.getQuestionCount());
                homeHotTagResponseDTO.setTagId(hotQuestionTagRel.getTagId());
                homeHotTagResponseDTO.setTagName(tag.getName());
            }
            return homeHotTagResponseDTO;
        }).collect(Collectors.toList());

        return homeHotTagResponseVOS;
    }

    @Override
    public List<QuestionTagRel> queryByTagId(Long tagId) {
        return questionTagRelMapper.queryByTagId(tagId);
    }

    @Override
    public void batchAddQuestionTagRel(List<QuestionTagRel> questionTagRels) {
        questionTagRelMapper.batchAddQuestionTagRel(questionTagRels);
    }

    @Override
    public List<Tag> tagQuestionCountStatistics() {
        return questionTagRelMapper.tagQuestionCountStatistics();
    }


}
