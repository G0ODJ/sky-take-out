package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品管理相关操作")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 增加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品：{}",dishDTO);
        dishService.addDishWithFlavour(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品数据
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询接口")
    public Result<PageResult> getDishPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据分类id查询菜品（返回的是菜品集合）
     * @param id
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据分类id查询菜品")
    public Result<List<Dish>> getDishList(Long id) {
        log.info("根据分类id：{}查询菜品",id);
        List<Dish> dishList = dishService.getDishList(id);
        return Result.success(dishList);
    }

    /**
     * 根据菜品id查询菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据菜品id查询菜品")
    public Result<DishVO> getDishById(@PathVariable Long id) {
        log.info("查询菜品id为;{}",id);
        DishVO dishVO = dishService.getDishById(id);
        return Result.success(dishVO);
    }

    /**
     * 菜品起售和停售
     * @param status,id
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售和停售")
    public Result startOrStop(@PathVariable Integer status,Long id) {
        log.info("菜品id：{}状态变为{}",id,status);
        dishService.startOrStop(status,id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation(value = "修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息为：{}",dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation(value = "批量删除菜品")
    public Result deleteDishBatch(@RequestParam List<Long> ids) {
        log.info("批量删除菜品的id集合为：{}",ids);
        dishService.deleteDishBatch(ids);
        return Result.success();
    }

}
