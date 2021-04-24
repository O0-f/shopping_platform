package org.example.service.impl;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.dao.OrderDao;
import org.example.dto.OrderDto;
import org.example.entity.Order;
import org.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void save(Multimap<String, Object> params) {
        Order order = new Order();
        order.setName("order_" + RandomStringUtils.randomAlphanumeric(14));
        order.setStore_id((String) params.get("store_id").toArray()[0]);
        order.setAddress_id((String) params.get("address_id").toArray()[0]);
        order.setTime((Timestamp) params.get("time").toArray()[0]);
        order.setStatus((String) params.get("status").toArray()[0]);
        List<Object> commodity = Arrays.asList(params.get("commodity_id").toArray());
        List<Object> quantity = Arrays.asList(params.get("quantity").toArray());
        List<Object> price = Arrays.asList(params.get("price").toArray());
        int n = commodity.size();
        for (int i = 0; i < n; i++) {
            order.setCommodity_id((String) commodity.get(i));
            order.setQuantity((Long) quantity.get(i));
            order.setPrice((Double) price.get(i));
            orderDao.saveOrUpdate(order);
        }
    }

    @Override
    public void delete(String name) {
        orderDao.delete("name", name);
    }

    @Override
    public void update(String name, String status) {
        List<Order> orders = orderDao.find("name", name);
        for (Order order: orders) {
            order.setStatus(status);
            orderDao.saveOrUpdate(order);
        }
    }

    @Override
    public List<Multimap<String, Object>> find(String userId) {
        String sql = "SELECT DISTINCT order_name FROM order_view WHERE user_id = ?";
        List<Map<String, Object>> orderNameMaps = orderDao.query(sql, userId);
        List<String> orderNames = new ArrayList<>();
        for (Map<String, Object> orderNameMap: orderNameMaps) {
            orderNames.add((String) orderNameMap.get("order_name"));
        }
        sql = "SELECT * FROM order_view WHERE order_name = ?";
        List<Multimap<String, Object>> result = new ArrayList<>();
        for (String orderName: orderNames) {
            List<OrderDto> orderDtos = orderDao.query(sql, OrderDto.class, orderName);
            Multimap<String, Object> multimap = HashMultimap.create();
            if (orderDtos.size() > 0) {
                multimap.put("user_id", orderDtos.get(0).getUser_id());
                multimap.put("order_name", orderDtos.get(0).getOrder_name());
                for (OrderDto orderdto: orderDtos) {
                    multimap.put("commodity_name", orderdto.getCommodity_name());
                    multimap.put("quantity", orderdto.getQuantity());
                    multimap.put("price", orderdto.getPrice());
                }
                multimap.put("store_name", orderDtos.get(0).getStore_name());
                multimap.put("address_name", orderDtos.get(0).getAddress_name());
                multimap.put("time", orderDtos.get(0).getTime());
                multimap.put("status", orderDtos.get(0).getStatus());
            } else {
                multimap.put("user_id", null);
                multimap.put("order_name", null);
                multimap.put("commodity_name", null);
                multimap.put("quantity", null);
                multimap.put("price", null);
                multimap.put("store_name", null);
                multimap.put("address_name", null);
                multimap.put("time", null);
                multimap.put("status", null);
            }
            result.add(multimap);
        }
        return result;
    }

    @Override
    public List<OrderDto> findAll() {
        String sql = "SELECT * FROM order_view";
        return orderDao.query(sql, OrderDto.class);
    }
}
