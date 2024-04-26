/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Account;

/**
 *
 * @author Afiq Zafry
 */
import java.util.List;

public class User {
    private String email;
    private String username;
    private String password;
    private String role;
    private User parent;
    private List<User> children;
    private Double [] locationCoordinate;
    private Integer currentPoints;

    public User(String email, String username, String password, String role, Double[] locationCoordinate) { // Constructor for Teacher
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.locationCoordinate = locationCoordinate;
    }

    public User(String email, String username, String password, String role, List<User> children, Double[] locationCoordinate) { // Constructor for Parent
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
            this.parent=parent;
        } else {
            throw new IllegalStateException("Access denied. Parent only accessible for Young Students.");
        }
    }

    public void setChildren(List<User> children) {
        if ("Parent".equals(role)) {
            this.children=children;
        } else {
            throw new IllegalStateException("Access denied. Children only accessible for Parents.");
        }
    }

    public void setCurrentPoints(Integer currentPoints) {
        if ("Young Student".equals(role)) {
            this.currentPoints=currentPoints;
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

    
}

