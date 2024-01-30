package com.edsoft.LM.service;

import com.edsoft.LM.UserRepository.UserRepository;
import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long singUp(User user) {
        log.info("User signed up with ID: {}", user.getId());
        user = userRepository.save(user);
        return user.getId();
    }

    public List<User> getUsersByCriteria(String name, String password) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null) {
            predicates.add(builder.equal(root.get("name"), name));
        }

        if (password != null) {
            predicates.add(builder.equal(root.get("password"), password));
        }

        criteriaQuery.where(builder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<User> getAll(String name, String password) {
        log.info("Getting all users");
        List<User> filteredUsers = getUsersByCriteria(name, password);
        return filteredUsers;
    }

    @Override
    public User getById(Long userId) {
        log.info("Getting user by ID: {}", userId);
        return userRepository.findOneById(userId);
    }

    @Override
    public Long delete(Long userId) {
        log.info("Deleting user with ID: {}", userId);
        userRepository.delete(getById(userId));
        return userId;
    }

    @Override
    public User changePassword(UserPasswordChangePojo userPasswordChangePojo) {
        log.info("Changing password for user with name: {}", userPasswordChangePojo.name);
        User user = userRepository.findOneByName(userPasswordChangePojo.name); //DB User
        user.setPassword(userPasswordChangePojo.newPassword);
        User newUser = userRepository.save(user);
        return newUser;
    }

    @Override
    public User login(User user) {
        log.info("User logged in with name: {}", user.getName());
        return user;
    }
}
