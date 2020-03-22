package com.cafjo.carjo.api.all;

import com.google.firebase.messaging.*;
import com.cafjo.carjo.api.app.service.AppBaseService;
import com.cafjo.carjo.api.cpanel.entity.Push_Msg;
import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class PushNotificationService {
    AppBaseService service;
    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean sendPushToToken(String token, String title, String msg) {
        MulticastMessage message = MulticastMessage.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setNotification(new Notification(title, msg))
                .addToken(token)
                .build();
        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            return true;
        } catch (FirebaseMessagingException ex) {
            throw new NotFoundException("Can't Send push notification for this user");
        }
    }

    public boolean sendUserPush(Push_Msg request) {
        String token = service.getValue("users_info", "push_token", "id", request.getId());
        return sendPushToToken(token, request.getTitle(), request.getBody());
    }

    public boolean sendPushForAllUsers(String title, String msg) {
        List<String> tokens = new ArrayList<>();
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select push_token from users_info");
        for (Map<String, Object> map : list) {
            tokens.add(map.get("push_token") + "");
            System.out.println("map.get(\"push_token\") + \"\":" + map.get("push_token") + "");
        }
        try {
            if (tokens.size() > 0) {
                MulticastMessage message = MulticastMessage.builder()
                        .putData("score", "850")
                        .putData("time", "2:45")
                        .setNotification(new Notification(title, msg))
                        .addAllTokens(tokens)
                        .build();
                BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            }
            return true;
        } catch (FirebaseMessagingException ex) {
            for (String token : tokens) {
                sendPushToToken(token, title, msg);
            }
            return true;
        }
    }

}
