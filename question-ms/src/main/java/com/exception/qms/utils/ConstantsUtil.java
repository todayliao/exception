package com.exception.qms.utils;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription 常量工具类
 **/
public class ConstantsUtil {

    /** 问题标签最大数 */
    public static final int MAX_QUESTION_TAG_COUNT = 5;

    /** elasticsearch 相关 **/
    public static final String EXCEPTION_INDEX = "exception";
    public static final String RECOMMENDED_ARTICLE_INDEX = "recommended_article";

    public static final String EXCEPTION_INDEX_TYPE = "question";
    public static final String RECOMMENDED_ARTICLE_INDEX_TYPE = "recommendedArticle";

    public static final String FORMATTER_DATE = "yyyy-MM-dd";
    public static final String GOOGLE_FORMATTER_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static final String FORMATTER_DATE_TIME = "yyyy-MM-dd HH:mm:ss";


}
