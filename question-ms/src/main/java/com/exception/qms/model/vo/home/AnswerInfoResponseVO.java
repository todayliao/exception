package com.exception.qms.model.vo.home;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/26
 * @time 上午11:12
 * @discription
 **/
@Data
public class AnswerInfoResponseVO implements Serializable {
    private Long id;

    private String answerDesc;
}
