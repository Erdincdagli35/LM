package com.edsoft.LM.service;

import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;

import java.util.List;

public interface UserService {
    Long singUp(User user);

    List<User> getAll(String name, String password);

    User getById(Long userId);

    Long delete(Long userId);

    User changePassword(UserPasswordChangePojo userPasswordChangePojo);

    User login(User user);
}
