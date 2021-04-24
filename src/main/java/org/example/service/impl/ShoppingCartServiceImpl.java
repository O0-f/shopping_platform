package org.example.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.dao.ShoppingCartDao;
import org.example.dto.ShoppingCartDto;
import org.example.entity.ShoppingCart;
import org.example.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartDao shoppingCartDao;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao) {
        this.shoppingCartDao = shoppingCartDao;
    }

    @Override
    public void save(Map<String, Object> params) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setName("cart_" + RandomStringUtils.randomAlphanumeric(15));
        shoppingCart.setUser_id((String) params.get("user_id"));
        shoppingCart.setCommodity_id((String) params.get("commodity_id"));
        shoppingCart.setQuantity((Long) params.get("quantity"));
        shoppingCartDao.saveOrUpdate(shoppingCart);
    }

    @Override
    public void delete(String name) {
        shoppingCartDao.delete("name", name);
    }

    @Override
    public void update(String name, long quantity) {
        List<ShoppingCart> shoppingCarts = shoppingCartDao.find("name", name);
        for (ShoppingCart shoppingCart: shoppingCarts) {
            shoppingCart.setQuantity(quantity);
            shoppingCartDao.saveOrUpdate(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCartDto> find(String userId) {
        String sql = "SELECT * FROM shopping_cart_view WHERE user_id = ?";
        return shoppingCartDao.query(sql, ShoppingCartDto.class, userId);
    }
}
