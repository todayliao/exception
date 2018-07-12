package com.exception.qms.web.form.article;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

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
public class ArticleForm implements Serializable {
    @NotBlank
    @Length(max = 30, message = "文章标题最多30个字")
    private String title;

    /**
     * 题图
     */
    @NotNull
    private MultipartFile titleImage;

    /**
     * 文章类型
     */
    @NotNull
    private Integer type;

    /**
     * 正文
     */
    @NotBlank
    private String content;

    /**
     * 文章标签
     */
    @NotEmpty
    private List<Long> tagIds;
}
