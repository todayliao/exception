package com.exception.qms.domain.enhancement;

import com.exception.qms.domain.entity.UserQuestionContribution;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription 解决方案贡献统计
 **/
@Data
public class UserQuestionContributionStatistics extends UserQuestionContribution {

    /**
     * 贡献日期 yyyy-MM-dd
     */
    private LocalDate contributeDate;

}