package com.cafjo.carjo.api.cpanel.controller;

import com.cafjo.carjo.api.app.entity.JwtResponse;
import com.cafjo.carjo.api.cpanel.entity.admins_info;
import com.cafjo.carjo.api.cpanel.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/cpanel/admins")
public class AdminsController {
    @Autowired
    private AdminsService service;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/initlogin")
    public JwtResponse checkLogin(@RequestBody admins_info admin) {
        return service.checkLogin(admin.getUsername(), admin.getPassword());
    }

    @GetMapping(value = "/autologin")
    public boolean autoSignIn(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return service.autoLogin(authorizationHeader.replaceFirst("bearer", "").replaceAll(" ", ""));
    }

    @GetMapping(value = "/getall")
    public List<Map<String, Object>> getAllAdmins(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return service.getAllAdmins(authorizationHeader);
    }

//    @GetMapping(value = "/get/{id}")
//    public List<Map<String, Object>> getAdmin(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String id) {
//        return jdbcTemplate.queryForList("select * from admins_info where id = " + id);
//    }

    @DeleteMapping(value = "/delete/{id}")
    public boolean deleteAdmin(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String id) {

        return service.deleteAdmin(authorizationHeader,id);
    }

    @PostMapping(value = "/add")
    public boolean addAdmin(@RequestParam(value = "id", required = false, defaultValue = "null") String id,
                            @RequestParam(value = "username", required = false, defaultValue = "null") String username,
                            @RequestParam(value = "password", required = false, defaultValue = "null") String password,
                            @RequestParam(value = "role", required = false, defaultValue = "null") String role,
                            @RequestHeader(value = "Authorization", required = true) String authorizationHeader) {
        return service.addAdmin(authorizationHeader, new admins_info(id, username, password, role));
    }

}