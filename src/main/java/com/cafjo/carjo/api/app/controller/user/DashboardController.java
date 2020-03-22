package com.cafjo.carjo.api.app.controller.user;

import com.cafjo.carjo.api.app.service.user.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app/dashboard")
public class DashboardController extends BaseController{
    @Autowired
    private DashboardService service;

    @GetMapping(value = "/getallposts/{page}")
    public List<Map<String, Object>> getAllPosts(@RequestHeader(value = "Authorization") String authorizationHeader,@PathVariable int page) {
        return service.getAllPosts(getToken(authorizationHeader),page);
    }

    @GetMapping(value = "/getallposts/{id_category}/{page}")
    public List<Map<String, Object>> getAllPostsByCategory(@PathVariable int id_category, @PathVariable int page) {
        return service.getAllPostsByCategory(id_category, page);
    }

}
