package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    public void addDishWithFlavour(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    List<Dish> getDishList(Long id);

    DishVO getDishById(Long id);

    void startOrStop(Integer status, Long id);

    void update(DishDTO dishDTO);

    void deleteDishBatch(List<Long> ids);
}
