package com.otpverification.otp.repository;

import com.otpverification.otp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String Email);
}
