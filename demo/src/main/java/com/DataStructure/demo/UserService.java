package com.DataStructure.demo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
   

    public User registeredUser(String email, String username, String password, String role) {
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
    
    public User login(String email, String password){
        return userRepository.findByUsernameAndPassword(email, password).orElse(null);
    }
    
    public void addParentChild(String parentUsername, String childUsername) {
        User parent = userRepository.findByUsername(parentUsername);
        User child = userRepository.findByUsername(childUsername);

        if (parent != null && child != null) {
            child.setParent(parent);
            parent.getChildren().add(child);
            userRepository.save(child);
            userRepository.save(parent);
        }
    }
    
    public void removeParentChild(String parentUsername, String childUsername) {
        User parent = userRepository.findByUsername(parentUsername);
        User child = userRepository.findByUsername(childUsername);

        if (parent != null && child != null) {
            child.setParent(null);
            parent.getChildren().remove(child);
            userRepository.save(child);
            userRepository.save(parent);
        }
    }
    
    public void populateFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader("ParentChild.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                String parentUsername = parts[0].trim();
                String childUsername = parts[1].trim();

                addParentChild(parentUsername, childUsername);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
}
