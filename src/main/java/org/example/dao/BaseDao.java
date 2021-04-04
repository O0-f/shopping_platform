package org.example.dao;

import java.io.Serializable;
import java.util.List;

public interface BaseDao<T, ID extends Serializable> {

    void saveOrUpdate(T entity);

    void deleteById(ID id);

    T findById(ID id);

    List<T> findAll();

    List query(String sql);
}
