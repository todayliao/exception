package com.exception.qms.web.form.question;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午5:11
 * @discription
 **/
@Data
public class QuestionUpdateForm implements Serializable {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String questionDesc;
}
