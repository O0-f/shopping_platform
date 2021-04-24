package org.example.service;

import org.example.entity.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {

    void save(Map<String, Object> params);

    void delete(String id);

    void update(Address address);

    List<Address> find(String userId);
}
