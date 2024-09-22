package com.back_end.myProject.controller;


import com.back_end.myProject.dto.UserDTO;
import com.back_end.myProject.dto.request.AuthenticationRequest;
import com.back_end.myProject.dto.request.IntrospeactRequest;
import com.back_end.myProject.dto.response.AuthenticationResponse;
import com.back_end.myProject.dto.response.IntrospeactResponse;
import com.back_end.myProject.service.UserService;
import com.back_end.myProject.utils.ResponseCustom;
import com.back_end.myProject.utils.ResponsePageable;
import com.nimbusds.jose.JOSEException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    // GET -> YEU CAU LAY DU LIEU
    // POST ->  YEU CAU THEM DU LIEU
    // PUT -> YEU CAU CAP NHAT DU LIEU
    // DELETE -> YEU CAU XOA DU LIEU

    @PostMapping("")
    public ResponseEntity<ResponseCustom> createUser (@RequestBody UserDTO userDTO) {
        ResponseCustom responseCustom;
        try {
            String fullname = userDTO.getFullname();
            String email = userDTO.getEmail();
            String password = userDTO.getPassword();
            if (fullname == null || email == null || password == null) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing required fields", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
            }
            boolean IsCreate = userService.createUser(userDTO);
            if (IsCreate) {
                // 201
                responseCustom = new ResponseCustom(HttpStatus.CREATED.value(), "User created", null);
                return ResponseEntity.status(HttpStatus.CREATED).body(responseCustom);
            }else {
                responseCustom = new ResponseCustom(HttpStatus.CONFLICT.value(), "User already exists", null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(responseCustom);
            }

        }catch (Exception ex) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseCustom);
        }
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

            Boolean isLogin = userService.loginUser(email, password);

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

            Boolean isRegistered = userService.registerUser(email, fullname, password);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseCustom> deleteUser(@PathVariable("id") Long id) {
        ResponseCustom response;
       try {
        if (id == null) {
            response = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing required fields", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        Boolean isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            response = new ResponseCustom(HttpStatus.OK.value(), "Deletion successful", null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }else {
            response = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
       }catch (Exception e) {
            response =  new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
       }
    }

    @PutMapping("")
    public ResponseEntity<ResponseCustom> updateUser(@RequestBody UserDTO userDTO) {
        ResponseCustom responseCustom;
        try {
            Long id = userDTO.getId();
            String email = userDTO.getEmail();
            String fullname = userDTO.getFullname();
            String password = userDTO.getPassword();
            if (id == null || email == null || fullname == null ||  password == null) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing required fields", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
            }
            Boolean isUpdate = userService.updateUser(userDTO);
            if (isUpdate) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Update successful", null);
                return ResponseEntity.status(HttpStatus.OK).body(responseCustom);
            }else {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "User not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseCustom);
            }
      }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseCustom);
      }
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseCustom> getUsers () {
        ResponseCustom responseCustom;
        try {
            List<UserDTO> usersDTO = userService.getAllUsers();
            if (usersDTO != null || !usersDTO.isEmpty()) {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "Users", usersDTO);
                return ResponseEntity.status(HttpStatus.OK).body(responseCustom);
            }else {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "User not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseCustom);
            }
        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseCustom);
        }
    }

    @GetMapping("")
    public ResponseEntity<ResponseCustom>  getUserByName (@RequestParam(name = "full-name") String fullName) {
        ResponseCustom responseCustom;
        try {
            if (fullName == null || fullName.isEmpty()) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing required fields", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
            }
            UserDTO userDTO = userService.getUserByName(fullName);
            if (userDTO == null) {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "User not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseCustom);
            }else {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "User", userDTO);
                return ResponseEntity.status(HttpStatus.OK).body(responseCustom);
            }
        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseCustom);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseCustom> getUserById (@PathVariable Long id) {
        ResponseCustom responseCustom;
        try {
            if (id == null) {
                responseCustom = new ResponseCustom(HttpStatus.BAD_REQUEST.value(), "Missing required fields", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseCustom);
            }
            UserDTO userDTO = userService.findUserById(id);
            if (userDTO == null) {
                responseCustom = new ResponseCustom(HttpStatus.NOT_FOUND.value(), "User not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseCustom);
            }else {
                responseCustom = new ResponseCustom(HttpStatus.OK.value(), "User", userDTO);
                return ResponseEntity.status(HttpStatus.OK).body(responseCustom);
            }
        }catch (Exception e) {
            responseCustom = new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseCustom);
        }
    }


    @GetMapping("/test/users")
    public ResponseEntity<ResponseCustom> getAllUsers (Pageable pageable) {
        try {
            Page<UserDTO> userPage = userService.getUsers(pageable);
            if (userPage.hasContent()) {
                return ResponseEntity.ok(new ResponseCustom(HttpStatus.OK.value(), "Users", userPage));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseCustom(HttpStatus.NOT_FOUND.value(), "No users found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseCustom(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage()));
        }
    }


    @PostMapping("/test/auth/log-in")
    public AuthenticationResponse authenticateUser (@RequestBody AuthenticationRequest authenticationRequest) {
      return  userService.authenticate(authenticationRequest);
    }

        @PostMapping("/test/auth/token")
    public IntrospeactResponse introspeactResponse (@RequestBody IntrospeactRequest introspeactRequest) throws ParseException, JOSEException {
        return userService.introspeact(introspeactRequest);
    }

}
