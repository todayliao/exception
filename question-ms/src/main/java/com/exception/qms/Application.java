package com.exception.qms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import site.exception.utils.JsonUtil;

@SpringBootApplication
@MapperScan("com.exception.qms.domain.mapper")
@EnableScheduling
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    /**
     * 序列化反序列化,更改 json 序列化和反序列化的方式，统一使用 jackson
     *
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setObjectMapper(JsonUtil.OBJECT_MAPPER);
        return jsonConverter;
    }

	/**
	 * validator
	 * requestParam 注解的参数校验需要加上 MethodValidationPostProcessor bean
	 *
	 * @return
	 */
	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}

	/**
	 * 允许跨域访问
	 *
	 * @return
	 */
//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", buildConfig());
//		return new CorsFilter(source);
//	}
//
//	private CorsConfiguration buildConfig() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		corsConfiguration.addAllowedOrigin("*");
//		corsConfiguration.addAllowedHeader("*");
//		corsConfiguration.addAllowedMethod("*");
//		// 预检请求的有效期，单位为秒。
//		corsConfiguration.setMaxAge(3600L);
//		// 是否支持安全证书
//		corsConfiguration.setAllowCredentials(true);
//		return corsConfiguration;
//	}

	@Bean(destroyMethod = "shutdown")
	public ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean() {
		ThreadPoolExecutorFactoryBean threadPoolExecutorFactoryBean = BeanUtils.instantiate(ThreadPoolExecutorFactoryBean.class);
		threadPoolExecutorFactoryBean.setCorePoolSize(10);
		threadPoolExecutorFactoryBean.setMaxPoolSize(30);
		return threadPoolExecutorFactoryBean;
	}

//	@Bean
//	public TaskExecutor taskExceutorBean() {
//		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//		executor.setCorePoolSize(1);
//		executor.setMaxPoolSize(30);
//		executor.initialize();
//		return executor;
//	}

}
