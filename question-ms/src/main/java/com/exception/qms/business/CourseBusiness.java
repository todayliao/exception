package com.exception.qms.business;

import com.exception.qms.model.vo.course.QueryCourseContentResponseVO;
import com.exception.qms.model.vo.user.QueryUserPageListResponseVO;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface CourseBusiness {

    PageQueryResponse queryCoursePageList(Integer pageIndex, Integer pageSize);

    QueryCourseContentResponseVO queryCourseContent(Long courseId, Integer chapter);
}
