package com.DataStructure.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

import jakarta.persistence.Id;


import java.util.List;
import java.util.Objects;

// import org.springframework.data.annotation.Id;

@Entity
@Table(name = "users_table")
public class User {

    @Id
    private int id;
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private String email;
    private String username;
    private String password;
    private String role;
    private User parent;
    private List<User> children;
    private Double[] locationCoordinate;
    private Integer currentPoints;

    public User(){
        
    }

    // Constructor for Teacher
    public User(String email, String username, String password, String role, Double[] locationCoordinate) { 
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locationCoordinate = locationCoordinate;
    }

    // Constructor for Parent
    public User(String email, String username, String password, String role, List<User> children, Double[] locationCoordinate) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.children = children;
        this.locationCoordinate = locationCoordinate;
    }

    public User(String email, String username, String password, String role, User parent, Double[] locationCoordinate, Integer currentPoints) { // Constructor for student
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.parent = parent;
        this.locationCoordinate = locationCoordinate;
        this.currentPoints = currentPoints;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double[] getLocationCoordinate() {
        return locationCoordinate;
    }

    public void setLocationCoordinate(Double[] locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    public void setParent(User parent) {
        if ("Young Student".equals(role)) {
            this.parent = parent;
        } else {
            throw new IllegalStateException("Access denied. Parent only accessible for Young Students.");
        }
    }

    public void setChildren(List<User> children) {
        if ("Parent".equals(role)) {
            this.children = children;
        } else {
            throw new IllegalStateException("Access denied. Children only accessible for Parents.");
        }
    }

    public void setCurrentPoints(Integer currentPoints) {
        if ("Young Student".equals(role)) {
            this.currentPoints = currentPoints;
        } else {
            throw new IllegalStateException("Access denied. Current points only accessible for Young Students.");
        }
    }

    public Integer getCurrentPoints() {
        if ("Young Student".equals(role)) {
            return currentPoints;
        } else {
            throw new IllegalStateException("Access denied. Current points only accessible for Young Students.");
        }
    }

    public List<User> getChildren() {
        if ("Parent".equals(role)) {
            return children;
        } else {
            throw new IllegalStateException("Access denied. Children only accessible for Parents.");
        }
    }

    public User getParent() {
        if ("Young Student".equals(role)) {
            return parent;
        } else {
            throw new IllegalStateException("Access denied. Parent only accessible for Young Students.");
        }
    }

    @Override
    public boolean equals(Object a) {
        if (this == a) {
            return true;
        }

        if (a == null || getClass() != a.getClass()) {
            return false;
        }

        User that = (User) a;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode(){
        return Objects.hash(email, password, username);
    }

    @Override
    public String toString(){
        return "User{" +
                "E-Mail: " + email + 
                ", Username: " + username + "}";
    }
}
