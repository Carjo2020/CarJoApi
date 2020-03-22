package com.cafjo.carjo.api.cpanel.controller;

import com.cafjo.carjo.api.all.PushNotificationService;
import com.cafjo.carjo.api.cpanel.entity.Push_Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/cpanel/users")
public class UsersController {
    @Autowired
    PushNotificationService pushNotificationService;

    @PostMapping(value = "/sendforuser")
    public boolean sendUserPush(@RequestBody Push_Msg request) {
        return pushNotificationService.sendUserPush(request);
    }

    @PostMapping(value = "/sendforall")
    public boolean sendPushForAllUsers(@RequestBody Push_Msg request) {
        return pushNotificationService.sendPushForAllUsers(request.getTitle(), request.getBody());
    }

}