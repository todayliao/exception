package com.exception.qms.business.impl;

import com.exception.qms.business.TagBusiness;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.service.TagService;
import com.exception.qms.model.dto.question.request.QueryTagsByNameRequestDTO;
import com.exception.qms.model.dto.question.response.QueryTagsByNameResponseDTO;
import com.exception.qms.model.vo.tag.QueryTagPageListResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class TagBusinessImpl implements TagBusiness {

    @Autowired
    private TagService tagService;
    @Autowired
    private Mapper mapper;

    @Override
    public BaseResponse queryTagsByTagName(QueryTagsByNameRequestDTO queryTagsByNameRequestDTO) {
        List<Tag> tags = tagService.queryTagsByTagName(queryTagsByNameRequestDTO);
        List<QueryTagsByNameResponseDTO> queryTagsByNameResponseDTOS = tags.stream().map(tag -> {
            QueryTagsByNameResponseDTO queryTagsByNameResponseDTO = new QueryTagsByNameResponseDTO();
            queryTagsByNameResponseDTO.setTagId(tag.getId());
            queryTagsByNameResponseDTO.setTagName(tag.getName());
            return queryTagsByNameResponseDTO;
        }).collect(Collectors.toList());

        return new BaseResponse().success(queryTagsByNameResponseDTOS);
    }

    @Override
    public PageQueryResponse<QueryTagPageListResponseVO> queryTagPageList(Integer pageIndex, Integer pageSize) {
        int totalCount = tagService.queryTagTotalCount();

        List<QueryTagPageListResponseVO> queryTagPageListResponseVOS = null;
        if (totalCount > 0) {
            List<Tag> tags = tagService.queryTagPageList(pageIndex, pageSize);
            queryTagPageListResponseVOS = tags.stream()
                    .map(tag -> mapper.map(tag, QueryTagPageListResponseVO.class))
                    .collect(Collectors.toList());
        }
        return new PageQueryResponse<QueryTagPageListResponseVO>().successPage(queryTagPageListResponseVOS,
                pageIndex, totalCount, pageSize);
    }
}
