package org.example.service;

import org.example.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService {

    void save(Map<String, Object> params);

    void delete(String name);

    void update(String name, long quantity);

    List<ShoppingCartDto> find(String userId);
}
