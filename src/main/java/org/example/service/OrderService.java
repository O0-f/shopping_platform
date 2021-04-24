package org.example.service;

import com.google.common.collect.Multimap;
import org.example.dto.OrderDto;

import java.util.List;

public interface OrderService {

    void save(Multimap<String, Object> params);

    void delete(String name);

    void update(String name, String status);

    List<Multimap<String, Object>> find(String userId);

    List<OrderDto> findAll();
}
