package com.exception.qms.model.form.course;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/11/26
 * @time 下午4:05
 * @discription
 **/
@Data
public class PublishCourseForm implements Serializable {
    @NotBlank
    private String title;
    @NotBlank
    private String titleImage;
    @NotBlank
    private String summary;
    /**
     * 教程类型
     */
    @NotNull
    private Integer type;
    @NotBlank
    private String chapterJson;
}
