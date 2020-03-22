package com.cafjo.carjo.api.cpanel.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/cpanel/test")
public class TestCpanelController {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @GetMapping(value = "/getallposts")
    public List<Map<String, Object>> getAllUsers() {
        return jdbcTemplate.queryForList("select * from posts_data ORDER BY post_id DESC");
    }

    @GetMapping(value = "/deleteallposts")
    public boolean deleteallusers() {
        try {
            jdbcTemplate.update("delete from posts_data");
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}