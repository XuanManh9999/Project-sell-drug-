package com.back_end.myProject.service;

import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.dto.request.AuthenticationRequest;
import com.back_end.myProject.dto.request.IntrospeactRequest;
import com.back_end.myProject.dto.response.AuthenticationResponse;
import com.back_end.myProject.dto.response.IntrospeactResponse;
import com.nimbusds.jose.JOSEException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
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

    AuthenticationResponse authenticate (AuthenticationRequest request);
    IntrospeactResponse introspeact(IntrospeactRequest request) throws JOSEException, ParseException;

}
