package org.example.dao.impl;

import org.example.dao.StoreDao;
import org.example.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StoreDaoImpl extends BaseDaoImpl<Store, String> implements StoreDao {

    private final String tableName = "store";

    @Autowired
    public StoreDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        super.tableName = tableName;
    }
}
