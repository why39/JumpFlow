package com.hxy.config;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 类RedisConfig的功能描述:
 * Redis配置
 *
 * @auther hxy
 * @date 2017-11-15 21:49:31
 */
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "spring.http")
public class SpringConfig {
    private static Logger logger = Logger.getLogger(SpringConfig.class);

    private Map<String, String> multipart = new HashMap<>(); //接收prop1里面的属性值

    public Map<String, String> getMultipart() {
        return multipart;
    }

    public void setMultipart(Map<String, String> multipart) {
        this.multipart = multipart;
    }

    public String getLocation() {
        return multipart.get("location");
    }
}
