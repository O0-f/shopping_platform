package org.example.dao.impl;

import org.example.dao.OrderDao;
import org.example.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDaoImpl extends BaseDaoImpl<Order, String> implements OrderDao {

    private final String tableName = "order";

    @Autowired
    public OrderDaoImpl() {
        super.tableName = tableName;
    }
}
