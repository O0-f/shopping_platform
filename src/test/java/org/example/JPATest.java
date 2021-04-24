package org.example;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.example.dao.AddressDao;
import org.example.dao.OrderDao;
import org.example.dao.UserDao;
import org.example.dto.OrderDto;
import org.example.dto.ShoppingCartDto;
import org.example.entity.Address;
import org.example.entity.Order;
import org.example.entity.User;
import org.example.service.CommodityService;
import org.example.service.OrderService;
import org.example.service.ShoppingCartService;
import org.example.service.UserService;
import org.example.utils.EncryptUtils;
import org.example.utils.JPAUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-mvc.xml", "classpath*:spring-hibernate.xml", "classpath*:spring-ehcache.xml"})
@WebAppConfiguration
public class JPATest {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    private final EntityManager entityManager = JPAUtils.getEntityManager();

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        Address address = entityManager.find(Address.class, "id_test0");
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
        String sql = "SELECT * FROM `order`";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
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
    public void daoUpdate() throws Exception {
        Address address = new Address();
        address.setId("id_test");
        address.setUser_id("user_id_test");
        address.setName("g3333");
        address.setLng("666621");
        address.setLat("554");
        addressDao.saveOrUpdate(address);
        User user = userDao.findById("user_id_test");
        user.setPassword(EncryptUtils.SHAEncrypt("111"));
        userDao.saveOrUpdate(user);
        boolean flag = EncryptUtils.SHACompare(EncryptUtils.SHAEncrypt("111"), userDao.findById("user_id_test").getPassword());
        System.out.println("Success");
    }

    @Test
    public void daoFind() {
        Address address = addressDao.findById("id_test");
        List<User> user = userDao.find("name", "test");
        Order order = orderDao.findById("order_id_test");
        System.out.println(address.getName());
    }

    @Test
    public void daoFindAll() {
        List<Address> addressList = addressDao.findAll();
        List<User> userList = userDao.findAll();
        System.out.println("Success");
    }

    @Test
    public void daoQuery() {
        String sql = "SELECT * FROM order_view WHERE user_id = 'user_id_test'";
        List<OrderDto> orderDtos = orderDao.query(sql, OrderDto.class);
        System.out.println("Success");
    }

    @Test
    public void serviceSave() {
        User user = new User();
        user.setId("user_id_test0");
        user.setType("user");
        user.setName("hhhhhh");
        user.setPhone("666666");
        user.setPassword("000000");
        userService.save(user);
        Multimap<String, Object> multimap = HashMultimap.create();
        multimap.put("store_id", "store_id_test");
        multimap.put("address_id", "id_test");
        multimap.put("time", new Timestamp(System.currentTimeMillis()));
        multimap.put("status", "154xxx");
        multimap.put("commodity_id", "commodity_id_test");
        multimap.put("quantity", 10L);
        multimap.put("price", 26.6);
        multimap.put("commodity_id", "commodity_id_test0");
        multimap.put("quantity", 20L);
        multimap.put("price", 64.0);
        orderService.save(multimap);
        Map<String, Object> cartParams = new HashMap<>();
        cartParams.put("user_id", "user_id_test");
        cartParams.put("commodity_id", "commodity_id_test");
        cartParams.put("quantity", 5L);
        shoppingCartService.save(cartParams);
    }

    @Test
    public void serviceDelete() {
        userService.delete("user_id_test0");
        orderService.delete("order_test");
    }

    @Test
    public void serviceUpdate() throws Exception {
        userService.update("user_id_test0", "phone", "99999999");
        orderService.update("order_test", "kkkk");
    }

    @Test
    public void serviceFind() {
        User user = userService.find("id", "user_id_test0");
        List<Multimap<String, Object>> multimaps = orderService.find("user_id_test");
        List<ShoppingCartDto> shoppingCartDtos = shoppingCartService.find("user_id_test");
        List<String> types = commodityService.getTypes();
        System.out.println("Success");
    }
}
