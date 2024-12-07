package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavourMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceIml implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavourMapper dishFlavourMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加菜品信息和口味
     *
     * @param dishDTO
     */
    @Override
    public void addDishWithFlavour(DishDTO dishDTO) {
        Dish dish = new Dish();
        // 拷贝属性
        BeanUtils.copyProperties(dishDTO, dish);
        // mapper层处理数据
        dishMapper.insert(dish);

        Long id = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(id);
            }
            // 批量插入
            dishFlavourMapper.insertBatch(flavors);
        }
    }

    /**
     * 分页查询菜品数据
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        int page = dishPageQueryDTO.getPage();
        int pageSize = dishPageQueryDTO.getPageSize();
        // 使用PageHelper来进行分页查询
        PageHelper.startPage(page, pageSize);
        // mapper层进行数据库操作
        Page<DishVO> pagelist = (Page<DishVO>) dishMapper.pageQuery(dishPageQueryDTO);
        // 获取分页查询的total
        long total = pagelist.getTotal();
        // 获取分页查询的数据集合
        List<DishVO> result = pagelist.getResult();

        return new PageResult(total, result);
    }

    /**
     * 根据分类id查询菜品（返回的是菜品集合）
     * @param id
     * @return
     */
    @Override
    public List<Dish> getDishList(Long id) {
        return dishMapper.getDishListByCategoryId(id);
    }

    /**
     * 根据菜品id查询菜品
     * @param id
     * @return
     */
    @Override
    public DishVO getDishById(Long id) {
        Dish dish = dishMapper.getDishById(id);
        List<DishFlavor> flavors = dishFlavourMapper.getFlavorsByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 菜品起售和停售
     * @param status,id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(status);
        dishMapper.update(dish);
    }

    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 修改菜品信息
        dishMapper.update(dish);
        // 先删除关联的菜品口味信息
        List<Long> ids = new ArrayList<>();
        ids.add(dish.getId());
        dishFlavourMapper.deleteByDishIds(ids);
        // 再添加新的口味信息
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 对集合操作，首先判断是否有值
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavourMapper.insertBatch(flavors);
        }


    }

    @Override
    public void deleteDishBatch(List<Long> ids) {
        // 菜品中若有起售中的则不能删除
        for (Long id : ids) {
            Dish dish = dishMapper.getDishById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 菜品如果关联套餐，则不能删除
        List<Long> seatmealIds = setmealDishMapper.selectSetmealIdsByDishIds(ids);
        if (seatmealIds != null && seatmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.deleteDishBatch(ids);
        dishFlavourMapper.deleteByDishIds(ids);// 关联菜品的口味也要删除
    }
}
