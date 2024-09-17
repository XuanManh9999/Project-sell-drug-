package com.back_end.myProject.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {
    String accessToken;
    String refreshToken;
    boolean authenticated;

    public AuthenticationResponse () {

    }
    public AuthenticationResponse(String accessToken, String refreshToken, boolean authenticated) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.authenticated = authenticated;
    }
}
