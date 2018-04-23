package com.exception.qms.business;

import com.exception.qms.common.EditorMdUploadImageResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface FileBusiness {

    EditorMdUploadImageResponse editorMdImageUpload(MultipartFile file);
}
