package com.mawe.spring_jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mawe.spring_jwt.model.Role;
import com.mawe.spring_jwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String username);

    User findByRole(Role admin);

}
