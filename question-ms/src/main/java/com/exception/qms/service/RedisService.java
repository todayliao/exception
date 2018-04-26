package com.exception.qms.service;

import com.exception.qms.domain.entity.User;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription
 **/
public interface RedisService {

    void set(String key, String value);

    boolean exists(String key);

}
