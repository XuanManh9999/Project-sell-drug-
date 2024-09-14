package com.back_end.myProject.controller;


import com.back_end.myProject.utils.ResponseCustom;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {

    @GetMapping("")
    public ResponseEntity getUser() {
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseCustom(200, "OK", "HEllo"));
    }
}
