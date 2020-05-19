package com.atguigu.educenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.educenter.entity.UcenterMember;
import com.atguigu.educenter.entity.vo.RegisterVo;
import com.atguigu.educenter.mapper.UcenterMemberMapper;
import com.atguigu.educenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-05-16
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String login(UcenterMember member) {
        //获取登录密码和手机
        String phone = member.getMobile();
        String password = member.getPassword();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登陆失败");
        }
        //判断手机号是否正确
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", phone);
        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);
        if (ucenterMember == null) {//没有这个手机号
            throw new GuliException(20001, "登录失败");
        }
        if (!MD5.encrypt(password).equals(ucenterMember.getPassword())) {
            throw new GuliException(20001, "登录失败");
        }
        if (ucenterMember.getIsDisabled()) {
            throw new GuliException(20001, "登录失败");
        }
        //登陆成功
        return JwtUtils.getJwtToken(ucenterMember.getId(), ucenterMember.getNickname());
    }

    @Override
    public void register(RegisterVo registerVo) {
        String code = registerVo.getCode();
        String mobile = registerVo.getMobile();
        String nickName = registerVo.getNickname();
        String password = registerVo.getPassword();
        //非空判断
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "注册失败");
        }
        //判断验证码
        String phoneCode = redisTemplate.opsForValue().get(mobile);
        if (StringUtils.isEmpty(phoneCode) || !StringUtils.equals(code, phoneCode)) {
            throw new GuliException(20001, "验证码校验失败");
        }
        //判断手机号是否重复
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        if (member != null) {
            throw new GuliException(20001, "已有手机注册，请更换手机号");
        }
        UcenterMember ucenterMember = new UcenterMember();
        BeanUtils.copyProperties(registerVo, ucenterMember);
        ucenterMember.setPassword(MD5.encrypt(password));
        ucenterMember.setAvatar("http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoj0hHXhgJNOTSOFsS4uZs8x1ConecaVOB8eIl115xmJZcT4oCicvia7wMEufibKtTLqiaJeanU2Lpg3w/132");
        ucenterMember.setIsDisabled(false);
        baseMapper.insert(ucenterMember);
    }

    @Override
    public UcenterMember getOpenIdMember(String openid) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid", openid);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public Integer countRegister(String day) {

        return baseMapper.countRegisterDay(day);
    }
}
