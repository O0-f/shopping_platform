package org.example.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.dao.CommodityDao;
import org.example.dto.CommodityDto;
import org.example.entity.Commodity;
import org.example.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CommodityServiceImpl implements CommodityService {

    private final CommodityDao commodityDao;

    @Autowired
    public CommodityServiceImpl(CommodityDao commodityDao) {
        this.commodityDao = commodityDao;
    }

    @Override
    public void save(Map<String, Object> params) {
        Commodity commodity = new Commodity();
        commodity.setId("commodityId_" + RandomStringUtils.randomNumeric(8));
        commodity.setType((String) params.get("type"));
        commodity.setName((String) params.get("name"));
        commodity.setPrice((Double) params.get("price"));
        commodity.setQuantity((Long) params.get("quantity"));
        commodity.setViews((Long) params.get("views"));
        commodity.setDetails((String) params.get("details"));
        commodity.setImg((String) params.get("img"));
        commodityDao.saveOrUpdate(commodity);
    }

    @Override
    public void delete(String id) {
        commodityDao.deleteById(id);
    }

    @Override
    public void update(String id, String column, Object object) throws Exception {
        Commodity commodity = commodityDao.findById(id);
        switch (column) {
            case "type": commodity.setType((String) object);break;
            case "name": commodity.setName((String) object);break;
            case "price": commodity.setPrice((Double) object);break;
            case "quantity": commodity.setQuantity((Long) object);break;
            case "views": commodity.setViews((Long) object);break;
            case "details": commodity.setDetails((String) object);break;
            case "img": commodity.setImg((String) object);break;
            default: throw new Exception("Column name error.");
        }
        commodityDao.saveOrUpdate(commodity);
    }

    @Override
    public List<String> getTypes() {
        String sql = "SELECT DISTINCT type FROM commodity";
        List<Map<String, Object>> types = commodityDao.query(sql);
        List<String> result = new ArrayList<>();
        for (Map<String, Object> type: types) {
            result.add((String) type.get("type"));
        }
        return result;
    }

    @Override
    public Long getViews(String id) {
        return commodityDao.findById(id).getViews();
    }

    @Override
    public String getDetails(String id) {
        return commodityDao.findById(id).getDetails();
    }

    @Override
    public List<CommodityDto> search(String name) {
        String sql = "SELECT * FROM commodity_view WHERE name LIKE ?";
        return commodityDao.query(sql, CommodityDto.class, "%" + name + "%");
    }

    @Override
    public List<CommodityDto> find(String type) {
        String sql = "SELECT * FROM commodity_view WHERE type = ?";
        return commodityDao.query(sql, CommodityDto.class, type);
    }

    @Override
    public List<CommodityDto> findAll() {
        String sql = "SELECT * FROM commodity_view";
        return commodityDao.query(sql, CommodityDto.class);
    }
}
