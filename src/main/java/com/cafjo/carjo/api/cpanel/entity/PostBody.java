package com.cafjo.carjo.api.cpanel.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostBody {

    private String post_id;
    private String title;
    private String url_picture;
    private String url_video;
    private String text;
    private String date;
    private String id_category;
    private String likes;
    private String favorites;


}
