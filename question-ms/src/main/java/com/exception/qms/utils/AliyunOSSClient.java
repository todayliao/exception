package com.exception.qms.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阿里云 OSS 上传文件封装类
 *
 * @author jiangbing（江冰）
 **/
@Slf4j
@Component
@Getter
public class AliyunOSSClient {

    /**
     * endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
     * 如果您还没有创建Bucket，endpoint选择请参看文档中心的“开发人员指南 > 基本概念 > 访问域名”，
     * 链接地址是：https://help.aliyun.com/document_detail/oss/user_guide/oss_concept/endpoint.html?spm=5176.docoss/user_guide/endpoint_region
     * endpoint的格式形如“http://oss-cn-hangzhou.aliyuncs.com/”，注意http://后不带bucket名称，
     * 比如“http://bucket-name.oss-cn-hangzhou.aliyuncs.com”，是错误的endpoint，请去掉其中的“bucket-name”。
     */
    @Value("${aliyunoss.endpoint}")
    private String endpoint;

    /**
     * oss内网上传节点地址
     */
//    private String internalendpoint;

    /**
     * accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
     * 创建和查看访问密钥的链接地址是：https://ak-console.aliyun.com/#/。
     * 注意：accessKeyId和accessKeySecret前后都没有空格，从控制台复制时请检查并去除多余的空格。
     */
    @Value("${aliyunoss.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyunoss.accessKeySecret}")
    private String accessKeySecret;

    /**
     * Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
     * Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
     */
//    @Value("${aliyunoss.bucketName}")
//    private String bucketName;

    /**
     * 公有的bucket
     */
    @Value("${aliyunoss.publicBucketName}")
    private String publicBucketName;

    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
//    private static String firstKey = "my-first-key";

    private OSSClient ossClient;

    /**
     * OSSClient 初始化
     *
     * <p>Note:注意 init 后一定要销毁（调用 destroy()）</p>
     */
//    public void init() {
//        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
//        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
//        ossClient = new OSSClient(aliyunOSSConfig.getEndpoint(),
//                aliyunOSSConfig.getAccessKeyId(),
//                aliyunOSSConfig.getAccessKeySecret());
//    }


    /**
     * OSSClient 销毁
     */
//    public void destroy() {
//        if (ossClient != null) {
//            ossClient.shutdown();
//        }
//    }

