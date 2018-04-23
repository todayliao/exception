package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.mapper.UserMapper;
import com.exception.qms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryUsersByUserIds(List<Long> userIds) {
        return userMapper.queryUsersByUserIds(userIds);
    }

    @Override
    public User queryByUserName(String userName) {
        return userMapper.queryByUserName(userName);
    }
}
