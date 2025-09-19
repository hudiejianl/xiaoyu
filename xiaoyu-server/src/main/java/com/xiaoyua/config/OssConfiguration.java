package com.xiaoyua.config;

import com.xiaoyua.properties.AliOssProperties;
import com.xiaoyua.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OSS配置类
 * 
 * @author xiaoyu
 */
@Configuration
@EnableConfigurationProperties(AliOssProperties.class)
@Slf4j
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OssUtil aliOssUtil(AliOssProperties aliOssProperties){
        log.info("开始创建阿里云文件上传工具类对象：{}",aliOssProperties);
        return new OssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}