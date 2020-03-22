package com.cafjo.carjo.api.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Khalid Elshafie <abolkog@gmail.com>
 * @Created 08/10/2018 9:20 PM.
 */
@Service
public class UserServices implements UserDetailsService {

//    @Autowired
//    private UserRepository userRepository;

//    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return new User("test",passwordEncoder().encode("test"), AuthorityUtils.NO_AUTHORITIES);
//        AppUser user = userRepository.findByEmail(username);
//        if (user == null) {
//            throw new NotFoundException("User not found");
//        }

//        return new User("fuad","123", AuthorityUtils.NO_AUTHORITIES);
    }

//    public void save(AppUser user) {
//        user.setPassword(passwordEncoder().encode(user.getPassword()));
//        this.userRepository.save(user);
//    }

//    public List<AppUser> findAll() {
//        return userRepository.findAll();
//    }
}
