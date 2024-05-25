/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DataStructure.demo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")

public class AccessManager {
    private static final String EDUCATOR_ROLE = "Educator";
    private static final String PARENT_ROLE = "Parent";
    private static final String YOUNG_STUDENT_ROLE = "Young Student";

    @GetMapping("/event")
    public boolean canAccessEventPage(User user) {
        return true; // All roles can access Event Page
    }

    @GetMapping("/profile")
    public boolean canAccessViewProfilePage(User user) {
        return true; // All roles can access View Personal/Others' Profile
    }

    @GetMapping("/discussion")
    public boolean canAccessDiscussionPage(User user) {
        return true; // All roles can access Discussion Page
    }

    @GetMapping("/create-event")
    public boolean canAccessCreateEventPage(User user) {
        return user.getRole().equals(EDUCATOR_ROLE);
    }

    @GetMapping("/create-quiz")
    public boolean canAccessCreateQuizPage(User user) {
        return user.getRole().equals(EDUCATOR_ROLE);
    }

    @GetMapping("/booking")
    public boolean canAccessBookingPage(User user) {
        return user.getRole().equals(PARENT_ROLE);
    }

    @GetMapping("/quiz")
    public boolean canAccessQuizPage(User user) {
        return user.getRole().equals(YOUNG_STUDENT_ROLE);
    }

    @GetMapping("/add-friends")
    public boolean canAccessAddFriendsPage(User user) {
        return user.getRole().equals(YOUNG_STUDENT_ROLE);
    } 
}