package com.exception.qms.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author jiangbing(江冰)
 * @date 2018/6/29
 * @time 下午9:48
 * @discription 标签统计任务
 **/
@Component
@Slf4j
public class TagStatisticsTask {

    /**
     * 每天一次标签任务统计
     */
    @Scheduled(cron = "0 7 22 * * ?")
    public void tagQuestionCountStatistics() {
        log.info("start Statistics ==>");
    }
}
