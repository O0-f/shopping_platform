package org.example.service;

import org.example.entity.Store;

import java.util.List;
import java.util.Map;

public interface StoreService {

    void save(Map<String, Object> params);

    void delete(String id);

    void update(Store store);

    Store find(String id);

    List<Store> findAll();
}
