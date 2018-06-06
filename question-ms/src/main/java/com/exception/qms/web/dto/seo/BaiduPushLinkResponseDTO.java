package com.exception.qms.web.dto.seo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/10
 * @time 下午7:59
 * @discription
 **/
@Data
public class BaiduPushLinkResponseDTO implements Serializable {
    /**
     * 成功推送的url条数
     */
    private Integer success;

    /**
     * 当天剩余的可推送url条数
     */
    private Integer remain;

    /**
     * 由于不是本站url而未处理的url列表
     */
    @JsonProperty("not_same_site")
    private String notSameSite;

    /**
     * 不合法的url列表
     */
    @JsonProperty("not_valid")
    private String notValid;

    /**
     * 错误码，与状态码相同
     */
    private Integer error;

    /**
     * 错误描述
     */
    private String message;
}
