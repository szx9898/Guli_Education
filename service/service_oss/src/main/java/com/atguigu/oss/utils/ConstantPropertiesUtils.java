package com.atguigu.oss.utils;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口一个方法
@Data
@ConfigurationProperties(prefix = "aliyun.oss.file")
public class ConstantPropertiesUtils{

    //读取配置文件内容
    private String endpoint;

    private String keyId;

    private String keySecret;

    private String bucketName;

}
