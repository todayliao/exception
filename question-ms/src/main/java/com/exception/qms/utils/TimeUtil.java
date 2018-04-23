package com.exception.qms.utils;

import org.springframework.format.annotation.DateTimeFormat;
import sun.util.resources.cldr.dav.LocaleNames_dav;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription 时间工具类
 **/
public class TimeUtil {

    /**
     * 计算距离现在时间的时间差
     * @param startTime
     * @return
     */
    public static String calculateTimeDifference(LocalDateTime startTime) {
        if (startTime == null) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        // 时间在本日之内 -> 几秒前，几分钟前，几小时前
        LocalDateTime startTimeOfDay = now.with(LocalTime.MIN);
        LocalDateTime startTimeOfMonth = now.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime startTimeOfYear = now.with(TemporalAdjusters.firstDayOfYear());

        boolean isTimeOnDay = (startTime.equals(startTimeOfDay) || startTime.isAfter(startTimeOfDay))
                && startTime.isBefore(now);

        boolean isTimeOnMonth = (startTime.equals(startTimeOfMonth) || startTime.isAfter(startTimeOfMonth))
                && startTime.isBefore(now);

        boolean isTimeOnYear = (startTime.equals(startTimeOfYear) || startTime.isAfter(startTimeOfYear))
                && startTime.isBefore(now);
        if (isTimeOnDay) {
            LocalDateTime beforeOneMinutes = now.minusMinutes(1);
            LocalDateTime beforeOneHours = now.minusHours(1);
            // 一分钟以内，显示几秒前
            if (startTime.isAfter(beforeOneMinutes)) {
                long duration = ChronoUnit.SECONDS.between(startTime, now);
                return String.format("%d 秒前", duration + 1);
            } else if (startTime.isAfter(beforeOneHours)) {
                long duration = ChronoUnit.MINUTES.between(startTime, now);
                return String.format("%d 分钟前", duration + 1);
            } else  {
                long duration = ChronoUnit.HOURS.between(startTime, now);
                return String.format("%d 小时前", duration + 1);
            }
        }
        // 时间在本月之内，显示 -> 几天前
        else if (isTimeOnMonth) {
            long duration = ChronoUnit.DAYS.between(startTime, now);
            return String.format("%d 天前", duration + 1);
        }
        // 时间在本年内 -> 几月前
        else if (isTimeOnYear) {
            long duration = ChronoUnit.MONTHS.between(startTime, now);
            return String.format("%d 个月前", duration + 1);
        }
        // 其他 -> 几年前
        else {
            // todo 这里用 ChronoUnit 计算年发现一个 bug
            long duration = ChronoUnit.YEARS.between(startTime, now);
            return String.format("%d 年前", duration + 1);
        }
    }
}
