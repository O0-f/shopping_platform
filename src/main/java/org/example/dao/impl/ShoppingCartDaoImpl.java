package org.example.dao.impl;

import org.example.dao.ShoppingCartDao;
import org.example.entity.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShoppingCartDaoImpl extends BaseDaoImpl<ShoppingCart, String> implements ShoppingCartDao {

    private final String tableName = "shopping_cart";

    @Autowired
    public ShoppingCartDaoImpl() {
        super.tableName = tableName;
    }
}
