package com.cafjo.carjo.api.app.controller.user;

public class BaseController {
    public String getToken(String BearerToken){
        return BearerToken.replaceFirst("Bearer", "").replaceFirst("bearer", "").replaceAll(" ", "");
    }
}
