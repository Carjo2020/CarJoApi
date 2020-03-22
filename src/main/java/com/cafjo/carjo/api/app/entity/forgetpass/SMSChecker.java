package com.cafjo.carjo.api.app.entity.forgetpass;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SMSChecker {
//    private String user_token;
    private String sms_token;
    private String code;
}
