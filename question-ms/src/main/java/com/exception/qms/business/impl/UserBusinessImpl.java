package com.exception.qms.business.impl;

import com.exception.qms.business.TagBusiness;
import com.exception.qms.business.UserBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.PageQueryResponse;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.domain.entity.User;
import com.exception.qms.service.TagService;
import com.exception.qms.service.UserService;
import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;
import com.exception.qms.web.dto.question.response.QueryTagsByNameResponseDTO;
import com.exception.qms.web.vo.user.QueryUserPageListResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserBusinessImpl implements UserBusiness {

    @Autowired
    private UserService userService;

    @Override
    public PageQueryResponse<QueryUserPageListResponseVO> queryUserPageList(Integer pageIndex, Integer pageSize, String tab) {
        int totalCount = userService.queryUserPageListCount();

        List<QueryUserPageListResponseVO> userPageListResponseVOS = null;
        if (totalCount > 0) {

            List<User> users = userService.queryUserPageList(pageIndex, pageSize);

            userPageListResponseVOS = users.stream().map(user -> {
                QueryUserPageListResponseVO queryUserPageListResponseVO = new QueryUserPageListResponseVO();
                queryUserPageListResponseVO.setUserId(user.getId());
                queryUserPageListResponseVO.setUserName(user.getName());
                queryUserPageListResponseVO.setUserAvatar(user.getAvatar());
                queryUserPageListResponseVO.setUserIntroduction(user.getIntroduction());
                return queryUserPageListResponseVO;
            }).collect(Collectors.toList());
        }

        return new PageQueryResponse<QueryUserPageListResponseVO>().
                successPage(userPageListResponseVOS, pageIndex, totalCount, pageSize);
    }
}
