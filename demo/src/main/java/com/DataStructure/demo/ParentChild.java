/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.DataStructure.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Hasna
 */
@RestController
@RequestMapping("/users")
public class ParentChild {

    public class UserController {

        @Autowired
        private UserService userService;

        @PostMapping("/add-parent-child")
        public String addParentChild(@RequestParam String parentUsername, @RequestParam String childUsername) {
            userService.addParentChild(parentUsername, childUsername);
            return "Parent-child relationship added successfully";
        }

        @DeleteMapping("/remove-parent-child")
        public String removeParentChild(@RequestParam String parentUsername, @RequestParam String childUsername) {
            userService.removeParentChild(parentUsername, childUsername);
            return "Parent-child relationship removed successfully";
        }

        @PostMapping("/populate-from-file")
        public String populateFromFile(@RequestParam String filename) {
            userService.populateFromFile(filename);
            return "Parent-child relationships populated from file " + filename;
        }
    }
}
