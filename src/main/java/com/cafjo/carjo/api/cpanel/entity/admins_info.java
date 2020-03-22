package com.cafjo.carjo.api.cpanel.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class admins_info {
    private String id;
    private String username;
    private String password;
    private String role;

    public admins_info(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
