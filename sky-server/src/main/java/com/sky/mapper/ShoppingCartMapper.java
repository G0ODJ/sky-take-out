package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车中是否有数据
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 修改商品数量
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number}  where id = #{id} ")
    void updateNumber(ShoppingCart shoppingCart);


    /**
     * 插入购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart(name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time, number) " +
            "values(#{name},#{image},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime},#{number})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据菜品id查询购物车信息
     * @param dishId
     * @return
     */
    @Select("select * from shopping_cart sc where dish_id = #{dishId}")
    ShoppingCart getByDishId(Long dishId);

    /**
     * 根据购物车id删除item
     * @param id
     */

    @Delete("delete from shopping_cart where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据套餐id查询
     * @param setmealId
     * @return
     */
    @Select("select * from shopping_cart sc where setmeal_id = #{setmealId}")
    ShoppingCart getBySetmealId(Long setmealId);

    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void cleanShoppingCart(Long userId);

    /**
     * 批量插入购物车
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
