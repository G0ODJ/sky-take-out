package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询关联的套餐id
     * @param dishIds
     * @return
     */
    List<Long> selectSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐和菜品数据
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 通过套餐id删除套餐菜品关联数据
     * @param setmealId
     */
    void deleteBySetmealId(Long setmealId);
}
