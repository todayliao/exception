package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;
import com.exception.qms.web.vo.user.QueryUserPageListResponseVO;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface UserBusiness {

    PageQueryResponse<QueryUserPageListResponseVO> queryUserPageList(Integer pageIndex, Integer pageSize, String tab);

    BaseResponse queryContributionData(Long userId);

    /**
     * 查询用户展示页数据
     * @param userId
     * @return
     */
    BaseResponse queryUserDetail(Long userId, String tab);
}
