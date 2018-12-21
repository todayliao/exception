package com.exception.qms.task;

import com.exception.qms.business.SearchBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jiangbing(江冰)
 * @date 2018/6/29
 * @time 下午9:48
 * @discription es 相关定时任务
 **/
@Component
@Slf4j
public class SearchTask {

    @Autowired
    private SearchBusiness searchBusiness;


}
