package com.example.eback.dao;

import com.example.eback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserDAO extends JpaRepository<User,Integer> {
    User findByUsername(String username);
    List<User> findAll();
    boolean existsByName(String name);
}
