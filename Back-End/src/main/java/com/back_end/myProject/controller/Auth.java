package com.back_end.myProject.controller;

import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.dto.request.AuthLoginRequest;
import com.back_end.myProject.dto.response.AuthLoginResponse;
import com.back_end.myProject.service.IAuth;
import com.back_end.myProject.utils.ResponseCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class Auth {
    private final IAuth auth;
    public Auth(IAuth auth) {
        this.auth = auth;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseCustom> register(@RequestBody Map<String, Object> request) {
      ResponseCustom responseCustom;
      try {
          String email = (String) request.get("email");
          String password = (String) request.get("password");
          if (email.isEmpty() || password.isEmpty()) {
              responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "email or pass is not empty", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
          }
          Boolean isRegister = auth.register(email, password);
          if (isRegister) {
              responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Register success", null);
              return ResponseEntity.status(HttpStatus.OK).body(responseCustom);
          }else {
            responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), "Register failed", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseCustom);
          }
      }catch (Exception e) {
          responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
          return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    @PostMapping("/log-in")
    public ResponseEntity<?> login (@RequestBody AuthLoginRequest authLoginRequest) {
        AuthLoginResponse authLoginResponse;
        ResponseCustom responseCustom;
        try {
            String email = authLoginRequest.getEmail();
            String password = authLoginRequest.getPassword();
            if (email.isEmpty() || password.isEmpty()) {
                responseCustom =  new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "email or password is not empty", authLoginRequest);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
            }
            authLoginResponse = auth.login(email, password);
            if (authLoginResponse == null) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Login failed", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
            }else {
                return ResponseEntity.status(HttpStatus.OK).body(authLoginResponse);
            }
        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseCustom> authenticateUser(@RequestHeader("Authorization") String authorizationHeader) {
        ResponseCustom responseCustom;
       try {
           if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
               String token = authorizationHeader.substring(7);
               UserDTO userDTO =  auth.authenticateUser(token);
               if (userDTO != null) {
                   responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Authenticated", userDTO);
                   return ResponseEntity.status(HttpStatus.OK).body(responseCustom);
               }else {
                   responseCustom = new ResponseCustom(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", null);
                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseCustom);
               }
           } else {
               responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Authorization header is missing", null);
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
           }
       }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
            return new ResponseEntity<>(responseCustom, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

}
