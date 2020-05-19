package com.atguigu.eduorder.client;

import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("service-ucenter")
@Component
public interface UcenterMemberClient {
    //根据用户id获取用户信息
    @GetMapping("/educenter/member/getUserInfoById/{id}")
    public UcenterMemberOrder getUserInfoOrder(@PathVariable String id);
}

