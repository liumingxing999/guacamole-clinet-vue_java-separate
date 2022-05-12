package com.star.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * 
* Title: MyConfig
* Description:
* 自定义配置文件 
* Version:1.0.0  
* @author star
* @date 2022年5月12日
 */
@Component
public class MyConfig {

    @Value("${server.port:9863}")
    private String port;

    public String getPort() {
        return port;
    }
}
