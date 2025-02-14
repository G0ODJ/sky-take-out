package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 统计指定时间内的营业额
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // select sum(amount) from orders where order_time > ... and order_time < ... and status = 5
            LocalDateTime begintime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endtime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", begintime);
            map.put("end", endtime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            // 营业额非空判断
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }

        // 转换成string数据格式
        String stringDateList = StringUtils.join(dateList, ",");
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();
        turnoverReportVO.setDateList(stringDateList);
        turnoverReportVO.setTurnoverList(StringUtils.join(turnoverList, ","));

        return turnoverReportVO;
    }

    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        // 查询用户表 select count(id) from user where create_time > xx and ...
        // 每日新增用户数据
        List<Integer> newUserList = new ArrayList<>();
        // 当日累计用户数据
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            // select sum(amount) from orders where order_time > ... and order_time < ... and status = 5
            LocalDateTime begintime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endtime = LocalDateTime.of(date, LocalTime.MAX);
            Map map = new HashMap();

            map.put("end", endtime);
            Integer totalUser = userMapper.sumByDate(map);
            // 全部用户非空判断
            totalUser = totalUser == null ? 0 : totalUser;
            totalUserList.add(totalUser);

            map.put("begin", begintime);
            Integer newUser = userMapper.sumByDate(map);
            // 新增用户非空判断
            newUser = newUser == null ? 0 : newUser;
            newUserList.add(newUser);


        }

        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totalUserList,",")).build();
    }

    public List<LocalDate> getDateList(LocalDate begin, LocalDate end){
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
