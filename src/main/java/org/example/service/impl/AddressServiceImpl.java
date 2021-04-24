package org.example.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.dao.AddressDao;
import org.example.entity.Address;
import org.example.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    public void save(Map<String, Object> params) {
        Address address = new Address();
        address.setId("addressId_" + RandomStringUtils.randomNumeric(10));
        address.setUser_id((String) params.get("user_id"));
        address.setName((String) params.get("name"));
        address.setLng((String) params.get("lng"));
        address.setLat((String) params.get("lat"));
        addressDao.saveOrUpdate(address);
    }

    @Override
    public void delete(String id) {
        addressDao.deleteById(id);
    }

    @Override
    public void update(Address address) {
        addressDao.saveOrUpdate(address);
    }

    @Override
    public List<Address> find(String userId) {
        return addressDao.find("user_id", userId);
    }
}
