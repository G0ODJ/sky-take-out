package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.ArrayList;

public interface ShoppingCartService {

    void add(ShoppingCartDTO shoppingCartDTO);

    ArrayList<ShoppingCart> shoppingCartList();

    void sub(ShoppingCartDTO shoppingCartDTO);

    void cleanShoppingCart();
}
