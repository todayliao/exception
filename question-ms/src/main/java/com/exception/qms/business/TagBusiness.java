package com.exception.qms.business;

import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;
import com.exception.qms.web.vo.tag.QueryTagPageListResponseVO;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface TagBusiness {

    BaseResponse queryTagsByTagName(QueryTagsByNameRequestDTO queryTagsByNameDTO);

    PageQueryResponse<QueryTagPageListResponseVO> queryTagPageList(Integer pageIndex, Integer pageSize);
}
