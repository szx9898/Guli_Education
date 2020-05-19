package com.atguigu.msmservice.controller;

import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("edumsm/msm")
@CrossOrigin
public class MsmController {
    @Autowired
    private MsmService msmService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //发送短信
    @GetMapping("send/{phone}")
    public R sendMsm(@PathVariable String phone){
        String codeString = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(codeString)){
            return R.ok();
        }
        //生成随机值，传递阿里云进行转发
        String code= RandomUtil.getFourBitRandom();
        Map<String,Object>  param = new HashMap<>();
        param.put("code",code);
        //发送短信
        boolean isSend = msmService.sendMessage(param,phone);
        if (isSend) {
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.ok();
        } else {
            return R.error();
        }
    }
}
