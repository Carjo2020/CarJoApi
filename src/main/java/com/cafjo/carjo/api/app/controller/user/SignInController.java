package com.cafjo.carjo.api.app.controller.user;

import com.cafjo.carjo.api.app.entity.signin.SignInRequest;
import com.cafjo.carjo.api.app.service.UserService;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app/signin")
public class SignInController extends BaseController{
    @Autowired
    private UserService service;

    @PostMapping(value = "/initlogin")
    public Map<String,Object> initLogin(@RequestBody SignInRequest signInRequest) {
        return service.initLogin(signInRequest);
    }

    @GetMapping(value = "/autologin")
    public boolean autoSignIn(@RequestHeader(value = "Authorization") String authorizationHeader) {
        return service.autoLogin(getToken(authorizationHeader));
    }

}
