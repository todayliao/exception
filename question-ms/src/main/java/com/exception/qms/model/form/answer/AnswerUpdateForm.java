package com.exception.qms.model.form.answer;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午5:11
 * @discription
 **/
@Data
public class AnswerUpdateForm implements Serializable {
    @NotNull
    private Long id;
    @NotBlank
    private String answerDesc;
}
