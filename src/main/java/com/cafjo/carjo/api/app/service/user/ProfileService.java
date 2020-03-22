package com.cafjo.carjo.api.app.service.user;

import com.cafjo.carjo.api.app.entity.RestPassword;
import com.cafjo.carjo.api.app.service.AppBaseService;
import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ProfileService extends AppBaseService {
    private String Digit4Code = "";

    public Map<String, Object> sendSmsAsync(String phone) throws IOException {
        boolean isUser = checkAlradyUser(phone);
        if (!isUser) {
            Digit4Code = "";
            Map<String, Object> map = new HashMap<String, Object>();
            Random r = new Random();
            for (int iter = 0; iter < codeDigits; iter++) {
                Digit4Code += r.nextInt(10);
            }
            System.out.println("Digit4Code: " + Digit4Code);
            relansService.sendVerficationCode(phone, Digit4Code);
            map.put("sms_token", passwordEncoder().encode(Digit4Code));
            return map;
        } else {
            throw new NotFoundException("This phone is for another user");
        }
    }

    public boolean updatePhone(String token, String phone) {
        String old_phone = getPhoneFromToken(token);
        boolean isUser = checkAlradyUser(phone);
        if (!isUser) {
            jdbcTemplate.update($("update users_info set phone='%s' where phone='%s'", phone, old_phone));
            return true;
        } else {
            throw new NotFoundException("This phone is for another user");
        }
    }

    public Map<String, Object> getUserFromToken(String token) {
        return tokenUtil.getUserInfoFromToken(token);
    }

    public boolean restPassword(String token, RestPassword restPassword) {
        String phone = getPhoneFromToken(token);
        String old_password = restPassword.getOld_password();
        String new_password = restPassword.getNew_password();
        String passwordEncoder = getValue("users_info", "password", "phone", phone);
        boolean matechs = passwordEncoder().matches(old_password, passwordEncoder);
        System.out.println("matechs: " + matechs);
        if (matechs) {
            System.out.println($("update users_info set password='%s'where phone='%s'", passwordEncoder().encode(new_password), phone));
            jdbcTemplate.update($("update users_info set password='%s'where phone='%s'",
                    passwordEncoder().encode(new_password), phone));
            return true;
        } else {
            throw new NotFoundException("Old password does not match current password");
        }
    }

    public boolean seFavorite(String user_token, String post_id) {
        try {
            String phone = tokenUtil.getPhoneFromToken(user_token);
            String old_id_favority = getValue("users_info", "post_favorite", "phone", phone);
            if (!old_id_favority.contains("," + post_id + ",")) {
                System.out.println("old_id_favority: " + old_id_favority);
                jdbcTemplate.update($("update users_info set post_favorite='%s' where phone='%s'", old_id_favority + post_id + ",", phone));
                changePost("favorites", 1, post_id);
                changeUserPost("count_post_favorite", 1, phone);
            }
            return true;
        } catch (Exception e) {
            throw new NotFoundException("There's something wrong");
        }
    }

    public boolean setLike(String user_token, String post_id) {
        try {
            String phone = tokenUtil.getPhoneFromToken(user_token);
            String old_id_like = getValue("users_info", "post_like", "phone", phone);
           /* if (!old_id_like.contains("," + post_id + ",") && old_id_like.length() == 0) {
                jdbcTemplate.update($("update users_info set post_like='%s' where phone='%s'", "," + old_id_like + post_id + ",", phone));
            } else */
            if (!old_id_like.contains("," + post_id + ",")) {
                jdbcTemplate.update($("update users_info set post_like='%s' where phone='%s'", old_id_like + post_id + ",", phone));
                changePost("likes", 1, post_id);
                changeUserPost("count_post_likes", 1, phone);
            }
            return true;
        } catch (Exception e) {
            throw new NotFoundException("There's something wrong");
        }
    }

    private void changePost(String type, int value, String post_id) {
        String old_type = getValue("posts_data", type, "post_id", post_id);
        int new_type = Integer.valueOf(old_type) + value;
        new_type = (new_type < 0) ? 0 : new_type;
        System.out.println($("update posts_data set %s ='%s' where post_id=%s", type, new_type + "", post_id));
        jdbcTemplate.update($("update posts_data set %s ='%s' where post_id=%s", type, new_type + "", post_id));
    }

    private void changeUserPost(String type, int value, String phone) {
        String old_type = getValue("users_info", type, "phone", phone);
        int new_type = Integer.valueOf(old_type) + value;
        new_type = (new_type < 0) ? 0 : new_type;
        System.out.println($("update users_info set %s ='%s' where phone='%s'", type, new_type + "", phone));
        jdbcTemplate.update($("update users_info set %s ='%s' where phone='%s'", type, new_type + "", phone));
    }

    public boolean deleteFavorite(String user_token, String post_id) {
        String phone = tokenUtil.getPhoneFromToken(user_token);
        try {
            String old_id_favority = getValue("users_info", "post_favorite", "phone", phone);
            if (old_id_favority.contains("," + post_id + ",")) {
                System.out.println("old_id_favority: " + old_id_favority);
                jdbcTemplate.update($("update users_info set post_favorite='%s' where phone='%s'", old_id_favority.replaceAll("," + post_id + ",", ","), phone));
                changePost("favorites", -1, post_id);
                changeUserPost("count_post_favorite", -1, phone);
            }
            return true;
        } catch (Exception e) {
            throw new NotFoundException("There's something wrong");
        }
    }

    public boolean deleteLike(String user_token, String post_id) {
        String phone = tokenUtil.getPhoneFromToken(user_token);
        try {
            String old_id_like = getValue("users_info", "post_like", "phone", phone);
            if (old_id_like.contains("," + post_id + ",")) {
                System.out.println("old_id_like: " + old_id_like);
                jdbcTemplate.update($("update users_info set post_like='%s' where phone='%s'", old_id_like.replaceAll("," + post_id + ",", ","), phone));
                changePost("likes", -1, post_id);
                changeUserPost("count_post_likes", -1, phone);
            }
            return true;
        } catch (Exception e) {
            throw new NotFoundException("There's something wrong");
        }
    }

    public List<Map<String, Object>> getPostByType(String token, String type) {
        List<Map<String, Object>> list = new ArrayList<>();
        String phone = tokenUtil.getPhoneFromToken(token);
        if (type.equalsIgnoreCase("like")) {
            String old_id_like = getValue("users_info", "post_like", "phone", phone).replaceFirst(",", "");

            String[] old_id_likes = old_id_like.split(",");
            if (old_id_likes.length == 0) {
                throw new NotFoundException("Empty List");
            }
            for (int i = 0; i < old_id_likes.length ; i++) {
                String id = old_id_likes[i];
                System.out.println("id" + id);
                List<Map<String, Object>> maps_posts = jdbcTemplate.queryForList("select * from posts_data where post_id=" + id);
                list.add(maps_posts.get(0));

            }
            return list;
        } else if (type.equalsIgnoreCase("favorite")) {
            String old_id_favorite = getValue("users_info", "post_favorite", "phone", phone).replaceFirst(",", "");
            String[] old_id_favorites = old_id_favorite.split(",");
            if (old_id_favorites.length == 0) {
                throw new NotFoundException("Empty List");
            }
            for (String id : old_id_favorites) {
                List<Map<String, Object>> maps_posts = jdbcTemplate.queryForList("select * from posts_data where post_id=" + id);
                list.add(maps_posts.get(0));
            }
            return list;
        }
        return null;
    }

    public boolean updateUser(String token, String first_name, String last_name, String email, String date, String old_password, String new_password) {
        if (!old_password.equals("null") && !new_password.equals("null") && !old_password.equals("") && !new_password.equals("")) {
            restPassword(token, new RestPassword(old_password, new_password));
        }
        String phone = tokenUtil.getPhoneFromToken(token);
        String id = getValue("users_info", "id", "phone", phone);
        updateUsersInfo(id, "first_name", first_name);
        updateUsersInfo(id, "last_name", last_name);
        updateUsersInfo(id, "email", email);
        updateUsersInfo(id, "date", date);
        jdbcTemplate.update($("update users_info set first_name='%s',last_name='%s',email='%s',date='%s' where phone='%s'",
                first_name, last_name, email, date, phone));
        return true;
    }


}
