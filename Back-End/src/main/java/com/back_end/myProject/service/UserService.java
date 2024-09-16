package com.back_end.myProject.service;

import com.back_end.myProject.dto.UserDTO;

import java.util.List;

public interface UserService {
    boolean loginUser(String email, String password);
    boolean registerUser (String email, String fullName, String password);
    boolean deleteUser(Long id);
    boolean updateUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserByName(String name);
    UserDTO findUserById(Long id);
}
