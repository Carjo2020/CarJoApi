package com.cafjo.carjo.api.app.service;

import com.cafjo.carjo.api.app.entity.signin.SignInRequest;
import com.cafjo.carjo.api.app.entity.signup.User_Info;
import com.cafjo.carjo.api.cpanel.entity.admins_info;
import com.cafjo.carjo.api.error.BadRequestException;
import com.cafjo.carjo.api.error.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Service
@Component
public class TokenUtil extends AppBaseService {

    private final String CLAIMS_SUBJECT = "sub";
    private final String CLAIMS_CREATED = "created";
    private final String CLAIMS_ISSUER = "iss";
    //    @Value("${auth.expiration}")
    private Long TOKEN_VALIDITY = 604800L;

    //    @Value("${auth.secret}")
    private String TOKEN_SECRET = "4ccedb66ed687a731a439da862cb6248231ca2bcc6b000653474b2bc4138c6d8";
//    private String SMS_TOKEN_SECRET = "smsa439da3122cbbcc6866248d687b000653474b2bce3a74c231cacedb668d841c6";

    public String generateTokenFromPhone(String phone) {

        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_SUBJECT, phone);
        claims.put(CLAIMS_CREATED, new Date());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

    public String getPhoneFromToken(String token) {
        try {
            Claims claims = getClaims(token);
            String phone = claims.getSubject();

            return claims.getSubject();
        } catch (Exception ex) {
            throw new NotFoundException("Phone for this token does not found");
        }
    }

    public SignInRequest getSignInRequestFromToken(String token) {
        try {
            Claims claims = getClaims(token);
            String phone = claims.getSubject();
            String pass = claims.getIssuer();
            return new SignInRequest(phone, pass);
        } catch (Exception ex) {
            return null;
        }
    }

    public admins_info getAdminsInfoFromToken(String token) {
        try {
            Claims claims = getClaims(token);
            String phone = claims.getSubject();
            String pass = claims.getIssuer();
            System.out.println("phone:"+phone);
            System.out.println("pass:"+pass);
            return new admins_info(phone, pass);
        } catch (Exception ex) {
            return null;
        }
    }

    public Map<String, Object> getUserInfoFromToken(String token) {
        System.out.println("run");
        SignInRequest signInRequest = tokenUtil.getSignInRequestFromToken(token);
        System.out.println("signInRequest:" + signInRequest);
        String phone = signInRequest.getUsername();
        String password = signInRequest.getPassword();
        System.out.println("phone" + phone);
        System.out.println("password" + password);
        if (phone != null) {
            String passwordEncoder = getValue("users_info", "password", "phone", phone);
            boolean matechs = passwordEncoder().matches(password, passwordEncoder);
            System.out.println("matechs: " + matechs);
            if (matechs) {
                return jdbcTemplate.queryForList($("select * from users_info where phone='%s'", phone)).get(0);
            } else {
                throw new BadRequestException("Password does not matches");
            }
        } else {
            throw new NotFoundException("User not found");
        }

    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000);
    }

    public boolean isTokenValid(String token, User_Info user) {
        String username = getPhoneFromToken(token);

        return (username.equals(user.getPhone()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            claims = null;
        }

        return claims;
    }

    public String generateTokenFromPhoneAndPassword(SignInRequest user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_SUBJECT, user.getUsername());
        claims.put(CLAIMS_ISSUER, user.getPassword());
        claims.put(CLAIMS_CREATED, new Date());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET)
                .compact();
    }

//    public String generateSmsToken(String phone) {
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(CLAIMS_SUBJECT, phone);
//        claims.put(CLAIMS_CREATED, new Date());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(generateExpirationDate())
//                .signWith(SignatureAlgorithm.HS512, SMS_TOKEN_SECRET)
//                .compact();
//    }
//    public String getPhoneFromSmsToken(String token) {
//        try {
//            Claims claims = getSmsClaims(token);
//
//            return claims.getSubject();
//        } catch (Exception ex) {
//            return null;
//        }
//    }
//    private Claims getSmsClaims(String token) {
//        Claims claims;
//        try {
//            claims = Jwts.parser().setSigningKey(SMS_TOKEN_SECRET)
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception ex) {
//            claims = null;
//        }
//        return claims;
//    }
}
