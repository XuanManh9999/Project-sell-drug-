package com.back_end.myProject.service.impl;

import com.back_end.myProject.entities.User;
import com.back_end.myProject.repository.UserRepository;
import com.back_end.myProject.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean loginUser(String email, String password) {
        User user =  userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }

    @Override
    public boolean registerUser(String email, String fullName, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            // email da duoc su dung
            return false;
        }
        // create User
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullname(fullName);
        newUser.setPassword(password);
        newUser.setStatus(1);
        userRepository.save(newUser);
        return true;
    }
}
