package com.otpverification.otp.repository;

import com.otpverification.otp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {


    Optional<Role> findByRole(String name);
}
