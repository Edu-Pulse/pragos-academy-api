package org.binar.pragosacademyapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service(value = "userDetailService")
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Autowired
    private UserDetailServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("mencari data user by email");
        User user = userRepository.findByEmail(username).orElse(null);
        if (user == null){
            log.warn("data user dengan username: "+ username +" tidak ditemukan");
            throw new UsernameNotFoundException("invalid username or password");
        }
        log.info("data user dengan username: "+ username + " ditemukan");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user){
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ user.getRole()));
        return authorities;
    }
}
