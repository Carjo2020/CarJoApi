package com.cafjo.carjo.api.app.entity.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.util.http.parser.Authorization;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User_All_Columns {

    int id;
    String first_name;
    String last_name;
    String phone;
    String email;
    String date;
    //    String push_token;
//    String push_enable;
//    String categories;
//    String authorization;
    String old_password;
    String new_password;
    String post_likes;
    String post_favorite;
    String count_post_likes;
    String count_post_favorite;
}
