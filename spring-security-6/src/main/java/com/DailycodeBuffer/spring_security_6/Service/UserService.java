package com.DailycodeBuffer.spring_security_6.Service;

import com.DailycodeBuffer.spring_security_6.Entity.User;
import com.DailycodeBuffer.spring_security_6.Repository.UserRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static UserRepo userRepo = null;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static AuthenticationManager authenticationManager = null;
    private static JwtService jwtService = null;

    public UserService(UserRepo userRepo, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepo = userRepo;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public static String verify(User user) {
        Authentication authenticate
                = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUserName(), user.getPassword()));
//        var u=userRepo.findByUserName(user.getUserName());
        if(authenticate.isAuthenticated())
            return jwtService.generateToken(user);
        else
            return "failure";
    }

    public User register(User user) {
        user.setPassword(bCryptPasswordEncoder
                .encode(user.getPassword()));
        return userRepo.save(user);
    }
}
