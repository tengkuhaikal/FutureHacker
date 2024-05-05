package com.DataStructure.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/register")
    public String getRegisterPage(){
        return "registerPage";
    }

    @PostMapping("login")
    public String getLoginPage(@ModelAttribute User user){
        System.out.println(user.toString());
        return "loginPage";
    }
    
}
