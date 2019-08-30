package com.rsw.service;

import com.rsw.pojo.log.PayLog;
import com.rsw.pojo.order.Order;

public interface OrderService {

    public void add(Order order);

    public PayLog getPayLogByUserName(String userName);

    public void updatePayStatus(String userName);

}
