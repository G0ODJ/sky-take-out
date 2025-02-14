package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    public void add(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 在mapper层修改数据库数据时更新了setmeal的id
        setmealMapper.insert(setmeal);
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealId);
        }
        // 同时更新，不在遍历中重复调用数据库插入语句
        setmealDishMapper.insertBatch(setmealDTO.getSetmealDishes());
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        int page = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        PageHelper.startPage(page, pageSize);
        // 响应的数据中有套餐的分类名称，其在分类表中存在，因此需要使用SetmealVO对象，其中包括分类名称
        Page<SetmealVO> pageSetmealVO = (Page<SetmealVO>)setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(pageSetmealVO.getTotal(),pageSetmealVO.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    @Override
    public void deleteSetmeals(List<Long> ids) {
        int count = setmealMapper.countIfStart(ids);
        if(count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }else{
            setmealMapper.deleteBatch(ids);
        }
    }

    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);
        // 将关联原套餐的菜品删除，在添加新的套餐菜品数据
        Long setmealId = setmeal.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);
        // 再添加新的套餐菜品数据
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishMapper.insertBatch(setmealDishes);
    }

    @Override
    public Setmeal selectById(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        return setmeal;
    }

    /**
     * 根据id查询套餐和套餐菜品关系
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = setmealMapper.getByIdWithDish(id);
        return setmealVO;
    }


    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

}
