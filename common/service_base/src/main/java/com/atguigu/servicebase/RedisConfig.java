package com.atguigu.servicebase;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration//配置类
@EnableCaching//开启缓存
public class RedisConfig extends CachingConfigurerSupport {
}
