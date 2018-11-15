package com.exception.qms.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午8:45
 * @discription editorMd 响应类
 **/
@Data
public class EditorMdUploadImageResponse implements Serializable {

    /**
     * 0 表示上传失败，1 表示上传成功
     */
    private Integer success = 0;
    private String message;
    private String url;

    public EditorMdUploadImageResponse fail(String errorMessage) {
        this.setSuccess(0);
        this.setMessage(errorMessage);
        return this;
    }

    public EditorMdUploadImageResponse success(String message, String url) {
        this.setSuccess(1);
        this.setMessage("upload image success");
        this.setUrl(url);
        return this;
    }

    public EditorMdUploadImageResponse success(String url) {
        this.setSuccess(1);
        this.setMessage("upload image success");
        this.setUrl(url);
        return this;
    }

}
