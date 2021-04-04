package org.example;

import org.example.dao.AddressDao;
import org.example.dao.UserDao;
import org.example.entity.Address;
import org.example.entity.User;
import org.example.utils.JPAUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-mvc.xml", "classpath*:spring-hibernate.xml", "classpath*:spring-ehcache.xml"})
@WebAppConfiguration
public class JPATest {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private UserDao userDao;

    private EntityManager entityManager = JPAUtils.getEntityManager();

    @Test
    public void save() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Address address = new Address();
        address.setId("id_test0");
        address.setUser_id("user_id_test");
        address.setName("ghg");
        address.setLng("666");
        address.setLat("333");
        entityManager.merge(address);
        entityManager.flush();
        entityTransaction.commit();
        Address addressNew = entityManager.find(Address.class, "id_test0");
        System.out.println(addressNew.getName());
    }

    @Test
    public void delete() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Address address = entityManager.find(Address.class, "id_test0");;
        entityManager.remove(address);
        entityManager.flush();
        entityTransaction.commit();
    }

    @Test
    public void update() {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        Address address = new Address();
        address.setId("id_test");
        address.setUser_id("user_id_test");
        address.setName("g2222");
        address.setLng("66651");
        address.setLat("3444");
        entityManager.merge(address);
        entityManager.flush();
        entityTransaction.commit();
    }

    @Test
    public void find() {
        Address address = entityManager.find(Address.class, "id_test");
        System.out.println(address.getName());
    }

    @Test
    public void sql() {
        String sql = "SELECT * FROM address";
        Query query = entityManager.createNativeQuery(sql);
        List list = query.getResultList();
        System.out.println("Success");
    }

    @Test
    public void daoSave() {
        Address address = new Address();
        address.setId("id_test0");
        address.setUser_id("user_id_test");
        address.setName("ghg");
        address.setLng("666");
        address.setLat("333");
        addressDao.saveOrUpdate(address);
    }

    @Test
    public void daoDelete() {
        addressDao.deleteById("id_test0");
    }

    @Test
    public void daoUpdate() {
        Address address = new Address();
        address.setId("id_test");
        address.setUser_id("user_id_test");
        address.setName("g3333");
        address.setLng("666621");
        address.setLat("554");
        addressDao.saveOrUpdate(address);
    }

    @Test
    public void daoFind() {
        Address address = addressDao.findById("id_test");
        User user = userDao.findById("user_id_test");
        System.out.println(address.getName());
        System.out.println(user.getName());
    }

    @Test
    public void daoFindAll() {
        List addressList = addressDao.findAll();
        List userList = userDao.findAll();
        System.out.println("Success");
    }
}
