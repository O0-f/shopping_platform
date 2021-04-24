package org.example.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseDao<T, ID extends Serializable> {

    void saveOrUpdate(T entity);

    void deleteById(ID id);

    void delete(String column, Object object);

    T findById(ID id);

    List<T> find(String column, Object object);

    List<T> findAll();

    List<Map<String, Object>> query(String sql, Object... args);

    <E> List<E> query(String sql, Class<E> classObject, Object... args);
}
