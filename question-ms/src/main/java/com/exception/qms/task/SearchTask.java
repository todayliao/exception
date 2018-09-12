package com.exception.qms.task;

import com.exception.qms.business.SearchBusiness;
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
 * @discription es 相关定时任务
 **/
@Component
@Slf4j
public class SearchTask {

    @Autowired
    private SearchBusiness searchBusiness;

    /**
     * 每天凌晨5点推送一次优选博文的索引
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void tagQuestionCountStatistics() {
        log.info("task for tagQuestionCountStatistics start ==>");

        searchBusiness.updateAllRecommendedArticleIndex();

        log.info("task for tagQuestionCountStatistics end ==>");
    }
}
