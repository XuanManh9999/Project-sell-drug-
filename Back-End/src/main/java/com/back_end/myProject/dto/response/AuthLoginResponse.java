package com.back_end.myProject.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthLoginResponse {
    private boolean authenticated;
    private String token;
}
