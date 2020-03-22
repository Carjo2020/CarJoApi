package com.cafjo.carjo.api.app.controller.user;

import com.cafjo.carjo.api.app.entity.profile.User_All_Columns;
import com.cafjo.carjo.api.app.entity.signup.User_Info;
import com.cafjo.carjo.api.app.service.user.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app/signup")
public class SignUpController {
    @Autowired
    private SignUpService service;
//    @Autowired
//    private UserService userService;

//    @PostMapping(value = "/sendsms")
//    public Map<String, Object> sendSmsAsync(@RequestBody Phone phone) throws IOException {
//        boolean isUser = userService.checkAlradyUser(phone.getPhone());
//        if (!isUser) {
//            return service.sendSmsAsync(phone.getPhone());
//        } else {
//            throw new NotFoundException("This User is already exist");
//        }
//
//    }


//    @PutMapping(value = "/user/update/password")
//    public boolean smsUpdatePassword(@RequestBody SMSUpdatePassword body) {
//        return service.smsUpdatePassword(body);
//    }

    @PostMapping(value = "/adduser")
    public boolean addUser(@RequestBody User_Info user) {
        return service.addUser(user);
    }

    @PostMapping(value = "/updateuser")
    public boolean updateUser(@RequestHeader(value = "Authorization") String authorizationHeader,@RequestBody User_All_Columns user) {
        return service.updateUser(authorizationHeader,user);
    }

}
