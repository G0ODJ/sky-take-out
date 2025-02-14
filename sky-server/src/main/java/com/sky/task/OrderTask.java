package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component //实例化
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理订单支付超时
     */
    @Scheduled(cron = "0 * * * * ?") //每分钟处罚一次
    public void processTimeoutOrder(){
        log.info("定时处理超时订单;{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> outTimeOrders = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,time);

        if(outTimeOrders!=null && outTimeOrders.size()>0){
            for(Orders order:outTimeOrders){
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    @Scheduled(cron = "0 0 1 * * ?") //每天凌晨1点
    public void processDeliveryOrder(){
        log.info("每天凌晨1点处理派送中的订单;{}", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().minusMinutes(60);
        List<Orders> outTimeOrders = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS,time);

        if(outTimeOrders!=null && outTimeOrders.size()>0){
            for(Orders order:outTimeOrders){
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }

}
