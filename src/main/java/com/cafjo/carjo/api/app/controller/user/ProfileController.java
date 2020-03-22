package com.cafjo.carjo.api.app.controller.user;

import com.cafjo.carjo.api.app.entity.Phone;
import com.cafjo.carjo.api.app.service.user.ProfileService;
import com.cafjo.carjo.api.error.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/app/profile")
public class ProfileController extends BaseController {

    private static final String AUTHORIZATION = "Bearer TOKEN";
    @Autowired
    private ProfileService service;

    @GetMapping(value = "/getuser")
    public Map<String, Object> getUser(@RequestHeader(value = "Authorization") String authorizationHeader) {
        String token = authorizationHeader.replaceFirst("Bearer", "").replaceFirst("bearer", "").replaceAll(" ", "");
        return service.getUserFromToken(token);
    }

    @PostMapping(value = "/sendsms")
    public Map<String, Object> sendSmsAsync(@RequestBody Phone phone) throws IOException {
        return service.sendSmsAsync(phone.getPhone());
    }
    @PostMapping(value = "/updatephone")
    public boolean updatePhone(@RequestHeader(value = "Authorization") String authorizationHeader,@RequestBody Phone phone) throws IOException {
        return service.updatePhone(getToken(authorizationHeader),phone.getPhone());
    }

    @GetMapping(value = "/{action}/{type}/{post_id}")
    public boolean seLike(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String action, @PathVariable String type, @PathVariable String post_id) {
        String token = authorizationHeader.replaceFirst("Bearer", "").replaceFirst("bearer", "").replaceAll(" ", "");
        if (action.equalsIgnoreCase("set")) {
            if (type.equalsIgnoreCase("like")) {
                return service.setLike(token, post_id);
            } else if (type.equalsIgnoreCase("favorite")) {
                return service.seFavorite(token, post_id);
            } else {
                throw new NotFoundException("There's something wrong");
            }
        } else if (action.equalsIgnoreCase("delete")) {
            if (type.equalsIgnoreCase("like")) {
                return service.deleteLike(token, post_id);
            } else if (type.equalsIgnoreCase("favorite")) {
                return service.deleteFavorite(token, post_id);
            } else {
                throw new NotFoundException("There's something wrong");
            }
        }
        throw new NotFoundException("There's something wrong");
    }
    //    @GetMapping(value = "/setlike")
//    public boolean seLike(
//            @RequestHeader(value="Authorization") String authorizationHeader
////            HttpServletRequest httpServletRequest/*, @RequestBody PostStatusResponse respone*/
//    ) {
////        System.out.println("\n[username]: " + authentication.getName() + "\n");
////        String token= httpServletRequest.getHeader(AUTHORIZATION);
//        System.out.println(authorizationHeader);
////        token= StringUtils.removeStart(token, "Bearer").trim();
////        return service.seLike(respone);
//        return true;
//    }
//    @GetMapping(value = "/deleteaction/{type}/{post_id}")
//    public boolean deleteLike(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String type, @PathVariable String post_id) {
//        String token = authorizationHeader.replaceAll("Bearer", "").replaceAll("bearer", "").replaceAll(" ", "");
//        if (type.equalsIgnoreCase("like")) {
//            return service.deleteLike(token, post_id);
//        } else if (type.equalsIgnoreCase("favorite")) {
//            return service.deleteFavorite(token, post_id);
//        } else {
//            throw new NotFoundException("There's something wrong");
//        }
//
//    }

    @GetMapping(value = "/getpostsbytype/{type}")
    public List<Map<String, Object>> getPostByType(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String type) {
        String token = authorizationHeader.replaceFirst("Bearer", "").replaceFirst("bearer", "").replaceAll(" ", "");
        System.out.println(token);
        return service.getPostByType(token, type);
    }
}
