package com.edsoft.LM.service;

import com.edsoft.LM.mapping.UserMapping;
import com.edsoft.LM.models.User;
import com.edsoft.LM.pojo.UserPasswordChangePojo;
import com.edsoft.LM.pojo.UserReturnPojo;
import com.edsoft.LM.repository.UserRepository;
import com.edsoft.LM.security.JwtUtil;
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

    private final JwtUtil jwtUtil;

    private final UserMapping userMapping;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserReturnPojo singUp(User user) {
        log.info("User signed up with ID: {}", user.getId());
        user.setJwtToken(generateToken(user.getName()));
        user = userRepository.save(user);
        return userMapping.mapUserToUserReturnPojo(user);
    }

    @Override
    public List<UserReturnPojo> getAll(String name, String password) {
        log.info("Getting all users");
        List<User> users = getUsersByCriteria(name, password);
        return userMapping.mapUsersToUserReturnsPojo(users);
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
    public UserReturnPojo changePassword(UserPasswordChangePojo userPasswordChangePojo) {
        log.info("Changing password for user with name: {}", userPasswordChangePojo.getName());
        User user = userRepository.findOneByName(userPasswordChangePojo.getName()); //DB User
        user.setPassword(userPasswordChangePojo.getNewPassword());
        User savedUser = userRepository.save(user);
        return userMapping.mapUserToUserReturnPojo(savedUser);
    }

    @Override
    public UserReturnPojo login(User user) {
        log.info("User logged in with user.name: {}", user.getName());
        return userMapping.mapUserToUserReturnPojo(user);
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

    public String generateToken(String name) {
        return jwtUtil.generateToken(name);
    }
}
