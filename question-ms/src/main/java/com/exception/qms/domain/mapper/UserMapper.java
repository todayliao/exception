package com.exception.qms.domain.mapper;

import com.exception.qms.domain.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User queryByUserName(String userName);

    List<User> queryUsersByUserIds(List<Long> userIds);

    int queryUserPageListCount();

    List<User> queryUserPageList(@Param("start") int start, @Param("pageSize") int pageSize);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int updateLastLoginTime(Long userId);
}