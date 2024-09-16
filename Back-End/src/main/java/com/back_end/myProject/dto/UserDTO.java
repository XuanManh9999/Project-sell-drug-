package com.back_end.myProject.dto;

import lombok.Getter;
import lombok.Setter;
//Java Bean
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String email;
    private String fullname;
    private Integer age;
    private String phone;
    private String password;

}
