package com.cafjo.carjo.api.cpanel.controller;

import com.cafjo.carjo.api.cpanel.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/cpanel/posts")
public class PostController {

    @Autowired
    private PostService service;

    @PostMapping(value = "/add")
    public boolean addPost(@RequestParam(value = "title", required = false, defaultValue = "null") String title,
                           @RequestParam(value = "id_category", required = false, defaultValue = "null") String id_category,
                           @RequestParam(value = "post_id", required = false, defaultValue = "null") String post_id,
                           @RequestParam(value = "text", required = false, defaultValue = "null") String text,
                           @RequestParam(value = "url_picture", required = false, defaultValue = "null") String url_picture,
                           @RequestParam(value = "url_video", required = false, defaultValue = "null") String url_video,
                           @RequestParam(value = "file_picture", required = false) MultipartFile file_picture,
                           @RequestParam(value = "file_video", required = false) MultipartFile file_video

    ) throws IOException {
        return service.addPost(post_id, title.replaceAll("'", "''"), id_category, text.replaceAll("'", "''"), url_picture, url_video, file_picture, file_video);
    }

    @DeleteMapping(value = "/delete/{post_id}")
    public boolean deletePost(@PathVariable String post_id) throws IOException {
        return service.deletePost(post_id);
    }
}
