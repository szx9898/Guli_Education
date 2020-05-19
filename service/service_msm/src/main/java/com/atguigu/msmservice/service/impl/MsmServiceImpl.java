package com.atguigu.msmservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.atguigu.msmservice.service.MsmService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean sendMessage(Map<String, Object> param, String phone) {
        if (StringUtils.isEmpty(phone)) return false;
        DefaultProfile profile = DefaultProfile.getProfile("default","LTAI4FjNQBtv8XkBBxeU6FwD","ZOJOmES9FSLg3MCJ5H2so5rN3PnmPC");
        IAcsClient client = new DefaultAcsClient(profile);
        //设置相关固定参数
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        //设置发送相关参数
        request.putQueryParameter("PhoneNumbers",phone);
        request.putQueryParameter("SignName","乐优商城");
        request.putQueryParameter("TemplateCode","SMS_190282066");
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        //最终发送
        try {
            CommonResponse response = client.getCommonResponse(request);
            boolean success = response.getHttpResponse().isSuccess();
            return success;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
