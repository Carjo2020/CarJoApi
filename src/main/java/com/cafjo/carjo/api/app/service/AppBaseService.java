package com.cafjo.carjo.api.app.service;

import com.cafjo.carjo.api.all.BucketService;
import com.cafjo.carjo.api.all.PushNotificationService;
import com.cafjo.carjo.api.all.RelansService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AppBaseService {

    public int codeDigits = 4;

    @Autowired
    public JdbcTemplate jdbcTemplate;
    @Autowired
    public TokenUtil tokenUtil;
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    public BucketService bucketService;
    @Autowired
    public PushNotificationService pushNotificationService;
    @Autowired
    public RelansService relansService;

    public String getPhoneFromToken(String token) {
        return tokenUtil.getPhoneFromToken(token);
    }

    public String getPhoneFromSmsToken(String token) {
        return tokenUtil.getPhoneFromSmsToken(token);
    }

    public boolean checkAlradyUser(String username) {
        System.out.println("select count(id) from users_info where  phone='" + username + "' or email='" + username + "'");
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select count(id) from users_info where  phone='" + username + "' or email='" + username + "'");
        Map<String, Object> map = list.get(0);
        Double no = Double.valueOf(map.get("count") + "");
        if (no != 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getTableCount(String nameTable, String nameID) {
        System.out.println("select max(" + nameID + ") from " + nameTable);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select max(" + nameID + ") from " + nameTable);
        String respone = list.get(0).get("max") + "";
        if (respone.equals("null") || respone.equals(null) || respone.equals("")) {
            return 0;
        }
        return Integer.valueOf(respone);
    }

    public int getRawsCount(String nameTable, String nameID) {
        System.out.println("select count(" + nameID + ") from " + nameTable);
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select count(" + nameID + ") from " + nameTable);
        String respone = list.get(0).get("count") + "";
        if (respone.equals("null") || respone.equals(null) || respone.equals("")) {
            return 0;
        }
        return Integer.valueOf(respone);
    }

    public String getValue(String table_name, String table_column) {
        try {
            return jdbcTemplate.queryForList("select " + table_column + " from " + table_name).get(0).get(table_column) + "";
        } catch (Exception e) {
            return "null";
        }
    }

    public String getValue(String table_name, String table_column, String where_name, String where_value) {
        try {
            System.out.println("select " + table_column + " from " + table_name + " where " + where_name + "='" + where_value + "';");
            return jdbcTemplate.queryForList("select " + table_column + " from " + table_name + " where " + where_name + "='" + where_value + "';").get(0).get(table_column) + "";
        } catch (Exception e) {
            return "null";
        }
    }
//where email='" + user + "' or phone='" + user + "';"
//    public boolean isUser(String user) {
//        List<Map<String, Object>> list = jdbcTemplate.queryForList("select count(id) from users_info" );
//        Map<String, Object> map = list.get(0);
//        Double no = Double.valueOf(map.get("count") + "");
//        if (no != 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public String $(String text, String... values) {
        return String.format(text, values);
    }


//    public String sendUserPush(push_msg push) {
//        System.out.println("rnn:");
//        String token = getValue("users_info", "push_token", "id", push.getId());
//        System.out.println("token:" + token);
////        String token = "cn1-fcm-dK2wLGsT4uM:APA91bEjY81W42l9ATPdfg5MYGTO9sMcJgw3OH8T5z5yS-shMORKPkWWs-tRfEG2dwn3MX8QHtihcU_E0eAtza1ADXXMjziC_92H_RL3V_3xMLN_zPYDfY5dLBC3sWtaEP2xWSBLpA2R";
//        MulticastMessage message = MulticastMessage.builder()
//                .putData("score", "850")
//                .putData("time", "2:45")
//                .setNotification(new Notification(push.getTitle(), push.getBody()))
//                .addToken(token)
//                .build();
//        try {
//            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
//            return "true";
//        } catch (FirebaseMessagingException ex) {
//            Logger.getLogger("erorr in sendPushToAdmins at OwnerService").log(Level.SEVERE, null, ex);
//            return "error sending push";
//        }
//    }


    public String getGymIdInt(String id_gym) {
        return jdbcTemplate.queryForList("select id from gyms_info where id_gym='" + id_gym + "';").get(0).get("id") + "";
    }

    public String getGymIdString(String id) {
        return jdbcTemplate.queryForList("select id_gym from gyms_info where id='" + id + "';").get(0).get("id_gym") + "";
    }

    public double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    //    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
//    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void updateUsersInfo(String id, String name, String value) {
        value = (value.equals("null") || value.equals("")) ? getValue("users_info", name, "id", id) : value;
    }


}
