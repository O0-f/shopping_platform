package org.example.dao.impl;

import org.example.dao.CommodityDao;
import org.example.entity.Commodity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CommodityDaoImpl extends BaseDaoImpl<Commodity, String> implements CommodityDao {

    private final String tableName = "commodity";

    @Autowired
    public CommodityDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        super.tableName = tableName;
    }
}
