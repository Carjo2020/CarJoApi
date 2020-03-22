package com.cafjo.carjo.api.app.service.user;

import com.cafjo.carjo.api.app.service.AppBaseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService extends AppBaseService {
    private int LIST_SIZE = 10;

    public List<Map<String, Object>>
    getAllPosts(String token, int page) {
        String start_from = (page < 1) ? "0" : ((page - 1) * LIST_SIZE) + "";
        List<Map<String, Object>> old_list = jdbcTemplate.queryForList($("SELECT * FROM posts_data ORDER BY post_id LIMIT %s OFFSET %s;",
                LIST_SIZE + "", start_from));
        List<Map<String, Object>> new_list = new ArrayList<>();

        for (Map<String, Object> map : old_list) {
            Map<String, Object> new_map = new HashMap<>();
            String post_id = map.get("post_id").toString();
            boolean is_liked_me = checkIfLikedMe(token, post_id);
            boolean is_favorited_me = checkIfFavoritedMe(token, post_id);

            new_map.put("post_id", map.get("post_id"));
            new_map.put("title", map.get("title"));
            new_map.put("url_picture", map.get("url_picture"));
            new_map.put("url_video", map.get("url_video"));
            new_map.put("text", map.get("text"));
            new_map.put("date", map.get("date"));
            new_map.put("id_category", map.get("id_category"));
            new_map.put("likes", map.get("likes"));
            new_map.put("favorites", map.get("favorites"));
            new_map.put("is_liked_me", is_liked_me);
            new_map.put("is_favorited_me", is_favorited_me);

            new_list.add(new_map);
        }
        return new_list;
    }

    private boolean checkIfLikedMe(String token, String post_id) {
        String phone = getPhoneFromToken(token);
        String post_like = getValue("users_info", "post_like", "phone", phone);
        if (post_like.contains("," + post_id + ",")) {
            return true;
        }
        return false;
    }

    private boolean checkIfFavoritedMe(String token, String post_id) {
        String phone = getPhoneFromToken(token);
        String post_favorite = getValue("users_info", "post_favorite", "phone", phone);
        if (post_favorite.contains("," + post_id + ",")) {
            return true;
        }
        return false;
    }

    public List<Map<String, Object>> getAllPostsByCategory(int id_category, int page) {
        String start_from = (page < 1) ? "0" : ((page - 1) * LIST_SIZE) + "";
//        System.out.println($("SELECT * FROM posts_data where id_catagory like '%,%s,%' ORDER BY post_id LIMIT %s OFFSET %s;",
//                id_category + "", LIST_SIZE + "", start_from));
        return jdbcTemplate.queryForList("SELECT * FROM posts_data where id_category like '%," + id_category + ",%' ORDER BY post_id LIMIT " +
                LIST_SIZE + " OFFSET " + start_from + ";");
    }
}
