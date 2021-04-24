package org.example.service;

import org.example.dto.CommodityDto;

import java.util.List;
import java.util.Map;

public interface CommodityService {

    void save(Map<String, Object> params);

    void delete(String id);

    void update(String id, String column, Object object) throws Exception;

    List<String> getTypes();

    String getDetails(String id);

    List<CommodityDto> find(String type);

    List<CommodityDto> findAll();
}
