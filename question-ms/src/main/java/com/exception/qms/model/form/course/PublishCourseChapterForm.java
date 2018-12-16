package com.exception.qms.model.form.course;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/11/26
 * @time 下午4:25
 * @discription
 **/
@Data
public class PublishCourseChapterForm implements Serializable {
    @NotBlank
    private String chapterTitle;
    @NotNull
    private Integer chapterNum;
}
