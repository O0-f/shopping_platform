package org.example.service;

import org.example.dto.UserDto;
import org.example.entity.User;

import java.util.List;

public interface UserService {

    void save(User user);

    void delete(String id);

    void update(String id, String column, Object object) throws Exception;

    User find(String column, Object object);

    UserDto findView(String id);

    List<User> findAll();
}
