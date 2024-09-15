package com.back_end.myProject.service;

public interface UserService {
    boolean loginUser(String email, String password);
    boolean registerUser (String email, String fullName, String password);
}
