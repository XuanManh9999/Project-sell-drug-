package com.back_end.myProject.service;

import com.back_end.myProject.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    boolean loginUser(String email, String password);
    boolean registerUser (String email, String fullName, String password);
    boolean deleteUser(Long id);
    boolean updateUser(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserByName(String name);
    UserDTO findUserById(Long id);
    boolean createUser(UserDTO userDTO);

    Page<UserDTO> getUsers(Pageable pageable); // Thêm phương thức phân trang
    //    Phuong thuc phan trang
}
