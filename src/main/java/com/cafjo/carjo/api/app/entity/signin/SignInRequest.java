package com.cafjo.carjo.api.app.entity.signin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    private String username;
    private String password;
    private String push_token;

    public SignInRequest(String username, String pass) {
        this.username=username;
        this.password=pass;
    }
}
