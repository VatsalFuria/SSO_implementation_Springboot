package com.DailycodeBuffer.spring_security_6.Controller;

import com.DailycodeBuffer.spring_security_6.Entity.User;
import com.DailycodeBuffer.spring_security_6.Repository.UserRepo;
import com.DailycodeBuffer.spring_security_6.Service.JwtService;
import com.DailycodeBuffer.spring_security_6.Service.UserService;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;


@RestController
public class UserController {

    private final UserRepo userRepo;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public UserController(UserRepo userRepo, UserService userService, UserDetailsService userDetailsService) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/api/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }


    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody User user){
        String token = UserService.verify(user);

        ResponseCookie cookie = ResponseCookie.from("jwtToken", token)
                .httpOnly(true)
                .secure(true)        // Set to true in production (requires HTTPS)
                .path("/")
                .maxAge(3600)        // Set expiration time as needed
                .sameSite("Lax")     // Configure as per your requirement (None, Lax, Strict)
                .build();

        System.out.println("token: " + token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout() {

        System.out.println("IN POST LOGOUT");

        try {
            ResponseCookie deleteCookie = ResponseCookie.from("jwtToken")
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(0)  // Expire the cookie immediately
                    .sameSite("Lax")
                    .build();

            System.out.println("exiting POST LOGOUT");

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                    .body("Logged out successfully");

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/api/userNameFromToken")
    public ResponseEntity<String> validateToken(@CookieValue(name = "jwtToken", required = false) String token) {

        JwtService jwtService = new JwtService();

        System.out.println("jwt token found through /api/userNameFromToken " + token);

        String userName = jwtService.extractUserName(token);

        if (userName.isEmpty()) {
            return ResponseEntity.status(401).body("Couldnt find Username");
        }
        return ResponseEntity.ok(userName);
    }

}
