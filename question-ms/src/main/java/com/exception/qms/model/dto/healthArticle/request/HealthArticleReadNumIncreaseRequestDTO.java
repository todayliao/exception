package com.exception.qms.model.dto.healthArticle.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午7:59
 * @discription
 **/
@Data
public class HealthArticleReadNumIncreaseRequestDTO implements Serializable {
    @NotNull
    private Long articleId;
}
