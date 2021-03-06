package org.example.dao.impl;

import org.example.dao.AddressDao;
import org.example.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDaoImpl extends BaseDaoImpl<Address, String> implements AddressDao {

    private final String tableName = "address";

    @Autowired
    public AddressDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        super.tableName = tableName;
    }
}
