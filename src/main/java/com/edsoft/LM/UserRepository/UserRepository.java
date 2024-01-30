package com.edsoft.LM.UserRepository;

import com.edsoft.LM.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneById(Long id);

    User findOneByName(String name);

    List<User> findAll();
}
