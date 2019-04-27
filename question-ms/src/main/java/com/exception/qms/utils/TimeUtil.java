package com.exception.qms.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/6
 * @time 下午12:32
 * @discription 时间工具类
 **/
public class TimeUtil {

    /** 一年的天数 **/
    private final static int YEAR_DAYS_COUNT = 365;
    /** 30天为一个月 **/
    private final static int MONTH_DAYS_COUNT = 30;

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

        boolean isTimeOnDay = (startTime.equals(startTimeOfDay) || startTime.isAfter(startTimeOfDay))
                && startTime.isBefore(now);

        // 传入的时间在本日内
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

        else {
            long untilDays = startTime.toLocalDate().until(now, ChronoUnit.DAYS);
            if (untilDays < MONTH_DAYS_COUNT) {
                return String.format("%d 天前", untilDays);
            }
            else if (untilDays >= MONTH_DAYS_COUNT) {
                return String.format("%d 个月前", untilDays / MONTH_DAYS_COUNT);
            }
            else if (untilDays >= YEAR_DAYS_COUNT) {
                return startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            }

        }
        return null;
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
//        ZoneId zone = ZoneId.of("Asia/Shanghai");
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }


    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().toLocalDate().until(LocalDate.parse("2018-06-01"), ChronoUnit.DAYS));
        System.out.println(31 / MONTH_DAYS_COUNT + 1);
    }
}
