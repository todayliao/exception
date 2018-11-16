package com.exception.qms.business;

import com.exception.qms.model.vo.user.QueryUserPageListResponseVO;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

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
