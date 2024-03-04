package com.edsoft.LM.repository;

import com.edsoft.LM.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneById(Long id);

    User findOneByName(String name);
}
