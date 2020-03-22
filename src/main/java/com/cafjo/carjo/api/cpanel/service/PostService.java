package com.cafjo.carjo.api.cpanel.service;

import com.cafjo.carjo.api.cpanel.entity.PostBody;
import com.cafjo.carjo.api.error.BadRequestException;
import com.cafjo.carjo.api.app.service.AppBaseService;
import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class PostService extends AppBaseService {

    public boolean addPost(PostBody body, MultipartFile image, MultipartFile video) throws IOException {

//        String post_id_body = body.getpost_id();
        String title = body.getTitle();
        String text = body.getText();
        String date = new Date().getTime() + "";
        String id_category = body.getId_category();
        String likes = "0";
        String favorites = "0";

        String url_picture = "";
        String url_video = "";
        System.out.println("image: " + image);
        if (image == null) {
            url_picture = body.getUrl_picture();
        } else {
            url_picture = bucketService.getFileUrl(image);
        }
        if (video == null) {
            url_video = body.getUrl_video();
        } else {
            url_video = bucketService.getFileUrl(video);
        }
        try {
            int generate_post_id = getTableCount("posts_data", "post_id") + 1;
            jdbcTemplate.update($("insert into posts_data values(%s,'%s','%s','%s','%s','%s','%s','%s','%s');",
                    generate_post_id + "",
                    title,
                    url_picture,
                    url_video,
                    text,
                    date,
                    id_category,
                    likes,
                    favorites
            ));
            return true;
        } catch (Exception e) {
            throw new BadRequestException("There's something wrong");
        }


    }


    public boolean addPost(String post_id, String title, String id_category, String text, String url_picture, String url_video, MultipartFile file_picture, MultipartFile file_video) throws IOException {

        String date = new Date().getTime() + "";
        String likes = "0";
        String favorites = "0";
        if (file_picture != null) {
            url_picture = bucketService.getFileUrl(file_picture);
        }
        if (file_video != null) {
            url_video = bucketService.getFileUrl(file_video);
        }

        if (post_id.equals("null")) {
            try {
                int generate_post_id = getTableCount("posts_data", "post_id") + 1;
                jdbcTemplate.update($("insert into posts_data values(%s,'%s','%s','%s','%s','%s','%s','%s','%s');",
                        generate_post_id + "",
                        title,
                        url_picture,
                        url_video,
                        text,
                        date,
                        "," + id_category + ",",
                        likes,
                        favorites
                ));
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException("There's something wrong");
            }
        } else {
            updatePost(title, url_picture, url_video, text, date, id_category, post_id);
        }
        return true;


    }

    public void updatePost(String title, String url_picture, String url_video, String text, String date, String id_category, String post_id) {
        if (isThisAlradyPost(post_id)) {
            String old_url_picture = getValue("posts_data", "url_picture", "post_id", post_id);
            String old_url_video = getValue("posts_data", "url_video", "post_id", post_id);
            if (isFromBucket(old_url_picture) && !url_picture.equals(old_url_picture) && !url_picture.equals("null")) {
                System.out.println("isFromBucket(old_url_picture) && !url_picture.equals(old_url_picture): " + (isFromBucket(old_url_picture) && !url_picture.equals(old_url_picture)));
                bucketService.deleteFromURL(old_url_picture);
            }
            if (isFromBucket(old_url_video) && !url_video.equals(old_url_video) && !url_video.equals("null")) {
                System.out.println("(isFromBucket(old_url_video) && !url_video.equals(old_url_video)): " + (isFromBucket(old_url_video) && !url_video.equals(old_url_video)));
                bucketService.deleteFromURL(old_url_video);
            }

            title = (title.equals("null")) ? getValue("posts_data", "title", "post_id", post_id) : title;
            url_picture = (url_picture.equals("null")) ? getValue("posts_data", "url_picture", "post_id", post_id) : url_picture;
            url_video = (url_video.equals("null")) ? getValue("posts_data", "url_video", "post_id", post_id) : url_video;
            text = (text.equals("null")) ? getValue("posts_data", "text", "post_id", post_id) : text;
            date = (date.equals("null")) ? getValue("posts_data", "date", "post_id", post_id) : date;
            id_category = (id_category.equals("null")) ? getValue("posts_data", "id_category", "post_id", post_id) : ("," + id_category + ",");
            System.out.println($("update posts_data set title='%s',url_picture='%s',url_video='%s',text='%s',date='%s',id_category='%s' where post_id=%s",
                    title,
                    url_picture,
                    url_video,
                    text,
                    date,
                    id_category,
                    post_id
            ));
            jdbcTemplate.update($("update posts_data set title='%s',url_picture='%s',url_video='%s',text='%s',date='%s',id_category='%s' where post_id=%s",
                    title,
                    url_picture,
                    url_video,
                    text,
                    date,
                    id_category,
                    post_id
            ));
        } else {
            throw new NotFoundException("There's no post with this post_id");
        }
    }

    public boolean isThisAlradyPost(String post_id) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select count(*) from posts_data where post_id=" + post_id);
        String respone = list.get(0).get("count") + "";
        if (!respone.equals("null") && !respone.equals(null) && !respone.equals("")) {
            if (Integer.valueOf(respone) == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean deletePost(String post_id) {
        String url_picture = getValue("posts_data", "url_picture", "post_id", post_id);
        String url_video = getValue("posts_data", "url_video", "post_id", post_id);
        deleteURLIfFromBucket(url_picture);
        deleteURLIfFromBucket(url_video);
        jdbcTemplate.update("delete from posts_data where post_id=" + post_id);
        return true;
    }

    public boolean isFromBucket(String url) {
        return url.contains("rasheeqat-47924.appspot.com");
    }

    public void deleteURLIfFromBucket(String url) {
        if (isFromBucket(url)) {
            if (!isForAnother(url)) {
                bucketService.deleteFromURL(url);
            } else {
                System.out.println("WARING: THIS URL IS FOR ANOTHER POST");
            }
        }
    }

    public boolean isForAnother(String url) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList($("select Count(*) from posts_data where url_picture='%s' or url_video='%s'", url, url));
        String respone = list.get(0).get("Count") + "";
        if (!respone.equals("null") && !respone.equals(null) && !respone.equals("")) {
            int count = Integer.valueOf(respone);
            if (count > 1) {
                return true;
            }
        } else {
            System.out.println("nullllll");
        }
        return false;
    }

}



