package com.atguigu.eduorder.service;

import com.atguigu.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author zzxx
 * @since 2020-05-17
 */
public interface OrderService extends IService<Order> {

    String createOrder(String courseId, String memberId);
}
