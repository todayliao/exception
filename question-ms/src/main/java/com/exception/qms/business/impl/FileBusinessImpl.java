package com.exception.qms.business.impl;

import com.exception.qms.business.FileBusiness;
import com.exception.qms.business.TagBusiness;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.common.EditorMdUploadImageResponse;
import com.exception.qms.domain.entity.Tag;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.exception.BaseException;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.TagService;
import com.exception.qms.utils.AliyunOSSClient;
import com.exception.qms.utils.KeyUtil;
import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;
import com.exception.qms.web.dto.question.response.QueryTagsByNameResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class FileBusinessImpl implements FileBusiness {

    @Autowired
    private AliyunOSSClient aliyunOSSClient;

    @Override
    public EditorMdUploadImageResponse editorMdImageUpload(MultipartFile file) {
        log.info("file upload start ==>");
        if (file == null) {
            log.warn("file upload error: the file is null.");
            throw new QMSException(QmsResponseCodeEnum.UPLOAD_FILE_NULL);
        }
        log.info("file info ==> contentType:{}, originName:{}", file.getContentType(), file.getOriginalFilename());

        String fileKey = KeyUtil.genUniqueKey();
        boolean isSuccess = aliyunOSSClient.uploadFile(fileKey, file);


        if (isSuccess) {
            String url = String.format("https://exception-image-bucket.oss-cn-hangzhou.aliyuncs.com/%s", fileKey);
            return new EditorMdUploadImageResponse().success(url);
        }
        return new EditorMdUploadImageResponse().fail(QmsResponseCodeEnum.UPLOAD_FILE_FAIL.getErrorMessage());
    }
}
