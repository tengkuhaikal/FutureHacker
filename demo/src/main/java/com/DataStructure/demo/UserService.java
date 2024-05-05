package com.DataStructure.demo;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    public User registeredUser(String email, String password, String username, String role) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty() || username == null || username.isEmpty() || role == null || role.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setUsername(username);
            user.setRole(role);

            return userRepository.save(user);
    }
}
