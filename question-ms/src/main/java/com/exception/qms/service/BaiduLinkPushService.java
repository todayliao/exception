package com.exception.qms.service;

import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.entity.UserAnswerContribution;
import com.exception.qms.domain.entity.UserQuestionContribution;

import java.time.LocalDate;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription 百度连接推送
 **/
public interface BaiduLinkPushService {

    void pushQuestionDetailPageLink(long questionId);

    void pushArticleDetailPageLink(long articleId);

}
