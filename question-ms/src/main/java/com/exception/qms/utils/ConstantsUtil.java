package com.exception.qms.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

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
    public static final String EXCEPTION_INDEX_TYPE = "question";


}
