package com.exception.qms.model.dto.user.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/28
 * @time 下午5:07
 * @discription
 **/
@Data
public class QueryContributionDataItemDTO implements Serializable {

    private LocalDate date;
    private Integer count;

}
