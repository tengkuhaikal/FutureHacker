package com.DataStructure.demo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User>  findByUsernameAndPassword(String username, String password);
    User findByUsername(String username);
}
