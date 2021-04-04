package org.example.dao.impl;

import org.example.dao.BaseDao;
import org.example.utils.JPAUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("unchecked")
public class BaseDaoImpl<T, ID extends Serializable> implements BaseDao<T, ID> {

    private Class<T> entityClass;
    private final EntityManager entityManager;
    private final EntityTransaction entityTransaction;
    protected String tableName;

    public BaseDaoImpl() {
        entityManager = JPAUtils.getEntityManager();
        entityTransaction = entityManager.getTransaction();
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
        T entity = getEntityManager().find(getEntityClass(), id);;
        getEntityManager().remove(entity);
        getEntityManager().flush();
        getEntityTransaction().commit();
    }

    @Override
    public T findById(ID id) {
        Assert.notNull(id, "Id is required.");
        return getEntityManager().find(getEntityClass(), id);
    }

    @Override
    public List<T> findAll() {
        String sql = "SELECT * FROM " + tableName;
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList();
    }

    @Override
    public List query(String sql) {
        return getEntityManager().createNativeQuery(sql).getResultList();
    }
}
