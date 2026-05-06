package com.example.user_management.service.impl;

import com.example.user_management.entity.user.User;
import com.example.user_management.entity.user.UserPrincipal;
import com.example.user_management.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo repo;

    public MyUserDetailsServiceImpl(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        UserDetails user1 = new UserPrincipal(user);
        System.out.println(user1);
        return  user1;
    }

}
