package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.util.List;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 根据id查询订单详情
     * @param id
     * @return
     */
    OrderVO getOrderDetail(Long id);

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery(int page, int pageSize, Integer status);

    /**
     * 用户取消订单
     * @param id
     */
    void userCancal(Long id);

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 管理端订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void adminCancal(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    void reminder(Long id);
}
