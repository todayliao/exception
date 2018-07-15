package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.HomePageQueryResponse;
import com.exception.qms.web.vo.home.QueryHomeItemPageListResponseVO;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface HomeBusiness {

    /**
     * 展示首页问题列表
     * @return
     * @param qLimitTime
     * @param limitTime
     */
    HomePageQueryResponse<QueryHomeItemPageListResponseVO> queryHomePageList(String qLimitTime, String limitTime);

    BaseResponse queryHotTags();

    BaseResponse queryHotQuestions();
}
