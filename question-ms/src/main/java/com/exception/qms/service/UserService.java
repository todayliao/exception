package com.exception.qms.service;

import com.exception.qms.domain.entity.User;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface UserService {

    /**
     * 根据 userId 批量获取用户信息
     * @param userIds
     * @return
     */
    List<User> queryUsersByUserIds(List<Long> userIds);

    User queryByUserName(String userName);
}
