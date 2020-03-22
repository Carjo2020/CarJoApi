package com.cafjo.carjo.api.cpanel.service;

import com.cafjo.carjo.api.app.entity.signin.SignInRequest;
import com.cafjo.carjo.api.error.BadRequestException;
import com.cafjo.carjo.api.app.entity.JwtResponse;
import com.cafjo.carjo.api.app.service.AppBaseService;

import java.util.List;
import java.util.Map;

import com.cafjo.carjo.api.cpanel.entity.admins_info;
import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminsService extends AppBaseService {

    public JwtResponse checkLogin(String username, String password) {
        String passwordEncoder = getValue("admins_info", "password", "username", username);
        System.out.println("passwordEncoder: " + passwordEncoder);
//        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT password FROM admins_info where username='" + username + "'");
        if (passwordEncoder.equals("null")) {
            throw new NotFoundException("This admin does not found");
        } else {
//            String passwordEncoder = (String) list.get(0).get("password");
            if (passwordEncoder().matches(password, passwordEncoder)) {
                return new JwtResponse(tokenUtil.generateTokenFromPhoneAndPassword(new SignInRequest(username, password)));
            } else {
                throw new BadRequestException("password not correct");
            }
        }
    }

    public boolean autoLogin(String user_token) {
        admins_info signInRequest = tokenUtil.getAdminsInfoFromToken(user_token);
        if (signInRequest != null) {
            String username = signInRequest.getUsername();
            String password = signInRequest.getPassword();
            System.out.println(username);
            System.out.println(password);
            String passwordEncoder = getValue("admins_info", "password", "username", username);
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
//    public void addAdmin(String username, String password) {
//        double count = getTableCount(" admins_info ", "id") + 1;
//        System.out.println(count);
//        jdbcTemplate.update("insert into admins_info values (1,'admin','admin');");
//    }

    public boolean addAdmin(String authorizationHeader, admins_info admin) {
        checkAdmin(authorizationHeader);
        try {
            if (admin.getId().equals("null")) {
                int count = getTableCount("admins_info", "id") + 1;
                jdbcTemplate.update(String.format("insert into admins_info values(%s,'%s','%s','%s');",
                        count + "",
                        admin.getUsername(),
                        passwordEncoder().encode(admin.getPassword()),
                        admin.getRole()
                ));
                return true;
            } else {
                updateAdmin(admin);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void updateAdmin(admins_info admin) {
        String username = (admin.getUsername().equals("null")) ? getValue("admins_info", "username", "id", admin.getId()) : admin.getUsername();
        String password = (admin.getPassword().equals("null")) ? getValue("admins_info", "password", "id", admin.getId()) : passwordEncoder().encode(admin.getPassword());
        String role = (admin.getRole().equals("null")) ? getValue("admins_info", "role", "id", admin.getId()) : admin.getRole();

        jdbcTemplate.update(String.format("update admins_info set username='%s', password='%s', role='%s' where id='%s'",
                username,
                password,
                role,
                admin.getId()
        ));
    }

    private void checkAdmin(String authorizationHeader) {
        admins_info signInRequest = tokenUtil.getAdminsInfoFromToken(authorizationHeader.replaceFirst("bearer", "").replaceAll(" ", ""));
        if (signInRequest != null) {
            String username = signInRequest.getUsername();
            System.out.println("username: " + username);
            if (checkAlradyAdmin(username)) {
                String password = signInRequest.getPassword();
                String passwordEncoder = getValue("admins_info", "password", "username", username);
                System.out.println("passwordEncoder: " + passwordEncoder);
                boolean matechs = passwordEncoder().matches(password, passwordEncoder);
                System.out.println("matechs: " + matechs);
                if (!matechs) {
                    throw new BadRequestException("Password does not matches");
                }
            } else {
                throw new NotFoundException("User not found");
            }
        } else {
            throw new NotFoundException("There's no admin with this token");
        }

    }

    public boolean checkAlradyAdmin(String phone) {
        System.out.println("select count(id) from admins_info where  username='" + phone + "'");
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select count(id) from admins_info where  username='" + phone + "'");
        Map<String, Object> map = list.get(0);
        Double no = Double.valueOf(map.get("count") + "");
        System.out.println("no: " + no);
        if (no != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteAdmin(String authorizationHeader, String id) {
        checkAdmin(authorizationHeader);
        jdbcTemplate.execute("delete from admins_info where id =" + id);
        return true;
    }

    public List<Map<String, Object>> getAllAdmins(String authorizationHeader) {
        checkAdmin(authorizationHeader);
        return jdbcTemplate.queryForList("select * from admins_info ORDER BY id ASC;");
    }
}
