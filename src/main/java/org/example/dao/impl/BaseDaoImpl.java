package org.example.dao.impl;

import org.example.dao.BaseDao;
import org.example.utils.JPAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

    private Class<T> entityClass;
    private final EntityManager entityManager;
    private final EntityTransaction entityTransaction;
    private final JdbcTemplate jdbcTemplate;
    protected String tableName;

    @Autowired
    public BaseDaoImpl(JdbcTemplate jdbcTemplate) {
        entityManager = JPAUtils.getEntityManager();
        entityTransaction = entityManager.getTransaction();
        this.jdbcTemplate = jdbcTemplate;
        Class<?> getClass = getClass();
        Type type = getClass.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
            entityClass = (Class<T>) parameterizedType[0];
        }
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected EntityTransaction getEntityTransaction() {
        return entityTransaction;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public void saveOrUpdate(T entity) {
        Assert.notNull(entity, "Entity is required.");
        getEntityTransaction().begin();
        getEntityManager().merge(entity);
        getEntityManager().flush();
        getEntityTransaction().commit();
    }

    @Override
    public void deleteById(ID id) {
        Assert.notNull(id, "Id is required.");
        getEntityTransaction().begin();
        T entity = getEntityManager().find(getEntityClass(), id);
        getEntityManager().remove(entity);
        getEntityManager().flush();
        getEntityTransaction().commit();
    }

    @Override
    public void delete(String column, Object object) {
        Assert.notNull(column, "Column is required.");
        Assert.notNull(object, "Object is required.");
        List<T> list = find(column, object);
        getEntityTransaction().begin();
        for (T entity: list) {
            getEntityManager().remove(getEntityManager().contains(entity)? entity: getEntityManager().merge(entity));
            getEntityManager().flush();
        }
        getEntityTransaction().commit();
    }

    @Override
    public T findById(ID id) {
        Assert.notNull(id, "Id is required.");
        return getEntityManager().find(getEntityClass(), id);
    }

    @Override
    public List<T> find(String column, Object object) {
        Assert.notNull(column, "Column is required.");
        Assert.notNull(object, "Object is required.");
        String sql = "SELECT * FROM " + tableName + " WHERE " + column + " = ?";
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(getEntityClass()), object);
    }

    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(getEntityClass()));
    }

    @Override
    public List<Map<String, Object>> query(String sql, Object... args) {
        return getJdbcTemplate().queryForList(sql, args);
    }

    @Override
    public <E> List<E> query(String sql, Class<E> classObject, Object... args) {
        return getJdbcTemplate().query(sql, new BeanPropertyRowMapper<>(classObject), args);
    }
}
