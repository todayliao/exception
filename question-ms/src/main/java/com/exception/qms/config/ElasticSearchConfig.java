package com.exception.qms.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/17
 * @time 下午2:33
 * @discription
 **/
@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private int esPort;

    @Value("${elasticsearch.cluster.name}")
    private String esName;

    @Bean
    public TransportClient esClient() throws UnknownHostException {
        TransportClient client = null;
        try {
            Settings settings = Settings.builder()
                    .put("cluster.name", esName)
                    .put("client.transport.sniff", true)
                    .build();

            InetSocketTransportAddress master = new InetSocketTransportAddress(
                    InetAddress.getByName(esHost), esPort
            );

            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(master);
        } catch (UnknownHostException e) {
            log.error("esClient init exception, ", e);
        }

        log.info("esClient init success ...");
        return client;
    }
}
