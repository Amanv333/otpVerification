package com.otpverification.otp.service;

import com.otpverification.otp.Dto.UserDto;
import com.otpverification.otp.entity.Role;
import com.otpverification.otp.entity.User;
import com.otpverification.otp.repository.RoleRepository;
import com.otpverification.otp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public ResponseEntity<?> registerUser(UserDto dto){
        if(userRepository.existsByEmail(dto.getEmail())){
            return new ResponseEntity<>("User is Already registered with email "+dto.getEmail(), HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByMobile(dto.getMobile())){
            return new ResponseEntity<>("User is Already registered with mobile "+dto.getMobile(), HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setMobile(dto.getMobile());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());

        Set<String> strRoles = dto.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null){
            Role userRole = roleRepository.findByRole("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRole("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByRole("ROLE_MODERATOR")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByRole("ROLE_USER")
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);



        userRepository.save(user);
        return new ResponseEntity<>("user register successfully ",HttpStatus.CREATED);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void verifyEmail(User user) {
        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public boolean isEmailVerified(String email) {
        User user = userRepository.findByEmail(email);
        return user != null && user.isEmailVerified();
    }
}
