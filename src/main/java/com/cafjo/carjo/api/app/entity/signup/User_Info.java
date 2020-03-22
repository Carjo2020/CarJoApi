package com.cafjo.carjo.api.app.entity.signup;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User_Info {

    int id;
    String first_name;
    String last_name;
    String phone;
    String email;
    String date;
    String password;
    String push_token;
    String post_likes;
    String post_favorite;
    String count_post_likes;
    String count_post_favorite;
}
