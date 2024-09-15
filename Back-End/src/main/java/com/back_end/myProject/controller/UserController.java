package com.back_end.myProject.controller;


import com.back_end.myProject.service.UserService;
import com.back_end.myProject.utils.ResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseCustom> loginUser(@RequestBody Map<String, String> body) {
        ResponseCustom response;
        try {
            String email = body.get("email");
            String password = body.get("password");

            if (email == null || password == null) {
                response = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing email or password", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isLogin = userService.loginUser(email, password);

            if (isLogin) {
                response = new ResponseCustom(HttpStatus.OK.value(), "Login successful", null);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                response = new ResponseCustom(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response = new ResponseCustom(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), // Mã lỗi HTTP
                    "Internal Server Error",                 // Thông điệp lỗi
                    e.getMessage()                           // Chi tiết lỗi
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseCustom> registerUser(@RequestBody Map<String, String> body) {
        ResponseCustom response;
        try {
            String email = body.get("email");
            String fullname = body.get("fullname");
            String password = body.get("password");

            if (email == null || fullname == null || password == null) {
                response = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing required fields", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            boolean isRegistered = userService.registerUser(email, fullname, password);

            if (isRegistered) {
                response = new ResponseCustom(HttpStatus.CREATED.value(), "Registration successful", null);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response = new ResponseCustom(HttpStatus.CONFLICT.value(), "Email already in use", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } catch (Exception e) {
            response = new ResponseCustom(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
