package com.exception.qms.model.form.course;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription
 **/
@Data
public class EditCourseChapterForm implements Serializable {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotNull
    private Integer chapterNum;
    @NotBlank
    private String enTitle;
    @NotBlank
    private String seoKeywords;
    @NotBlank
    private String content;
}
