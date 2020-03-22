package com.cafjo.carjo.api.app.controller.user;

import com.cafjo.carjo.api.app.entity.Phone;
import com.cafjo.carjo.api.app.entity.profile.User_All_Columns;
import com.cafjo.carjo.api.app.service.user.ForgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app/forget")
public class ForgetPassController extends BaseController {
    @Autowired
    private ForgetService service;

    @PostMapping(value = "/sendsms")
    public Map<String, Object> sendSmsAsync(@RequestBody Phone phone) throws IOException {
        return service.sendSmsAsync(phone.getPhone());
    }


    @PostMapping(value = "/updatepassword")
    public boolean smsUpdatePassword(@RequestHeader(value = "Authorization") String authorizationHeader, @RequestBody User_All_Columns user) {
        return service.smsUpdatePassword(getToken(authorizationHeader), user.getNew_password());
    }


}
