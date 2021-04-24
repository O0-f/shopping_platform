package org.example.service.impl;

import org.example.dao.UserDao;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void save(User user) {
        userDao.saveOrUpdate(user);
    }

    @Override
    public void delete(String id) {
        userDao.deleteById(id);
    }

    @Override
    public void update(String id, String column, Object object) throws Exception {
        User user = userDao.findById(id);
        switch (column) {
            case "name": user.setName((String) object);break;
            case "phone": user.setPhone((String) object);break;
            case "password": user.setPassword((String) object);break;
            default: throw new Exception("Column name error.");
        }
        userDao.saveOrUpdate(user);
    }

    @Override
    public User find(String column, Object object) {
        List<User> users = userDao.find(column, object);
        if (users.size() == 0) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public UserDto findView(String id) {
        String sql = "SELECT * FROM user_view WHERE id = ?";
        return userDao.query(sql, UserDto.class, id).get(0);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
