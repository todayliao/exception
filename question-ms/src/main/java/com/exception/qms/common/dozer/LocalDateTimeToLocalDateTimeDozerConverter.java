package com.exception.qms.common.dozer;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2018/1/3
 * @time 下午5:57
 * @discription
 **/
public class LocalDateTimeToLocalDateTimeDozerConverter extends DozerConverter<LocalDateTime, LocalDateTime> {
    public LocalDateTimeToLocalDateTimeDozerConverter() {
        super(LocalDateTime.class, LocalDateTime.class);
    }

    @Override
    public LocalDateTime convertFrom(LocalDateTime source, LocalDateTime destination) {
        return source;
    }

    @Override
    public LocalDateTime convertTo(LocalDateTime source, LocalDateTime destination) {
        return source;
    }
}
