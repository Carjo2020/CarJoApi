package com.cafjo.carjo.api.app.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app/test")
public class TestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping(value = "/test")
    public String test() {
        return "test";
    }


    @GetMapping(value = "/getallusers")
    public List<Map<String, Object>> getAllUsers() {
        return jdbcTemplate.queryForList("select * from users_info");
    }

    @GetMapping(value = "/deleteallusers")
    public boolean deleteallusers() {
        try {
            jdbcTemplate.update("delete from users_info");
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}