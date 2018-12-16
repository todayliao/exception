package com.exception.qms.service;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:01
 * @discription 百度连接推送
 **/
public interface BaiduLinkPushService {

    void pushQuestionDetailPageLink(long questionId);

    void pushArticleDetailPageLink(long articleId);

    void pushCourseChapterPageLink(long chapterId);

    void pushRecommendedArticleDetailPageLink(long articleId);

}
