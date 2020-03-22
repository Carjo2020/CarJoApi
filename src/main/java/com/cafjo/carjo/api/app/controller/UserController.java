package com.cafjo.carjo.api.app.controller;

import com.cafjo.carjo.api.all.FireBaseUtil;
import com.cafjo.carjo.api.app.entity.Phone;
import com.cafjo.carjo.api.app.entity.forgetpass.SMSChecker;
import com.cafjo.carjo.api.app.service.user.SignUpService;
import com.rasheeqat.app.entity.*;
import com.cafjo.carjo.api.app.service.UserService;

import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app")
public class UserController {

    @Autowired
    private UserService service;
    @Autowired
    private SignUpService signUpService;


    @PostMapping(value = "/signup/sendsms")
    public Map<String, Object> sendSmsAsync(@RequestBody Phone phone) throws IOException {
        boolean isUser = service.checkAlradyUser(phone.getPhone());

        if (!isUser) {
            return signUpService.sendSmsAsync(phone.getPhone());
        } else {
            throw new NotFoundException("This User is already exist");
        }

    }


    @GetMapping(value = "/checkuser")
    public boolean checkUser(@RequestBody Phone phone) {
        return service.checkAlradyUser(phone.getPhone());
    }


//    @PostMapping(value = "/signin")
//    public JwtResponse signIn(@RequestBody SignInRequest signInRequest) {
//        return service.signIn(signInRequest);
//    }


    @PostMapping(value = "/check4digit")
    public boolean checkCode(@RequestBody SMSChecker body) {
        return service.checkCode(body);
    }

    FireBaseUtil fireBaseUtil = new FireBaseUtil();



}
