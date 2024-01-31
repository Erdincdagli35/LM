package com.edsoft.LM.mapping;

import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserReturnPojo;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapping {
    public List<UserReturnPojo> mapUsersToUserReturnsPojo(List<User> users) {
        return users.stream()
                .map(user -> {
                    UserReturnPojo userReturnPojo = new UserReturnPojo();
                    userReturnPojo.setName(user.getName());
                    userReturnPojo.setToken(user.getJwtToken());
                    return userReturnPojo;
                })
                .collect(Collectors.toList());
    }

    public UserReturnPojo mapUserToUserReturnPojo(User user) {
        UserReturnPojo userReturnPojo = new UserReturnPojo();
        userReturnPojo.setName(user.getName());
        userReturnPojo.setToken(user.getJwtToken());
        return userReturnPojo;
    }
}
