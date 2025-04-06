package com.DailycodeBuffer.spring_security_6.Repository;

import com.DailycodeBuffer.spring_security_6.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByUserName(String UserName);
}
