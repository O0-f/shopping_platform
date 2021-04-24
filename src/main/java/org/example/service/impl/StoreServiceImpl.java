package org.example.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.dao.StoreDao;
import org.example.entity.Store;
import org.example.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreDao storeDao;

    @Autowired
    public StoreServiceImpl(StoreDao storeDao) {
        this.storeDao = storeDao;
    }

    @Override
    public void save(Map<String, Object> params) {
        Store store = new Store();
        store.setId("storeId_" + RandomStringUtils.randomNumeric(12));
        store.setName((String) params.get("name"));
        store.setLng((String) params.get("lng"));
        store.setLat((String) params.get("lat"));
        storeDao.saveOrUpdate(store);
    }

    @Override
    public void delete(String id) {
        storeDao.deleteById(id);
    }

    @Override
    public void update(Store store) {
        storeDao.saveOrUpdate(store);
    }

    @Override
    public Store find(String id) {
        return storeDao.findById(id);
    }

    @Override
    public List<Store> findAll() {
        return storeDao.findAll();
    }
}
