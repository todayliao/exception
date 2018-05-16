package com.exception.qms.domain.enhancement;

import com.exception.qms.domain.entity.UserAnswerContribution;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 解决方案贡献统计
 **/
@Data
public class UserAnswerContributionStatistics extends UserAnswerContribution {

    /**
     * 贡献日期 yyyy-MM-dd
     */
    private LocalDate contributeDate;

}