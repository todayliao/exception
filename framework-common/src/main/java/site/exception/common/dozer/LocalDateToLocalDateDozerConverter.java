package site.exception.common.dozer;

import org.dozer.DozerConverter;

import java.time.LocalDate;

/**
 * @author jiangbing(江冰)
 * @date 2018/1/3
 * @time 下午5:58
 * @discription
 **/
public class LocalDateToLocalDateDozerConverter extends DozerConverter<LocalDate, LocalDate> {
    public LocalDateToLocalDateDozerConverter() {
        super(LocalDate.class, LocalDate.class);
    }

    @Override
    public LocalDate convertFrom(LocalDate source, LocalDate destination) {
        return source;
    }

    @Override
    public LocalDate convertTo(LocalDate source, LocalDate destination) {
        return source;
    }
}
