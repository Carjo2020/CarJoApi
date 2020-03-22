package com.cafjo.carjo.api.all;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class RelansService {
    private String SENDER_ID = "D0ygW9QqM7e5ylRe2vp5lo4BG";
    private String API = "eaecb9656afbd9db955b22a49d78f5e5a582d2778d55ee09a448a3aa8f7a8f90";

    public void sendVerficationCode(String phone, String code) throws IOException {
        sendReleansSMS(phone, "Your Verfication code is:" + code);
    }

    public void sendReleansSMS(String mobileNumber, String message) throws ClientProtocolException, IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://platform.releans.com/api/message/send");
        httpPost.addHeader("Authorization", "Bearer " + API);
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
        List<BasicNameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("senderId", SENDER_ID));
        nvps.add(new BasicNameValuePair("mobileNumber", mobileNumber));
        nvps.add(new BasicNameValuePair("message", message));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        System.out.println("Sms sent status: " + response.getStatusLine().getStatusCode());
        client.close();
    }

}
