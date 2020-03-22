package com.cafjo.carjo.api.app.service.user;

import com.cafjo.carjo.api.app.entity.profile.User_All_Columns;
import com.cafjo.carjo.api.app.entity.signup.User_Info;
import com.cafjo.carjo.api.app.service.AppBaseService;
import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SignUpService extends AppBaseService {

    public Map<String, Object> sendSmsAsync(String phone) throws IOException {
        String Digit4Code = "";
        Map<String, Object> map = new HashMap<String, Object>();
        Random r = new Random();
        for (int iter = 0; iter < codeDigits; iter++) {
            Digit4Code += r.nextInt(10);
        }
        System.out.println("Digit4Code: " + Digit4Code);
        relansService.sendVerficationCode(phone, Digit4Code);
        map.put("sms_token", passwordEncoder().encode(Digit4Code));
        return map;
    }


    public boolean addUser(User_Info user) {
        int count = getTableCount("users_info", "id") + 1;
        System.out.println("count:" + count);
        boolean isUser = checkAlradyUser(user.getPhone());
        if (!isUser) {
            jdbcTemplate.update($("insert into users_info values(" + count + ",'%s','%s','%s','%s','%s','%s','%s', ',' , ',' ,'true', ',' ,'0','0','')", user.getFirst_name(), user.getLast_name(), user.getPhone(), user.getEmail(), user.getDate(),
                    passwordEncoder().encode(user.getPassword()), user.getPush_token()));
            return true;
        } else {
            throw new NotFoundException("This User is already exist");
        }
    }

    public boolean updateUser(String Authorization, User_All_Columns user) {
        String phone = tokenUtil.getPhoneFromToken(Authorization.replaceFirst("bearer ", "").replaceFirst("Bearer ", ""));
        boolean isUser = checkAlradyUser(phone);
        if (isUser) {
            jdbcTemplate.update($("update users_info set first_name='%s',last_name='%s',email='%s',date='%s' where phone='%s'",
                    user.getFirst_name(), user.getLast_name(), user.getEmail(), user.getDate(), phone));
            if (!user.getOld_password().equals("")) {
                String pass = getValue("users_info", "password", "phone", phone);
                if (passwordEncoder().matches(user.getOld_password(), pass)) {
                    jdbcTemplate.update($("update users_info set password='%s' where phone='%s'",
                            passwordEncoder().encode(user.getNew_password()), phone));
                } else {
                    throw new NotFoundException("The old password doesn't matches");
                }
            }

            return true;
        } else {
            throw new NotFoundException("User not exist");
        }
    }

}
