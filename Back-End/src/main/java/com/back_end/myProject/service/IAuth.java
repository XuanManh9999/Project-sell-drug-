package com.back_end.myProject.service;

import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.dto.response.AuthLoginResponse;
import com.back_end.myProject.entities.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface IAuth {
    boolean register (String email, String password);
    AuthLoginResponse login(String email, String password);
    UserDTO authenticateUser (String token) throws JOSEException, ParseException;
}
