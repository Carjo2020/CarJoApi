package com.cafjo.carjo.api.app.service.user;

import com.cafjo.carjo.api.app.service.AppBaseService;
import com.cafjo.carjo.api.error.NotFoundException;
import com.cafjo.carjo.api.error.ThreadException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ForgetService extends AppBaseService {
    private String Digit4Code = "";

    public Map<String, Object> sendSmsAsync(String phone) throws IOException {
        boolean isUser = checkAlradyUser(phone);
        if (isUser) {
            Digit4Code = "";
            Map<String, Object> map = new HashMap<String, Object>();
            Random r = new Random();
            for (int iter = 0; iter < codeDigits; iter++) {
                Digit4Code += r.nextInt(10);
            }
            System.out.println("Digit4Code: " + Digit4Code);
            relansService.sendVerficationCode(phone, Digit4Code);
            map.put("user_token", tokenUtil.generateTokenFromPhone(phone));
            map.put("sms_token", passwordEncoder().encode(Digit4Code));
            return map;
        } else {      
            throw new NotFoundException("This phone is not for real user");
        }
    }

    public boolean smsUpdatePassword(String token,String new_password) {
        try {

            String phone = getPhoneFromToken(token);
            String password = passwordEncoder().encode(new_password);
            System.out.println($("update users_info set password='%s'where phone='%s'", password, phone));
            jdbcTemplate.update($("update users_info set password='%s'where phone='%s'", password, phone));
            return true;
        } catch (Exception e) {
            throw new ThreadException("There's something wrong in this password");
        }
    }
}
