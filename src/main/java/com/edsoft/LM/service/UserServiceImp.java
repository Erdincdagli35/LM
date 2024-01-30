package com.edsoft.LM.service;

import com.edsoft.LM.UserRepository.UserRepository;
import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public Long singUp(User user) {
        user = userRepository.save(user);
        return user.getId();
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findOneById(userId);
    }

    @Override
    public Long delete(Long userId) {
        userRepository.delete(getById(userId));
        return userId;
    }

    @Override
    public User changePassword(UserPasswordChangePojo userPasswordChangePojo) {
        User user = userRepository.findOneByName(userPasswordChangePojo.name); //DB User
        user.setPassword(userPasswordChangePojo.newPassword);
        User newUser = userRepository.save(user);
        return newUser;
    }

    @Override
    public User login(User user) {
        return user;
    }
}
