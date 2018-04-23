package com.exception.qms.business;

import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.web.vo.home.QueryHomeQuestionPageListResponseVO;

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
     */
    PageQueryResponse<QueryHomeQuestionPageListResponseVO> queryQuestionPageList(Integer pageIndex, Integer pageSize, String tab);

    BaseResponse queryHotTags();

    BaseResponse queryHotQuestions();
}
