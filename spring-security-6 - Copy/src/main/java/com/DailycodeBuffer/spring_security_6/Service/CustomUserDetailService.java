package com.DailycodeBuffer.spring_security_6.Service;

import com.DailycodeBuffer.spring_security_6.CustomUserDetails;
import com.DailycodeBuffer.spring_security_6.Entity.User;
import com.DailycodeBuffer.spring_security_6.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomUserDetailService implements UserDetailsService {


    private final UserRepo userRepo;

    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByUserName(username);
        if(Objects.isNull(user)){
            System.out.println("User not Available");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);
    }
}
