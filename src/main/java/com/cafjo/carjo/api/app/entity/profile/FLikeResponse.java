package com.cafjo.carjo.api.app.entity.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FLikeResponse {

    private String user_token;
    private String type;

}
