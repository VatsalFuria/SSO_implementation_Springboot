package com.DailycodeBuffer.spring_security_6.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PageController {


    @GetMapping("/landing")
    public String welcome(){
        return "landing";
    }

    @GetMapping("/register")
    public String showSignUp() {
        return "signup"; // Loads signup.html
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login"; // Loads login.html
    }

    @GetMapping("/customlogout")
    public String showLogout() {
        return "logout";
    }

    @GetMapping("/product")
    public String showProduct() {
        return "product"; // Loads login.html
    }
}
