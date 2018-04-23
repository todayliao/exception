package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.FileBusiness;
import com.exception.qms.business.QuestionBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.EditorMdUploadImageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController extends BaseController {

    @Autowired
    private FileBusiness fileBusiness;

    /**
     * 文件上传
     *
     * @return
     */
    @PostMapping("/editorMdImg/upload")
    public EditorMdUploadImageResponse editorMdImageUpload(@RequestParam(value = "editormd-image-file") MultipartFile file) {
        return fileBusiness.editorMdImageUpload(file);
    }

}
