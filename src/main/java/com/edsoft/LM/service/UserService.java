package com.edsoft.LM.service;

import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;
import com.edsoft.LM.pojo.UserReturnPojo;

import java.util.List;

public interface UserService {
    UserReturnPojo singUp(User user);

    List<UserReturnPojo> getAll(String name, String password);

    User getById(Long userId);

    Long delete(Long userId);

    UserReturnPojo changePassword(UserPasswordChangePojo userPasswordChangePojo);

    UserReturnPojo login(User user);
}
