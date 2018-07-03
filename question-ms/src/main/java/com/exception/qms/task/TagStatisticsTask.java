package com.exception.qms.task;

import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/6/29
 * @time 下午9:48
 * @discription 标签统计任务
 **/
@Component
@Slf4j
public class TagStatisticsTask {

    @Autowired
    private QuestionTagService questionTagService;
    @Autowired
    private TagService tagService;

    /**
     * 每天凌晨4点统计一次标签下的问题数量
     */
    @Scheduled(cron = "0 58 21 * * ?")
    public void tagQuestionCountStatistics() {
        log.info("task for tagQuestionCountStatistics start ==>");

        List<Tag> tags = questionTagService.tagQuestionCountStatistics();

        // todo 待分片更新
        tags.forEach(tag -> tagService.updateTagQuestionCount(tag));

        log.info("task for tagQuestionCountStatistics end ==>");
    }
}
