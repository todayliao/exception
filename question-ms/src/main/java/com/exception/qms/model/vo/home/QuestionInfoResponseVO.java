package com.exception.qms.model.vo.home;

import com.exception.qms.model.vo.common.TagResponseVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/26
 * @time 上午11:12
 * @discription
 **/
@Data
public class QuestionInfoResponseVO implements Serializable {
    private Long id;

    private String titleCn;

    private String questionDesc;

    private List<TagResponseVO> tags;

}
