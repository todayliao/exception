package com.exception.qms.model.vo.course;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2019/2/9
 * @time 20:10
 * @discription
 **/
@Data
public class ChapterPageResponseVO implements Serializable {
    private String chapterTitle;
    private String courseEnTitle;
    private String chapterEnTitle;
}
