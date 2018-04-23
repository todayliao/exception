package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.User;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User queryByUserName(String userName);

    List<User> queryUsersByUserIds(List<Long> userIds);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}