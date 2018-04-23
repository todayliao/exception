package com.exception.qms.web.form.question;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午5:11
 * @discription
 **/
@Data
public class QuestionForm implements Serializable {
    @NotBlank
    private String title;
    @NotBlank
    private String questionDesc;
    @NotBlank
    private String answerDesc;
    @NotEmpty
    private List<Long> tagIds;
}
