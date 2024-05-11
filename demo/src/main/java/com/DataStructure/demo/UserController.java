package com.DataStructure.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    
    
    private UserService service;

    @GetMapping("/register")
    public String getRegisterPage(Model user){
        user.addAttribute("registerRequest", new User());
        return "register_page";
    }
    
    @GetMapping
    public String getLoginPage(Model user){
        user.addAttribute("loginRequest", new User());
        return "login_page";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user){
        System.out.println("register request: "+ user);
        User registereduser = service.registeredUser(user.getEmail(), user.getUsername(), user.getPassword(), user.getRole());
       return  registereduser == null ? "error_page": "redirect:/login";
    }
    
    
    @PostMapping("/login")
    public String login(@ModelAttribute User user){
        System.out.println("login request: "+ user);
        User succeedlogin = service.login(user.getEmail(), user.getPassword());
       return  succeedlogin == null ? "error_page": "home";
    }
    
}
