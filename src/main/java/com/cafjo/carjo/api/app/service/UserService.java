package com.cafjo.carjo.api.app.service;

import com.cafjo.carjo.api.app.entity.forgetpass.SMSChecker;
import com.cafjo.carjo.api.app.entity.signin.SignInRequest;
import com.cafjo.carjo.api.error.BadRequestException;
import com.rasheeqat.app.entity.*;


import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserService extends AppBaseService {


    public Map<String, Object> initLogin(SignInRequest signInRequest) {
        Map<String, Object> map = new HashMap<>();
        String username = signInRequest.getUsername();
        String push_token = signInRequest.getPush_token();
        if (checkAlradyUser(username)) {
            String phone = getValue("users_info", "phone", "email", username + "' or phone='" + username);
            String password = signInRequest.getPassword();
            String passwordEncoder = getValue("users_info", "password", "phone", phone);
            boolean matechs = passwordEncoder().matches(password, passwordEncoder);
            System.out.println("matechs: " + matechs);
            if (matechs) {
                jdbcTemplate.update("update users_info set push_token='" + push_token + "' where phone='" + phone + "';");
                String token = tokenUtil.generateTokenFromPhoneAndPassword(new SignInRequest(phone, signInRequest.getPassword()));
                map.put("user_token", token);
                map.put("posts_count", getRawsCount("posts_data", "post_id"));
                return map;
            } else {
                throw new BadRequestException("Password does not matches");
            }
        } else {
            throw new NotFoundException("User not found");
        }


    }


    public boolean checkCode(SMSChecker body) {
//        String user_token = body.getUser_token();
        String sms_token = body.getSms_token();
        String code = body.getCode();
//        String phone = tokenUtil.getPhoneFromSmsToken(user_token);
//        System.out.println("phone: " + phone);
//        boolean isUser = checkAlradyUser(phone);
//        if (isUser) {
        boolean matechs = passwordEncoder().matches(code, sms_token);
        System.out.println("matechs: " + matechs);
        if (matechs) {
            return true;
        } else {
            throw new NotFoundException("This code does not match token");
        }
//        } else {
//            throw new NotFoundException("User not found");
//        }
    }


    public boolean autoLogin(String user_token) {

        SignInRequest signInRequest = tokenUtil.getSignInRequestFromToken(user_token);
        if (signInRequest != null) {
            String phone = signInRequest.getUsername();
            String password = signInRequest.getPassword();
            System.out.println(phone);
            System.out.println(password);
            String passwordEncoder = getValue("users_info", "password", "phone", phone);
            boolean matechs = passwordEncoder().matches(password, passwordEncoder);
            System.out.println("matechs: " + matechs);
            if (matechs) {
                return matechs;
            } else {
                throw new BadRequestException("Password does not matches");
            }
        } else {
            throw new BadRequestException("Token not valid");
        }


    }
}
