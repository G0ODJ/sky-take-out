package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类：用于创建AliOssUtil对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    /**
     * 通过自动注入aliOssProperties对象的方式进行创建AliOssUtil
     * @param aliOssProperties
     * @return
     */
    @Bean //启用bean注解，项目启动的时候会自动运行该方法
    @ConditionalOnMissingBean //保证整个项目只有一个bean对象，因为只需要一个
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId(),
                                aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName());
    }
}
