package com.exception.qms.web.dto.user.response;

import lombok.Data;

import javax.validation.constraints.NotNull;
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
public class QueryContributionDataResponseDTO implements Serializable {

    private Integer totalCountOfMonth;
    private List<QueryContributionDataItemDTO> contributionItems;

}
