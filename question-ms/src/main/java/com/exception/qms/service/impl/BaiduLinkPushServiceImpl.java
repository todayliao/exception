package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.BaiduQuestionLinkPush;
import com.exception.qms.domain.mapper.BaiduQuestionLinkPushMapper;
import com.exception.qms.enums.BaiduQuestionLinkPushTypeEnum;
import com.exception.qms.service.BaiduLinkPushService;
import com.exception.qms.utils.JsonUtil;
import com.exception.qms.web.dto.seo.BaiduPushLinkResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class BaiduLinkPushServiceImpl implements BaiduLinkPushService {

    @Value("${baidu.linkPushHost}")
    private String linkPushHost;
    @Value("${baidu.linkPushToken}")
    private String linkPushToken;
    @Value("${domain}")
    private String domain;

    @Autowired
    private BaiduQuestionLinkPushMapper baiduQuestionLinkPushMapper;

    /**
     * 推送问题详情页连接
     *
     * @param questionId
     */
    @Override
    public void pushQuestionDetailPageLink(long questionId) {
        /*
         *   POST /urls?site=www.exception.site&token=QMhwk26FQ15c6FPQ HTTP/1.1
             User-Agent: curl/7.12.1
             Host: data.zz.baidu.com
             Content-Type: text/plain
             Content-Length: 83

             http://www.example.com/1.html
             http://www.example.com/2.html
         */
        String url = String.format("http://%s/urls?site=%s&token=%s", linkPushHost, domain, linkPushToken);
        String questionDetailPageUrl = String.format("%s/question/%d", domain, questionId);

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "text/plain");

        // 设置 body
        StringEntity entity = new StringEntity(questionDetailPageUrl, "UTF-8");
        httpPost.setEntity(entity);
        HttpResponse response = null;

        try {
            response = client.execute(httpPost);

            int responseCode = response.getStatusLine().getStatusCode();

            String responseJson = EntityUtils.toString(response.getEntity());
            log.info("response json: ", responseJson);

            BaiduPushLinkResponseDTO baiduPushLinkResponseDTO = JsonUtil.toBean(responseJson, BaiduPushLinkResponseDTO.class);

            BaiduQuestionLinkPush baiduQuestionLinkPush = new BaiduQuestionLinkPush();
            baiduQuestionLinkPush.setQuestionId(questionId);
            if (HttpStatus.SC_OK == responseCode) {
                log.info("push the link of question detail page success, url ==> {}, remain: {}", questionDetailPageUrl, baiduPushLinkResponseDTO.getRemain());
                baiduQuestionLinkPush.setType(BaiduQuestionLinkPushTypeEnum.SUCCESS.getCode());
                baiduQuestionLinkPush.setMessage("");
            } else {
                log.info("push the link of question detail page fail, url ==> {}", questionDetailPageUrl);
                baiduQuestionLinkPush.setType(BaiduQuestionLinkPushTypeEnum.FAIL.getCode());
                baiduQuestionLinkPush.setMessage(baiduPushLinkResponseDTO.getMessage());
            }

            // 提交记录，数据入库（不论成功或者失败）
            baiduQuestionLinkPushMapper.insert(baiduQuestionLinkPush);
        } catch (Exception e) {
            log.error("push the link of question detail page error, ", e);
        }

    }
}
