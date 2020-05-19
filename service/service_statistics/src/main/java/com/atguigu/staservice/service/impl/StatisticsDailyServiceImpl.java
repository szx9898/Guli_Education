package com.atguigu.staservice.service.impl;

import com.atguigu.staservice.client.UcenterClient;
import com.atguigu.staservice.entity.StatisticsDaily;
import com.atguigu.staservice.mapper.StatisticsDailyMapper;
import com.atguigu.staservice.service.StatisticsDailyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author zzxx
 * @since 2020-05-18
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public void registerCount(String day) {
        //删除相同天数日期数据
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated", day);
        baseMapper.delete(wrapper);
        Integer countRegister = (Integer) ucenterClient.countRegister(day).getData().get("countRegister");
        //添加到数据库统计分析表
        StatisticsDaily sta = new StatisticsDaily();
        sta.setRegisterNum(countRegister);//注册人数
        sta.setDateCalculated(day);//统计日期

        sta.setCourseNum(RandomUtils.nextInt(100, 200));
        sta.setLoginNum(RandomUtils.nextInt(100, 200));
        sta.setVideoViewNum(RandomUtils.nextInt(100, 200));

        baseMapper.insert(sta);
    }

    @Override
    public Map<String, Object> getShowData(String type, String begin, String end) {
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.between("date_calculated", begin, end);
        wrapper.select("date_calculated", type);
        List<StatisticsDaily> staList = baseMapper.selectList(wrapper);

        List<String> date_calculatedList = staList.stream().map(StatisticsDaily::getDateCalculated).collect(Collectors.toList());
        List<Integer> numDateList = new ArrayList<>();
        staList.forEach(sta -> {
            if (type.equals("login_num")) numDateList.add(sta.getLoginNum());
            if (type.equals("register_num")) numDateList.add(sta.getRegisterNum());
            if (type.equals("video_view_num")) numDateList.add(sta.getVideoViewNum());
            if (type.equals("course_num")) numDateList.add(sta.getCourseNum());
        });


        Map<String, Object> map = new HashMap<>();
        map.put("numDateList", numDateList);
        map.put("date_calculatedList", date_calculatedList);

        return map;
    }
}