    /**
     * 根据 bucketName 查看 bucket 是否已经被创建
     * @param bucketName
     * @return
     */
    public boolean isBucketExisted(String bucketName) {

        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }

            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                log.info("传入的 BucketName:{} 已经被创建", bucketName);
                return true;
            } else {
                log.info("传入的 BucketName:{} 未被创建", bucketName);
                return false;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 根据传入的 bucket 名称来创建 bucket
     *
     * @param bucketName bucket 名称
     * @return
     */
    public boolean createBucket(String bucketName) {
        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }

            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                log.info("要创建的 BucketName:{} 已存在，或被其他用户占用, 不再创建...", bucketName);
                return false;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 开始创建", bucketName);
                // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
                // 设置bucket权限为公共读，默认是私有读写
                CreateBucketRequest createBucketRequest= new CreateBucketRequest(bucketName);
                // SDK 默认是私有的
                createBucketRequest.setCannedACL(CannedAccessControlList.Private);
                ossClient.createBucket(createBucketRequest);
                return true;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 根据传入的 bucket 名称获取该 bucket 相关信息,包括如下：
     *
     * <ul>
     *     <li>1.数据中心</li>
     *     <li>2.创建时间</li>
     *     <li>3.用户标志</li>
     * </ul>
     *
     * @param bucketName
     * @return
     */
    public BucketInfo getBucketInfo(String bucketName) {
        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                // 查看Bucket信息。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
                BucketInfo info = ossClient.getBucketInfo(bucketName);
                log.info("Bucket {} 的信息 - 数据中心：{}, 创建时间：{}; 用户标志：{}",
                        bucketName,
                        info.getBucket().getLocation(),
                        info.getBucket().getCreationDate(),
                        info.getBucket().getOwner());
                return info;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 无法获取相关信息", bucketName);
                return null;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 向默认的 bucket 中上传文件
     *
     * <p>Note: 请务必确保 file 的 key 值唯一</p>
     * @param fileKey 文件的 key(注意唯一)
     * @return
     */
//    public boolean uploadFile(String fileKey, File file) {
//        return uploadFile(aliyunOSSConfig, aliyunOSSConfig.getBucketName(), fileKey, file);
//    }

    /**
     * 向默认的 bucket 中上传文件
     *
     * <p>Note: 请务必确保 file 的 key 值唯一</p>
     * @param fileKey 文件的 key(注意唯一)
     * @param inputStream
     * @return
     */
//    public boolean uploadFile(String fileKey, InputStream inputStream) {
//        return uploadFile(aliyunOSSConfig.getBucketName(), fileKey, inputStream);
//    }

    /**
     * 向 bucket 中上传文件
     *
     * <p>Note: 请务必确保 file 的 key 值唯一</p>
     * @param fileKey 文件的 key(注意唯一)
     * @return
     */
    public boolean uploadFile(String fileKey, MultipartFile multipartFile) {

        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
        OSSClient client = new OSSClient(endpoint,
                accessKeyId,
                accessKeySecret);
        try {
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (client.doesBucketExist(publicBucketName)) {
                // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
                // 创建上传Object的Metadata
                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentType(multipartFile.getContentType());
                client.putObject(publicBucketName, fileKey, new ByteArrayInputStream(multipartFile.getBytes()), meta);
                log.info("向 OSS bucket {} 中添加 key 为 {} 的文件成功", publicBucketName, fileKey);
                return true;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 无法上传文件", publicBucketName);
                return false;
            }
        } catch (Exception e) {
            log.error("oss exception, ", e);
        } finally {
            client.shutdown();
        }
        return false;
    }


    /**
     * 向 bucket 中上传文件
     *
     * <p>Note: 请务必确保 file 的 key 值唯一</p>
     * @param fileKey 文件的 key(注意唯一)
     * @return
     */
//    public boolean uploadExcelFile(String fileKey, MultipartFile multipartFile) {
//
//        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
//        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
//        OSSClient client = new OSSClient(aliyunOSSConfig.getInternalendpoint(),
//                aliyunOSSConfig.getAccessKeyId(),
//                aliyunOSSConfig.getAccessKeySecret());
//        try {
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (client.doesBucketExist(aliyunOSSConfig.getBucketName())) {
//                // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//                // 创建上传Object的Metadata
//                ObjectMetadata meta = new ObjectMetadata();
//                meta.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//                meta.setContentDisposition("attachment;filename=" + multipartFile.getOriginalFilename());
//                meta.setHeader("Content-disposition",
//                        "attachment;filename=" + new String(multipartFile.getOriginalFilename().getBytes("gbk"),
//                                "iso-8859-1"));
//                client.putObject(aliyunOSSConfig.getBucketName(), fileKey, new ByteArrayInputStream(multipartFile.getBytes()), meta);
//                log.info("向 OSS bucket {} 中添加 key 为 {} 的文件成功", aliyunOSSConfig.getBucketName(), fileKey);
//                return true;
//            } else {
//                log.info("传入的 BucketName:{} 未被创建, 无法上传文件", aliyunOSSConfig.getBucketName());
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        } finally {
//            client.shutdown();
//        }
//        return false;
//    }

    /**
     * 向公有 bucket 中上传文件
     *
     * <p>Note: 请务必确保 file 的 key 值唯一</p>
     * @param fileKey 文件的 key(注意唯一)
     * @return
     */
//    public boolean uploadFilePublic(String fileKey, MultipartFile multipartFile) {
//
//        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
//        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
//        OSSClient client = new OSSClient(endpoint,
//                accessKeyId,
//                accessKeySecret);
//        try {
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (client.doesBucketExist(publicBucketName)) {
//                // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//                // 创建上传Object的Metadata
//                ObjectMetadata meta = new ObjectMetadata();
//
//                if (StringUtils.isNotBlank(multipartFile.getOriginalFilename())) {
//                    // 根据后缀设置content-type
//                    meta.setContentType(FileUtil.processFileContentType(multipartFile.getOriginalFilename()));
//                    // 图片文件不设置附件名称
//                    if (!Objects.equals(meta.getContentType(), OSSFileContentTypeEnum.JPEG.getContentType())
//                            && !Objects.equals(meta.getContentType(), OSSFileContentTypeEnum.JPG.getContentType())
//                            && !Objects.equals(meta.getContentType(), OSSFileContentTypeEnum.PNG.getContentType())) {
//                        // 设置下载时文件名称
//                        meta.setContentDisposition("attachment;filename=" + multipartFile.getOriginalFilename());
//                    }
//                }
//                client.putObject(aliyunOSSConfig.getPublicBucketName(), fileKey, new ByteArrayInputStream(multipartFile.getBytes()), meta);
//                log.info("向 OSS bucket {} 中添加 key 为 {} 的文件成功", aliyunOSSConfig.getBucketName(), fileKey);
//                return true;
//            } else {
//                log.info("传入的 BucketName:{} 未被创建, 无法上传文件", aliyunOSSConfig.getBucketName());
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        } finally {
//            client.shutdown();
//        }
//        return false;
//    }

//    /**
//     * 向 bucket 中上传 base64
//     * @param fileKey
//     * @param base64Str
//     * @param contentType
//     * @param originalFileName 文件原始文件名
//     * @return
//     */
//    public boolean uploadFile(String fileKey, String base64Str, String contentType) {
//
//        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
//        // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/init.html?spm=5176.docoss/sdk/java-sdk/get-start
//        OSSClient client = new OSSClient(aliyunOSSConfig.getEndpoint(),
//                aliyunOSSConfig.getAccessKeyId(),
//                aliyunOSSConfig.getAccessKeySecret());
//        try {
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (client.doesBucketExist(aliyunOSSConfig.getBucketName())) {
//                // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//                // 创建上传Object的Metadata
//                ObjectMetadata meta = new ObjectMetadata();
//                // 设置文件上传的content-type,用来指定文件格式
//                if (StringUtils.isNotBlank(contentType)) {
//                    meta.setContentType(contentType);
//                }
//                client.putObject(aliyunOSSConfig.getBucketName(),
//                        fileKey,
//                        new ByteArrayInputStream(Base64Utils.decodeFromString(base64Str)), meta);
//                log.info("向 OSS bucket {} 中添加 key 为 {} 的文件成功", aliyunOSSConfig.getBucketName(), fileKey);
//                return true;
//            } else {
//                log.info("传入的 BucketName:{} 未被创建, 无法上传文件", aliyunOSSConfig.getBucketName());
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        } finally {
//            client.shutdown();
//        }
//        return false;
//    }
//
//    /**
//     * 向 bucket 中上传文件
//     *
//     * <p>Note: 请务必确保 file 的 key 值唯一</p>
//     * @param bucketName bucket 名称
//     * @param fileKey 文件的 key(注意唯一)
//     * @return
//     */
//    public boolean uploadFile(String bucketName, String fileKey, File file) {
//        try {
//            if (ossClient == null) {
//                throw new RuntimeException("OSS Client not init");
//            }
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (ossClient.doesBucketExist(bucketName)) {
//                // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//                ossClient.putObject(bucketName, fileKey, file);
//                log.info("向 OSS bucket {} 中添加 key 为 {} 的文件成功", bucketName, fileKey);
//                return true;
//            } else {
//                log.info("传入的 BucketName:{} 未被创建, 无法上传文件", bucketName);
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        return false;
//    }
//
//    /**
//     * 向 bucket 中上传文件
//     *
//     * <p>Note: 请务必确保 file 的 key 值唯一</p>
//     * @param bucketName bucket 名称
//     * @param fileKey 文件的 key(注意唯一)
//     * @param inputStream 文件流
//     * @return
//     */
//    public boolean uploadFile(String bucketName, String fileKey, InputStream inputStream) {
//        try {
//            if (ossClient == null) {
//                throw new RuntimeException("OSS Client not init");
//            }
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (ossClient.doesBucketExist(bucketName)) {
//                // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/upload_object.html?spm=5176.docoss/user_guide/upload_object
//                ossClient.putObject(bucketName, fileKey, inputStream);
//                log.info("向 OSS bucket {} 中添加 key 为 {} 的文件成功", bucketName, fileKey);
//                return true;
//            } else {
//                log.info("传入的 BucketName:{} 未被创建, 无法上传文件", bucketName);
//                return false;
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        return false;
//    }
//
//    /**
//     * 根据存储的 key 获取一个临时的访问 url(从默认的 bucket)
//     *
//     * <p>
//     *     生成的访问的 Url 格式如下:
//     *
//     *     http://exc-bucket.oss-cn-beijing.aliyuncs.com/1510039256837
//     *                ?Expires=1510128021
//     *                &OSSAccessKeyId=TMP.AQEZtOEjMCVtNTMIjkkOBE89yL7iTXfoR5gtruPSxMH6OTIwhxORDBYGaKKsMC4CFQDwAW81saN3_-dUx55BzcQd0BFNRAIVAOQ9ZOys2O_IHVHn4Y6sCB3ITqqE
//     *                &Signature=G%2BCYBv35LNDqk9rG66iRB%2Bj1KM4%3D
//     * </p>
//     * @param fileKey
//     * @return
//     */
//    public String generatePresignedUrl(String fileKey) {
//        return generatePresignedUrl(aliyunOSSConfig.getBucketName(), fileKey, 1);
//    }
//
//    public String generatePresignedUrl(String fileKey, int minutes) {
//        return generatePresignedUrl(aliyunOSSConfig.getBucketName(), fileKey, minutes);
//    }
//
//    /**
//     * 根据 bucket 和存储的 key 获取一个临时的访问 url
//     *
//     * <p>
//     *     生成的访问的 Url 格式如下:
//     *
//     *     http://exc-bucket.oss-cn-beijing.aliyuncs.com/1510039256837
//     *                ?Expires=1510128021
//     *                &OSSAccessKeyId=TMP.AQEZtOEjMCVtNTMIjkkOBE89yL7iTXfoR5gtruPSxMH6OTIwhxORDBYGaKKsMC4CFQDwAW81saN3_-dUx55BzcQd0BFNRAIVAOQ9ZOys2O_IHVHn4Y6sCB3ITqqE
//     *                &Signature=G%2BCYBv35LNDqk9rG66iRB%2Bj1KM4%3D
//     * </p>
//     * @param bucketName
//     * @param fileKey
//     * @return
//     */
//    public String generatePresignedUrl(String bucketName, String fileKey, int minutes) {
//
//        try {
//            if (ossClient == null) {
//                throw new RuntimeException("OSS Client not init");
//            }
//            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
//            if (ossClient.doesBucketExist(bucketName)) {
//                Calendar calendar = Calendar.getInstance();
//                // 过期时间为 1min
//                calendar.add(Calendar.MINUTE,  minutes);
//                URL url = ossClient.generatePresignedUrl(bucketName, fileKey, calendar.getTime());
//                return url.toString();
//            } else {
//                log.info("传入的 BucketName:{} 不存在", bucketName);
//                return null;
//            }
//        } catch (Exception e) {
//            log.error("", e);
//        }
//        return null;
//    }
//
//    /**
//     * 根据文件 key 和指定的宽高生成授权 url(从默认的 bucket)
//     *
//     * @param fileKey 文件 key
//     * @param w 指定图片宽度
//     * @param h 指定图片高度
//     * @return 授权 url
//     */
//    public String generatePresignedUrlBySize(String fileKey, int w, int h) {
//        return generatePresignedUrlBySize(aliyunOSSConfig.getBucketName(), fileKey, w, h);
//    }

    /**
     * 根据传入的 bucket 名称，文件 key 和指定的宽高生成授权 url
     *
     * @param bucketName bucket 名称
     * @param fileKey 文件 key
     * @param w 指定图片宽度
     * @param h 指定图片高度
     * @return 授权 url
     */
    public String generatePresignedUrlBySize(String bucketName, String fileKey, int w, int h) {
        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                Calendar calendar = Calendar.getInstance();
                // 过期时间为 1min
                calendar.add(Calendar.MINUTE, 1);

                // 访问使用的签名url进行访问的，这需要您首先进行图片处理后，然后对图片处理的url在进行签名生成签名的url进行访问
                GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, fileKey);
                request.setExpiration(calendar.getTime());
                Map<String, String> parameters = new HashMap<>(1);
                parameters.put("x-oss-process", "image/resize,m_fixed,h_" + h + ",w_" + w);
                request.setQueryParameter(parameters);

                URL url = ossClient.generatePresignedUrl(request);
                return url.toString();
            } else {
                log.info("传入的 BucketName:{} 不存在", bucketName);
                return null;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 获取 bucket 下存储的对象集合
     * @param bucketName
     * @return
     */
    public List<OSSObjectSummary> getBucketList(String bucketName) {

        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                // 查看Bucket中的Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
                ObjectListing objectListing = ossClient.listObjects(bucketName);
                List<OSSObjectSummary> objectSummary = objectListing.getObjectSummaries();
                return objectSummary;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 无法查下所属信息", bucketName);
                return null;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 删除 bucket 中的单个 object
     * @param bucketName
     * @param key
     * @return
     */
    public boolean deleteBucketObject(String bucketName, String key) {
        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                // 删除Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
                ossClient.deleteObject(bucketName, key);
                return true;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 无法删除其中的对象", bucketName);
                return false;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 删除某个 bucket
     * @param bucketName
     * @param key
     * @return
     */
    public boolean deleteBucket(String bucketName) {
        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                // 删除Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
                ossClient.deleteBucket(bucketName);
                return true;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 无法删除", bucketName);
                return false;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

    /**
     * 删除 bucket 中的批量 object
     * @param bucketName
     * @param keys
     * @return
     */
    public boolean deleteBucketObjects(String bucketName, List<String> keys) {
        try {
            if (ossClient == null) {
                throw new RuntimeException("OSS Client not init");
            }
            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_bucket.html?spm=5176.docoss/sdk/java-sdk/init
            if (ossClient.doesBucketExist(bucketName)) {
                // 删除Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
                // 链接地址是：https://help.aliyun.com/document_detail/oss/sdk/java-sdk/manage_object.html?spm=5176.docoss/sdk/java-sdk/manage_bucket
                if (!CollectionUtils.isEmpty(keys)) {
                    for (String key : keys) {
                        ossClient.deleteObject(bucketName, key);
                    }
                }
                return true;
            } else {
                log.info("传入的 BucketName:{} 未被创建, 无法删除", bucketName);
                return false;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return false;
    }

}